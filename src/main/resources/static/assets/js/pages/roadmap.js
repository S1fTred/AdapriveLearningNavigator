import { ApiError, plansApi, quizzesApi, roadmapsApi, tutorApi } from "/assets/js/core/api.js";
import { requireAuth } from "/assets/js/core/guard.js";
import { resolveActivePlan } from "/assets/js/core/plans.js";
import { resolveActiveRoadmap } from "/assets/js/core/roadmaps.js";
import { initPrivateShell } from "/assets/js/core/shell.js";
import { getPlanDraft, setSelectedRoadmapId } from "/assets/js/core/session.js";
import { escapeHtml, formatHours, renderEmptyState, showStatus } from "/assets/js/core/ui.js";

if (requireAuth()) {
    initPrivateShell("dashboard");

    const statusBox = document.querySelector("#roadmap-status");
    const metaContainer = document.querySelector("#roadmap-meta");
    const flowContainer = document.querySelector("#roadmap-flow");
    const topicDrawer = document.querySelector("#roadmap-topic-drawer");
    const topicPanel = document.querySelector("#roadmap-topic-panel");

    const state = {
        roadmap: null,
        selectedTopicId: null,
        activePlanId: null,
        plannedTopicIds: new Set(),
        knownTopicIds: new Set(),
        topicTab: "overview",
        quizByTopicId: new Map(),
        quizLoadingTopicIds: new Set(),
        quizErrorByTopicId: new Map(),
        quizResultByQuizId: new Map(),
        tutorChatByTopicId: new Map(),
        tutorLoadingTopicIds: new Set()
    };

    bindTopicDrawerEvents();
    loadPage().catch(handleError);

    async function loadPage() {
        const query = new URLSearchParams(window.location.search);
        const explicitRoadmapId = Number(query.get("roadmapId")) || null;
        const explicitTopicId = Number(query.get("topicId")) || null;
        const explicitPlanId = Number(query.get("planId")) || null;

        const roadmap = await resolveActiveRoadmap(explicitRoadmapId);
        if (!roadmap) {
            renderEmptyState(
                metaContainer,
                "Roadmap пока не найден",
                "В базе знаний пока нет опубликованных направлений."
            );
            flowContainer.innerHTML = "";
            topicPanel.innerHTML = "";
            return;
        }

        state.roadmap = roadmap;
        setSelectedRoadmapId(roadmap.id);
        hydrateKnownTopics(roadmap);
        await hydratePlanContext(roadmap.id, explicitPlanId);
        renderMeta(roadmap);
        renderFlow(roadmap);

        const initialTopicId = resolveInitialTopicId(roadmap, explicitTopicId);
        if (initialTopicId != null) {
            await selectTopic(initialTopicId, { syncUrl: false });
        } else {
            topicPanel.innerHTML = "";
            closeTopicDrawer();
        }
    }

    function hydrateKnownTopics(roadmap) {
        const draft = getPlanDraft();
        const knownTopicIds = draft.roleId === roadmap.id ? (draft.knownTopicIds || []) : [];
        const roadmapTopicIds = new Set((roadmap.topics || []).map((topic) => topic.topicId));

        state.knownTopicIds = new Set(
            knownTopicIds.filter((topicId) => roadmapTopicIds.has(topicId))
        );
    }

    async function hydratePlanContext(roleId, explicitPlanId) {
        state.activePlanId = null;
        state.plannedTopicIds = new Set();

        let plan = null;
        if (explicitPlanId) {
            plan = await plansApi.get(explicitPlanId);
        } else {
            plan = await resolveActivePlan();
            if (!plan || plan.roleId !== roleId) {
                plan = await findLatestPlanForRoadmap(roleId);
            }
        }

        if (!plan || plan.roleId !== roleId) {
            return;
        }

        state.activePlanId = plan.id;
        for (const week of plan.weeks || []) {
            for (const step of week.steps || []) {
                state.plannedTopicIds.add(step.topicId);
            }
        }
    }

    function resolveInitialTopicId(roadmap, explicitTopicId) {
        if (explicitTopicId && roadmap.topics.some((topic) => topic.topicId === explicitTopicId)) {
            return explicitTopicId;
        }

        return null;
    }

    function renderMeta(roadmap) {
        const knownCount = state.knownTopicIds.size;
        const remainingHours = calculateRemainingRoadmapHours(roadmap, state.knownTopicIds);

        metaContainer.innerHTML = `
            <article class="card panel-card roadmap-summary-card roadmap-page-summary">
                <div>
                    <p class="eyebrow">Текущая roadmap</p>
                    <h3>${escapeHtml(roadmap.name)}</h3>
                    <p>${escapeHtml(roadmap.description || "Roadmap без описания.")}</p>
                </div>
                <div class="pill-row roadmap-summary-pills">
                    <span class="badge">${roadmap.topicCount} тем</span>
                    <span class="badge badge-dark">${roadmap.requiredTopicCount} обязательных</span>
                    <span class="badge badge-success">${escapeHtml(formatHours(remainingHours))}</span>
                    ${knownCount ? `<span class="badge">Уже знакомо: ${knownCount}</span>` : ""}
                    ${state.activePlanId ? `<span class="badge">Есть weekly plan</span>` : ""}
                </div>
                <div class="form-actions roadmap-summary-actions">
                    ${state.activePlanId
                        ? `<a class="button button-primary" href="/plan?planId=${state.activePlanId}">Открыть план</a>`
                        : `<a class="button button-primary" href="/dashboard?roadmapId=${roadmap.id}">Собрать план</a>`}
                    <a class="button button-secondary" href="/dashboard?roadmapId=${roadmap.id}${state.activePlanId ? `&planId=${state.activePlanId}` : ""}">К каталогу</a>
                </div>
            </article>
        `;
    }

    async function findLatestPlanForRoadmap(roleId) {
        const page = await plansApi.list(0, 50);
        return (page.items || [])
            .filter((plan) => plan.roleId === roleId)
            .sort((left, right) => new Date(right.createdAt) - new Date(left.createdAt))[0] || null;
    }

    function calculateRemainingRoadmapHours(roadmap, knownTopicIds) {
        return (roadmap.topics || [])
            .filter((topic) => !knownTopicIds.has(topic.topicId))
            .reduce((sum, topic) => sum + Number(topic.estimatedHours || 0), 0);
    }

    function renderFlow(roadmap) {
        if (!roadmap.topics?.length) {
            renderEmptyState(
                flowContainer,
                "Roadmap пустая",
                "Для этого направления ещё не заведены темы."
            );
            return;
        }

        const orderedTopics = sortTopics(roadmap.topics);

        flowContainer.innerHTML = `
            <div class="roadmap-graph">
                <div class="roadmap-graph-title">
                    <span>Roadmap</span>
                    <strong>${escapeHtml(roadmap.name)}</strong>
                </div>
                ${orderedTopics.map((topic, index) => renderTopicNode(topic, index)).join("")}
            </div>
        `;

        flowContainer.querySelectorAll("[data-topic-id]").forEach((button) => {
            button.addEventListener("click", async () => {
                await selectTopic(Number(button.dataset.topicId));
            });
        });
    }

    function sortTopics(topics) {
        return [...topics].sort((left, right) => {
            const leftPriority = left.priority ?? Number.MAX_SAFE_INTEGER;
            const rightPriority = right.priority ?? Number.MAX_SAFE_INTEGER;
            if (leftPriority !== rightPriority) {
                return leftPriority - rightPriority;
            }
            return left.topicId - right.topicId;
        });
    }

    function renderTopicNode(topic, index) {
        const isSelected = topic.topicId === state.selectedTopicId;
        const isPlanned = state.plannedTopicIds.has(topic.topicId);
        const isKnown = state.knownTopicIds.has(topic.topicId);
        const lane = resolveTopicLane(index, topic);

        return `
            <div class="roadmap-graph-row roadmap-row-${lane}">
                <span class="roadmap-route-marker" aria-label="Шаг ${index + 1}">${index + 1}</span>
                <button class="roadmap-topic-card roadmap-graph-node ${isSelected ? "is-selected" : ""} ${isKnown ? "is-known" : ""}" type="button" data-topic-id="${topic.topicId}">
                    <span class="roadmap-node-copy">
                        <span class="step-meta">
                            <span class="badge">${escapeHtml(formatHours(topic.estimatedHours))}</span>
                            ${topic.isRequired ? `<span class="badge badge-success">Обязательная</span>` : `<span class="badge">Дополнительная</span>`}
                        </span>
                        <strong>${escapeHtml(topic.topicTitle)}</strong>
                        <span>${escapeHtml(topic.description || "Описание откроется в правой панели.")}</span>
                        <span class="topic-tags roadmap-card-pills">
                            ${topic.isCore ? `<span class="topic-chip">Core</span>` : ""}
                            ${isKnown ? `<span class="topic-chip known">Уже знакома</span>` : ""}
                            ${!isKnown && isPlanned ? `<span class="topic-chip">В текущем плане</span>` : ""}
                        </span>
                    </span>
                </button>
            </div>
        `;
    }

    function resolveTopicLane(index, topic) {
        if (index === 0 || topic.isCore) {
            return "center";
        }

        const pattern = ["left", "right", "left", "right", "left", "right", "center"];
        return pattern[(index - 1) % pattern.length];
    }

    async function selectTopic(topicId, options = {}) {
        if (!state.roadmap) {
            return;
        }

        state.selectedTopicId = topicId;
        renderFlow(state.roadmap);

        const topic = await roadmapsApi.getTopic(state.roadmap.id, topicId);
        renderTopicPanel(topic);
        openTopicDrawer();

        if (options.syncUrl !== false) {
            syncRoadmapUrl();
        }
    }

    function renderTopicPanel(topic) {
        const isInPlan = state.plannedTopicIds.has(topic.topicId);
        const isKnown = state.knownTopicIds.has(topic.topicId);
        const tab = state.topicTab;

        topicPanel.innerHTML = `
            <div class="card panel-card topic-detail-card ${tab === "tutor" ? "is-tutor-mode" : ""}">
                <button class="topic-drawer-close" type="button" data-topic-drawer-close aria-label="Закрыть детали темы">×</button>
                <p class="eyebrow">Детали темы</p>
                <h3 id="topic-drawer-title">${escapeHtml(topic.topicTitle)}</h3>
                <p>${escapeHtml(topic.description || "Подробное описание темы пока не заполнено.")}</p>
                <div class="pill-row panel-top-gap">
                    <span class="badge">${escapeHtml(formatHours(topic.estimatedHours))}</span>
                    ${topic.isRequired ? `<span class="badge badge-success">Обязательная</span>` : `<span class="badge">Дополнительная</span>`}
                    ${isKnown ? `<span class="badge">Уже знакома</span>` : ""}
                    ${!isKnown && isInPlan ? `<span class="badge">В активном плане</span>` : ""}
                </div>

                <div class="topic-tab-row panel-top-gap">
                    <button class="topic-tab ${tab === "overview" ? "is-active" : ""}" type="button" data-topic-tab="overview">Обзор</button>
                    <button class="topic-tab ${tab === "resources" ? "is-active" : ""}" type="button" data-topic-tab="resources">Ресурсы</button>
                    <button class="topic-tab ${tab === "quiz" ? "is-active" : ""}" type="button" data-topic-tab="quiz">Квиз</button>
                    <button class="topic-tab ${tab === "tutor" ? "is-active" : ""}" type="button" data-topic-tab="tutor">AI Tutor</button>
                </div>

                <div class="topic-detail-section">
                    ${renderTopicTabContent(topic, tab, isKnown)}
                </div>
            </div>
        `;

        topicPanel.querySelectorAll("[data-topic-drawer-close]").forEach((button) => {
            button.addEventListener("click", closeTopicDrawer);
        });

        topicPanel.querySelectorAll("[data-topic-tab]").forEach((button) => {
            button.addEventListener("click", async () => {
                state.topicTab = button.dataset.topicTab;
                renderTopicPanel(topic);
                openTopicDrawer();
                if (state.topicTab === "quiz") {
                    await ensureQuizLoaded(topic);
                }
            });
        });

        bindTutorForm(topic);
        bindQuizForm(topic);
        bindQuizRetry(topic);

        if (state.topicTab === "quiz") {
            ensureQuizLoaded(topic);
        }
    }

    function bindTopicDrawerEvents() {
        topicDrawer?.querySelectorAll("[data-topic-drawer-close]").forEach((button) => {
            button.addEventListener("click", closeTopicDrawer);
        });

        document.addEventListener("keydown", (event) => {
            if (event.key === "Escape") {
                closeTopicDrawer();
            }
        });
    }

    function openTopicDrawer() {
        topicDrawer?.classList.add("is-open");
        topicDrawer?.setAttribute("aria-hidden", "false");
    }

    function closeTopicDrawer() {
        topicDrawer?.classList.remove("is-open");
        topicDrawer?.setAttribute("aria-hidden", "true");
    }

    function renderTopicTabContent(topic, tab, isKnown) {
        const resources = topic.resources || [];
        const prereqs = topic.prereqs || [];
        const unlocks = topic.unlocks || [];

        if (tab === "resources") {
            return resources.length
                ? `
                    <div class="list">
                        ${resources.map((resource) => `
                            <article class="list-item">
                                <div>
                                    <h4>${escapeHtml(resource.title)}</h4>
                                    <p>${escapeHtml(resource.provider || "Источник не указан")} · ${escapeHtml(resource.type || "RESOURCE")}</p>
                                </div>
                                <a class="button button-ghost" href="${escapeHtml(resource.url)}" target="_blank" rel="noreferrer">Открыть</a>
                            </article>
                        `).join("")}
                    </div>
                `
                : "<p>Для темы пока не прикреплены ресурсы.</p>";
        }

        if (tab === "quiz") {
            return renderQuizTab(topic);
        }

        if (tab === "tutor") {
            return renderTutorTab(topic);
        }

        return `
            <div class="topic-detail-block topic-overview-block">
                <h4>Кратко о теме</h4>
                <p>${escapeHtml(buildSmartTopicOverview(topic, isKnown, prereqs, unlocks))}</p>
            </div>
        `;

        if (tab === "tutor") {
            const quizAvailable = topic.quiz?.available;
            return `
                <div class="tutor-placeholder">
                    <h4>AI Tutor для темы</h4>
                    <p>
                        Следующий продуктовый шаг: AI Tutor будет отвечать прямо в контексте выбранной темы,
                        её зависимостей, ресурсов и места в roadmap.
                    </p>
                    <div class="pill-row">
                        <span class="badge">${quizAvailable ? "Есть квиз" : "Квиз пока не заведён"}</span>
                        <span class="badge badge-dark">${resources.length} ресурсов</span>
                    </div>
                    <button class="button button-secondary panel-top-gap" type="button" disabled>AI Tutor скоро</button>
                </div>
            `;
        }

        return `
            ${isKnown ? `
                <div class="topic-detail-block">
                    <h4>Статус</h4>
                    <p>Тема отмечена как уже знакомая. Она остаётся в roadmap для понимания структуры, но не включается в новый weekly plan.</p>
                </div>
            ` : ""}

            <div class="topic-detail-block">
                <h4>Что нужно знать перед темой</h4>
                ${prereqs.length ? `
                    <div class="topic-tags">
                        ${prereqs.map((item) => `<span class="topic-chip">${escapeHtml(item.topicTitle)}</span>`).join("")}
                    </div>
                ` : "<p>Эту тему можно брать без обязательных предварительных тем.</p>"}
            </div>

            <div class="topic-detail-block">
                <h4>Что открывает дальше</h4>
                ${unlocks.length ? `
                    <div class="topic-tags">
                        ${unlocks.map((item) => `<span class="topic-chip muted">${escapeHtml(item.topicTitle)}</span>`).join("")}
                    </div>
                ` : "<p>Это конечная тема внутри текущего roadmap.</p>"}
            </div>

            <div class="topic-detail-block">
                <h4>Квиз</h4>
                <p>${topic.quiz?.available ? `Для темы доступен квиз «${escapeHtml(topic.quiz.title)}».` : "Для темы квиз пока не добавлен."}</p>
            </div>
        `;
    }

    function buildTopicOverview(topic, isKnown, prereqs, unlocks) {
        const title = topic.topicTitle || "эта тема";
        const description = topic.description && topic.description.trim()
            ? topic.description.trim()
            : `Тема «${title}» помогает закрыть один из шагов выбранного roadmap и постепенно перейти к более сложным разделам.`;
        const prereqPart = prereqs.length
            ? `Перед ней полезно понимать: ${prereqs.slice(0, 3).map((item) => item.topicTitle).join(", ")}.`
            : "Её можно изучать без обязательной предварительной темы.";
        const nextPart = unlocks.length
            ? `После неё станут понятнее следующие шаги: ${unlocks.slice(0, 3).map((item) => item.topicTitle).join(", ")}.`
            : "Это один из завершающих или самостоятельных блоков внутри текущего маршрута.";
        const knownPart = isKnown
            ? "Вы уже отметили её как знакомую, поэтому она сохраняется в roadmap для структуры, но не мешает персональному плану."
            : "Если тема уже знакома, её можно отметить в каталоге перед сборкой персонального плана.";

        return `${description} ${prereqPart} ${nextPart} ${knownPart}`;
    }

    function buildSmartTopicOverview(topic, isKnown, prereqs, unlocks) {
        const title = topic.topicTitle || "эта тема";
        const fingerprint = normalizeOverviewFingerprint([
            topic.topicCode,
            topic.topicTitle,
            topic.description,
            state.roadmap?.name
        ].filter(Boolean).join(" "));
        const specificOverview = resolveSpecificTopicOverview(fingerprint);
        const sourceDescription = topic.description && topic.description.trim()
            ? cleanupGeneratedDescription(topic.description.trim(), title)
            : "";
        const base = specificOverview || sourceDescription || fallbackTopicOverview(title, fingerprint);
        const context = buildOverviewContext(isKnown, prereqs, unlocks);

        return `${base} ${context}`.trim();
    }

    function resolveSpecificTopicOverview(fingerprint) {
        const templates = [
            {
                markers: ["sql", "structured query language", "баз данных"],
                text: "SQL — язык работы с реляционными базами данных. Через него описывают структуру данных, выбирают записи, фильтруют результаты, объединяют таблицы и изменяют данные. Понимание SQL важно почти в любом backend, analytics или data-направлении, потому что большинство прикладных систем опирается на постоянное хранилище."
            },
            {
                markers: ["oop", "object oriented", "объект", "класс", "инкапсуляц"],
                text: "ООП помогает моделировать программу через объекты, которые объединяют данные и поведение. Основные идеи — инкапсуляция, наследование, полиморфизм и абстракция — нужны, чтобы держать код связанным с предметной областью и строить расширяемые приложения."
            },
            {
                markers: ["string", "строк", "methods", "метод"],
                text: "Строки используются для хранения и обработки текста: имён, сообщений, данных из форм, URL и ответов API. В Java строка String неизменяема, поэтому методы вроде substring, replace или toLowerCase возвращают новый результат. Важно понимать сравнение строк, работу с индексами и типичные операции преобразования текста."
            },
            {
                markers: ["array", "массив"],
                text: "Массив хранит последовательность элементов и даёт быстрый доступ по индексу. Эта структура лежит в основе многих коллекций, алгоритмов и задач на обработку данных. Важно понимать нулевую индексацию, границы массива, проход по элементам и отличие массива от динамических списков."
            },
            {
                markers: ["loop", "цикл"],
                text: "Циклы нужны, когда один и тот же фрагмент логики выполняется много раз: пройти по массиву, читать данные, повторять попытку или обрабатывать коллекцию. Ключевой навык — понимать условие остановки, чтобы не получить бесконечный цикл, и выбирать подходящий вид цикла под задачу."
            },
            {
                markers: ["condition", "услов"],
                text: "Условия позволяют программе принимать решения: выполнить один блок кода или другой в зависимости от данных. Через if, else, switch и логические выражения строится большая часть бизнес-логики: проверки прав, валидация форм, обработка статусов и ветвление сценариев."
            },
            {
                markers: ["http", "rest", "api"],
                text: "HTTP и REST описывают, как клиент и сервер обмениваются данными. Метод запроса показывает намерение, URL указывает ресурс, статус ответа сообщает результат, а тело передаёт данные. Это база для web API, мобильных приложений, frontend-backend связки и интеграций между сервисами."
            },
            {
                markers: ["git", "github"],
                text: "Git хранит историю изменений проекта и позволяет безопасно работать с кодом в команде. Коммиты фиксируют состояние, ветки изолируют задачи, merge и pull request помогают объединять изменения. Без Git сложно контролировать версии, ревью и откаты."
            },
            {
                markers: ["docker", "container"],
                text: "Docker упаковывает приложение вместе с окружением в контейнер, чтобы оно одинаково запускалось на разных машинах. Образ описывает, что должно быть внутри, а контейнер является запущенным экземпляром этого образа. Это упрощает локальную разработку, деплой и изоляцию зависимостей."
            },
            {
                markers: ["kubernetes", "k8s"],
                text: "Kubernetes управляет контейнерными приложениями в кластере: запускает поды, следит за доступностью, масштабирует нагрузку и помогает выкатывать обновления. Его используют там, где одного Docker-контейнера уже мало и нужна управляемая инфраструктура."
            },
            {
                markers: ["spring boot", "spring"],
                text: "Spring Boot ускоряет создание Java-приложений на Spring: поднимает встроенный web-сервер, подключает автоконфигурацию и снижает количество шаблонного кода. В backend-разработке он часто используется для REST API, сервисов, интеграций и работы с базами данных."
            },
            {
                markers: ["jpa", "hibernate", "orm"],
                text: "JPA и Hibernate связывают объектную модель Java с таблицами реляционной базы данных. Entity описывает сохраняемый объект, репозитории упрощают запросы, а ORM берёт на себя часть SQL-рутины. При этом важно понимать транзакции, lazy loading и проблему N+1."
            },
            {
                markers: ["html", "semantic"],
                text: "HTML задаёт структуру веб-страницы: заголовки, секции, формы, списки, ссылки и основное содержимое. Семантическая разметка делает интерфейс понятнее браузеру, поисковым системам и assistive-технологиям, поэтому HTML — это не просто набор тегов, а каркас смысла страницы."
            },
            {
                markers: ["css", "flexbox", "grid"],
                text: "CSS отвечает за визуальное представление интерфейса: цвета, отступы, сетки, адаптивность, типографику и состояния элементов. Flexbox удобен для выравнивания в одном направлении, Grid — для двумерных макетов, а хорошая CSS-структура делает интерфейс предсказуемым и поддерживаемым."
            },
            {
                markers: ["javascript", "typescript", "node.js", "nodejs"],
                text: "JavaScript используется для интерактивности в браузере и серверной логики в Node.js. Важные темы — функции, объекты, асинхронность, промисы, работа с DOM или API. TypeScript добавляет типы, чтобы раньше находить ошибки и проще поддерживать крупный код."
            },
            {
                markers: ["react", "vue", "angular"],
                text: "Современные frontend-фреймворки помогают собирать интерфейс из компонентов. Компонент объединяет разметку, состояние и поведение отдельной части страницы, а управление состоянием определяет, как данные превращаются в UI. Это основа сложных web-приложений."
            },
            {
                markers: ["linux", "bash", "shell", "command line", "cli"],
                text: "Linux и shell дают разработчику прямой доступ к файлам, процессам, сети и инструментам сборки. Командная строка полезна для автоматизации, диагностики, работы с серверами и CI/CD. Важно понимать пути, права доступа, переменные окружения и конвейеры команд."
            },
            {
                markers: ["security", "jwt", "oauth", "auth"],
                text: "Темы безопасности отвечают за то, кто пользователь, что ему разрешено и как защитить данные. Authentication подтверждает личность, authorization ограничивает действия, а токены вроде JWT помогают передавать проверяемую информацию между клиентом и сервером."
            },
            {
                markers: ["test", "testing", "qa"],
                text: "Тестирование помогает проверять поведение системы до того, как ошибка попадёт к пользователю. Unit-тесты закрывают маленькие части логики, интеграционные тесты проверяют связку компонентов, а регрессионные проверки защищают уже работающий функционал от случайных поломок."
            },
            {
                markers: ["algorithm", "алгоритм", "data structure", "структур"],
                text: "Алгоритмы и структуры данных описывают, как эффективно хранить и обрабатывать информацию. Важно понимать сложность по времени и памяти, потому что одно и то же решение может работать мгновенно на малых данных и разваливаться на больших."
            },
            {
                markers: ["machine learning", "ml", "llm", "ai", "нейрос", "embedding", "эмбеддинг"],
                text: "AI и ML связаны с построением моделей, которые находят закономерности в данных и используют их для предсказаний, классификации, генерации или поиска. Ключевые идеи — данные, признаки, обучение, проверка качества и ограничения модели на новых примерах."
            }
        ];

        const extendedTemplates = [
            {
                markers: ["variable", "scope", "переменн", "област видим"],
                text: "Переменные хранят промежуточные значения, с которыми работает программа, а область видимости определяет, где эти значения доступны. Эта тема важна для чтения чужого кода, поиска ошибок и понимания, почему одно и то же имя может вести себя по-разному в разных частях программы."
            },
            {
                markers: ["data type", "тип данн", "primitive"],
                text: "Типы данных задают, какие значения можно хранить и какие операции с ними допустимы. Числа, строки, булевы значения, коллекции и пользовательские типы помогают программе явно выражать намерения и предотвращают часть ошибок ещё до запуска."
            },
            {
                markers: ["function", "method", "функц", "метод"],
                text: "Функции и методы позволяют выделять повторяемую логику в отдельные именованные блоки. Хорошая функция делает одну понятную вещь, принимает ограниченный набор входных данных и возвращает результат, который легко проверить и переиспользовать."
            },
            {
                markers: ["syntax", "basic", "основ", "синтакс"],
                text: "Базовый синтаксис — это грамматика языка: как объявлять значения, вызывать функции, писать условия, циклы и выражения. Когда эти правила становятся привычными, внимание можно переносить с формы записи на саму логику решения задачи."
            },
            {
                markers: ["collection", "list", "map", "set", "queue", "коллекц", "спис", "словар"],
                text: "Коллекции помогают хранить группы значений: списки сохраняют порядок, множества убирают дубли, map-структуры связывают ключи со значениями. Выбор подходящей коллекции влияет на читаемость кода, скорость поиска и удобство дальнейшей обработки данных."
            },
            {
                markers: ["generic", "дженер", "типизац"],
                text: "Generics позволяют описывать код, который работает с разными типами, но сохраняет проверку типов. Это особенно полезно для коллекций, обобщённых сервисов и API, где важно избежать небезопасных приведений и случайных ошибок во время выполнения."
            },
            {
                markers: ["annotation", "аннотац", "decorator"],
                text: "Аннотации и декораторы добавляют к коду метаданные или дополнительное поведение без ручного дублирования логики. Во фреймворках они часто используются для конфигурации маршрутов, зависимостей, валидации, транзакций и тестов."
            },
            {
                markers: ["package", "module", "namespace", "пакет", "модул"],
                text: "Пакеты и модули помогают делить проект на понятные части и управлять зависимостями между ними. Такая структура снижает хаос в кодовой базе, упрощает навигацию и делает границы ответственности между компонентами заметнее."
            },
            {
                markers: ["maven", "gradle", "build tool", "сборк"],
                text: "Инструменты сборки описывают, как проект компилируется, тестируется, упаковывается и какие зависимости ему нужны. Maven, Gradle и похожие системы превращают локальный набор файлов в воспроизводимый артефакт, который можно запускать и доставлять в окружения."
            },
            {
                markers: ["regex", "regular expression", "регуляр"],
                text: "Регулярные выражения используются для поиска, проверки и извлечения фрагментов текста по шаблону. Они полезны в валидации форм, парсинге логов, обработке строк и быстрых преобразованиях, но требуют аккуратности из-за плотного синтаксиса."
            },
            {
                markers: ["json", "xml", "yaml"],
                text: "JSON, XML и YAML — распространённые форматы обмена и хранения структурированных данных. Они встречаются в API, конфигурации, инфраструктуре и интеграциях, поэтому важно понимать их синтаксис, ограничения и типичные ошибки сериализации."
            },
            {
                markers: ["redis", "cache", "кэш"],
                text: "Кэширование ускоряет систему за счёт хранения часто используемых данных ближе к приложению. Redis и похожие инструменты помогают снижать нагрузку на базу, хранить временные значения и строить быстрые сценарии, но требуют контроля актуальности данных."
            },
            {
                markers: ["mongodb", "nosql"],
                text: "NoSQL-базы вроде MongoDB хранят данные не в классических таблицах, а в более гибких структурах, например документах. Это удобно для меняющихся моделей и быстрых итераций, но требует понимания индексов, схемы доступа и компромиссов консистентности."
            },
            {
                markers: ["message queue", "kafka", "rabbitmq", "очеред"],
                text: "Очереди сообщений разделяют сервисы по времени: один компонент публикует событие, другой обрабатывает его позже. Kafka, RabbitMQ и похожие инструменты помогают строить асинхронные процессы, разгружать пики нагрузки и делать интеграции устойчивее."
            },
            {
                markers: ["ci/cd", "cicd", "github actions", "pipeline"],
                text: "CI/CD автоматизирует проверку, сборку и доставку изменений. Хороший pipeline быстро запускает тесты, собирает артефакт, показывает ошибку рядом с изменением и снижает риск ручных действий при выпуске новой версии."
            },
            {
                markers: ["observability", "logging", "monitoring", "метрик", "лог"],
                text: "Наблюдаемость помогает понять, что происходит с системой после запуска: логи объясняют события, метрики показывают динамику, трассировки связывают запросы между сервисами. Без этого сложно расследовать инциденты и находить узкие места."
            },
            {
                markers: ["microservice", "микросервис"],
                text: "Микросервисы делят систему на независимые сервисы с собственными границами ответственности. Такой подход помогает масштабировать команды и части продукта отдельно, но добавляет сетевые ошибки, согласованность данных, наблюдаемость и сложность деплоя."
            },
            {
                markers: ["terraform", "iac", "infrastructure as code"],
                text: "Infrastructure as Code описывает инфраструктуру в виде версионируемого кода. Terraform и похожие инструменты позволяют воспроизводимо создавать окружения, отслеживать изменения и уменьшать ручные настройки серверов, сетей и облачных ресурсов."
            },
            {
                markers: ["aws", "azure", "gcp", "cloud", "облак"],
                text: "Облачные платформы дают готовые вычисления, хранение, сети, базы данных и управляемые сервисы. Здесь важно понимать не только кнопки в консоли, но и стоимость, безопасность, отказоустойчивость и то, как приложение ведёт себя под нагрузкой."
            },
            {
                markers: ["network", "tcp", "dns", "tls", "сет"],
                text: "Сетевые основы объясняют, как запрос проходит от клиента до сервера: DNS находит адрес, TCP устанавливает соединение, TLS защищает обмен, а протоколы уровня приложения передают данные. Это помогает диагностировать ошибки API, деплоя и интеграций."
            },
            {
                markers: ["android", "kotlin"],
                text: "Android-разработка объединяет язык Kotlin или Java, жизненный цикл экранов, работу с UI, сетью, хранилищем и ограничениями мобильной платформы. Важно учитывать состояние приложения, разрешения, производительность и поведение на разных устройствах."
            },
            {
                markers: ["ios", "swift", "swiftui"],
                text: "iOS-разработка строится вокруг Swift, интерфейсов, жизненного цикла приложения и интеграции с экосистемой Apple. SwiftUI описывает интерфейс декларативно, поэтому разработчик фокусируется на состоянии экрана и реакции UI на изменение данных."
            },
            {
                markers: ["flutter", "react native"],
                text: "Кроссплатформенная мобильная разработка позволяет писать значительную часть логики один раз и выпускать приложение под iOS и Android. Flutter и React Native ускоряют старт, но всё равно требуют знания платформенных ограничений, производительности и нативных интеграций."
            },
            {
                markers: ["unity", "game", "игр", "c#"],
                text: "Game development соединяет программирование, математику, работу с движком, сценами, физикой, анимацией и игровыми циклами. В Unity ключевыми становятся C#, компоненты, события, управление состоянием объектов и оптимизация под реальные устройства."
            },
            {
                markers: ["prompt", "ai agent", "agent"],
                text: "Prompt engineering и AI agents помогают управлять поведением языковых моделей через контекст, инструкции, инструменты и ограничения. Важно уметь формулировать задачу, проверять ответ, задавать формат результата и проектировать безопасные сценарии использования."
            },
            {
                markers: ["etl", "warehouse", "data engineer", "pipeline"],
                text: "Data engineering занимается движением данных: сбором, очисткой, преобразованием, хранением и доставкой в аналитические или продуктовые системы. Надёжный pipeline учитывает качество данных, расписание, повторные запуски и понятную диагностику ошибок."
            },
            {
                markers: ["product manager", "product", "manager"],
                text: "Product management связывает потребности пользователей, бизнес-цели и работу команды разработки. Важно формулировать проблему, проверять гипотезы, расставлять приоритеты и измерять результат не количеством задач, а изменением пользовательского поведения."
            },
            {
                markers: ["ux", "research", "user journey", "design system"],
                text: "UX и дизайн-системы помогают делать интерфейсы понятными, последовательными и воспроизводимыми. Здесь важны пользовательские сценарии, доступность, единые компоненты, визуальная иерархия и проверка решений на реальных задачах пользователя."
            },
            {
                markers: ["code review", "refactor", "clean code"],
                text: "Code review и рефакторинг улучшают качество проекта без изменения внешнего поведения. Хорошее ревью ищет риски, упрощает решение и повышает поддерживаемость, а рефакторинг делает код яснее, тестируемее и менее хрупким."
            },
            {
                markers: ["documentation", "technical writing", "writer"],
                text: "Техническая документация превращает сложную систему в понятные инструкции, справочники и объяснения. Хороший текст отвечает на реальные вопросы пользователя, показывает контекст, даёт примеры и снижает зависимость команды от устных пояснений."
            },
            {
                markers: ["agile", "scrum", "kanban"],
                text: "Agile-подходы помогают команде короткими итерациями доставлять ценность и регулярно сверяться с реальностью. Scrum, Kanban и похожие практики полезны не сами по себе, а когда делают поток работы прозрачнее и уменьшают время от идеи до проверки."
            }
        ];

        return [...templates, ...extendedTemplates]
            .find((template) => template.markers.some((marker) => fingerprint.includes(marker)))?.text || null;
    }

    function cleanupGeneratedDescription(description, title) {
        const cleaned = description
            .replace(/^(подтема|тема)\s+направления\s+«?.+?»?:\s*/i, "")
            .replace(/\s*к теме прикреплены материалы для самостоятельного изучения\.?/i, "")
            .trim();
        if (!cleaned || cleaned.length < 24 || cleaned.toLowerCase() === title.toLowerCase()) {
            return "";
        }
        return cleaned;
    }

    function fallbackTopicOverview(title, fingerprint) {
        if (fingerprint.includes("design") || fingerprint.includes("ux")) {
            return `Тема «${title}» помогает принимать продуктовые и визуальные решения не наугад, а через структуру, сценарии пользователя и ограничения интерфейса. Здесь важно смотреть не только на внешний вид, но и на понятность, доступность и повторяемость решений.`;
        }
        if (fingerprint.includes("architecture") || fingerprint.includes("system")) {
            return `Тема «${title}» относится к проектированию систем: как разделять ответственность, выбирать границы компонентов и заранее учитывать нагрузку, развитие и сопровождение. Такие знания помогают видеть не только отдельный код, но и поведение всей системы.`;
        }
        if (fingerprint.includes("deploy") || fingerprint.includes("cloud") || fingerprint.includes("aws")) {
            return `Тема «${title}» связана с запуском и поддержкой приложений вне локальной машины. Здесь важны окружения, конфигурация, наблюдаемость, отказоустойчивость и понимание того, как код превращается в работающий сервис для пользователя.`;
        }
        return `Тема «${title}» раскрывает отдельный практический блок внутри выбранного направления. В ней важно понять назначение, базовые понятия, типовые ошибки и ситуации, где этот подход применяется в реальной разработке.`;
    }

    function buildOverviewContext(isKnown, prereqs, unlocks) {
        const prereqPart = prereqs.length
            ? `Перед изучением полезно уверенно понимать: ${prereqs.slice(0, 2).map((item) => item.topicTitle).join(", ")}.`
            : "Тему можно начинать без отдельного обязательного входного блока.";
        const nextPart = unlocks.length
            ? `Дальше она помогает перейти к: ${unlocks.slice(0, 2).map((item) => item.topicTitle).join(", ")}.`
            : "В текущей карте это самостоятельный или завершающий блок.";
        const knownPart = isKnown
            ? "Она отмечена как уже знакомая, поэтому остаётся на карте для контекста, но не перегружает личный план."
            : "";
        return `${prereqPart} ${nextPart} ${knownPart}`.trim();
    }

    function normalizeOverviewFingerprint(value) {
        return value
            .toLowerCase()
            .replaceAll("_", " ")
            .replaceAll("-", " ");
    }

    function renderTutorTab(topic) {
        const messages = getTutorMessages(topic.topicId);
        const isLoading = state.tutorLoadingTopicIds.has(topic.topicId);
        const visibleMessages = messages.length
            ? messages
            : [{
                role: "assistant",
                content: "Задайте вопрос по теме. Я отвечу в контексте этой roadmap и смогу продолжить диалог следующими сообщениями."
            }];

        return `
            <div class="tutor-box">
                <div class="topic-detail-block tutor-chat-header">
                    <h4>AI Tutor по теме</h4>
                    ${renderTutorPromptMenu(topic, isLoading)}
                </div>
                <div class="tutor-chat" data-tutor-chat>
                    ${visibleMessages.map(renderTutorMessage).join("")}
                    ${isLoading ? renderTutorMessage({ role: "assistant", content: "Думаю над ответом...", loading: true }) : ""}
                </div>
                <form class="tutor-chat-form" data-tutor-form>
                    <label class="form-row">
                        <span class="field-label">Сообщение</span>
                        <textarea class="textarea tutor-chat-input" name="question" rows="1" maxlength="1200" data-tutor-input placeholder="Например: объясни проще и покажи небольшой пример"></textarea>
                    </label>
                    <div class="form-actions tutor-chat-actions">
                        <button class="button button-primary" type="submit" ${isLoading ? "disabled" : ""}>Отправить</button>
                        ${messages.length ? `<button class="button button-ghost" type="button" data-tutor-clear>Очистить чат</button>` : ""}
                    </div>
                </form>
            </div>
        `;
    }

    function renderTutorPromptMenu(topic, isLoading) {
        const prompts = buildTutorQuickPrompts(topic);

        return `
            <details class="tutor-prompt-menu">
                <summary class="tutor-prompt-trigger">Объяснить</summary>
                <div class="tutor-prompt-list">
                    ${prompts.map((item) => `
                        <button class="tutor-prompt-option" type="button" data-tutor-prompt="${escapeHtml(item.prompt)}" ${isLoading ? "disabled" : ""}>
                            ${escapeHtml(item.label)}
                        </button>
                    `).join("")}
                </div>
            </details>
        `;
    }

    function buildTutorQuickPrompts(topic) {
        const title = topic.topicTitle || "выбранная тема";

        return [
            {
                label: "Объяснить тему",
                prompt: `Объясни тему «${title}» простыми словами. Добавь короткий пример и покажи, как эта тема связана с roadmap.`
            },
            {
                label: "Ключевые моменты",
                prompt: `Перечисли ключевые моменты темы «${title}»: что нужно понять, запомнить и уметь применить на практике.`
            },
            {
                label: "Краткое резюме",
                prompt: `Кратко резюмируй тему «${title}» в 5-7 пунктах без лишней теории.`
            },
            {
                label: "Объяснить как новичку",
                prompt: `Объясни тему «${title}» максимально простым языком, как для новичка, который впервые сталкивается с этим понятием.`
            },
            {
                label: "Почему это важно?",
                prompt: `Почему тема «${title}» важна в этом направлении? Объясни, где она применяется и какие проблемы помогает решать.`
            }
        ];
    }

    function renderTutorMessage(message) {
        const roleLabel = message.role === "user" ? "Вы" : "AI Tutor";
        const roleClass = message.role === "user" ? "is-user" : "is-assistant";
        return `
            <article class="tutor-message ${roleClass} ${message.error ? "is-error" : ""} ${message.loading ? "is-loading" : ""}">
                <span class="tutor-message-role">${roleLabel}</span>
                <p>${escapeHtml(message.content)}</p>
            </article>
        `;
    }

    function renderQuizTab(topic) {
        const quiz = state.quizByTopicId.get(topic.topicId);
        const quizError = state.quizErrorByTopicId.get(topic.topicId);

        if (quizError) {
            return `
                <div class="topic-detail-block">
                    <h4>Квиз по теме</h4>
                    <p>${escapeHtml(quizError)}</p>
                    <button class="button button-primary" type="button" data-quiz-retry>Попробовать ещё раз</button>
                </div>
            `;
        }

        if (!quiz) {
            return `
                <div class="topic-detail-block">
                    <h4>Квиз по теме</h4>
                    <p>Загружаем короткую проверку понимания. Если для темы ещё нет ручного квиза, сервис создаст базовый self-check автоматически.</p>
                    <button class="button button-primary" type="button" disabled>Загрузка...</button>
                </div>
            `;
        }

        const questions = quiz.questions || [];
        const result = state.quizResultByQuizId.get(quiz.id);

        if (!questions.length) {
            return `
                <div class="topic-detail-block">
                    <h4>${escapeHtml(quiz.title || "Квиз")}</h4>
                    <p>Для этой темы пока нет вопросов.</p>
                </div>
            `;
        }

        return `
            <form class="quiz-box" data-quiz-form data-quiz-id="${quiz.id}">
                <div class="topic-detail-block">
                    <h4>${escapeHtml(quiz.title || "Квиз по теме")}</h4>
                    <p>Ответьте на вопросы и проверьте, насколько уверенно вы поняли тему.</p>
                </div>
                ${questions.map((question, questionIndex) => {
                    const isMultiple = question.type === "MULTIPLE";
                    return `
                        <fieldset class="quiz-question">
                            <legend class="visually-hidden">Вопрос ${questionIndex + 1}</legend>
                            <strong class="quiz-question-title">${questionIndex + 1}. ${escapeHtml(question.text)}</strong>
                            ${(question.options || []).map((option) => `
                                <label class="quiz-option">
                                    <input type="${isMultiple ? "checkbox" : "radio"}" name="question-${question.id}" value="${option.id}">
                                    <span>${escapeHtml(option.text)}</span>
                                </label>
                            `).join("")}
                        </fieldset>
                    `;
                }).join("")}
                <div class="form-actions">
                    <button class="button button-primary" type="submit">Проверить ответы</button>
                </div>
                ${result ? `
                    <div class="quiz-result">
                        <strong>${result.correctCount} из ${result.totalCount}</strong>
                        <span>Результат: ${escapeHtml(String(result.score))}%</span>
                    </div>
                ` : ""}
            </form>
        `;
    }

    async function ensureQuizLoaded(topic) {
        if (state.quizByTopicId.has(topic.topicId)) {
            return;
        }
        if (state.quizLoadingTopicIds.has(topic.topicId)) {
            return;
        }

        try {
            state.quizLoadingTopicIds.add(topic.topicId);
            state.quizErrorByTopicId.delete(topic.topicId);
            const quiz = await quizzesApi.getTopicQuiz(topic.topicId);
            state.quizByTopicId.set(topic.topicId, quiz);
            if (state.selectedTopicId === topic.topicId && state.topicTab === "quiz") {
                renderTopicPanel(topic);
                openTopicDrawer();
            }
        } catch (error) {
            state.quizErrorByTopicId.set(
                topic.topicId,
                error instanceof ApiError ? error.message : "Не удалось загрузить квиз."
            );
            if (state.selectedTopicId === topic.topicId && state.topicTab === "quiz") {
                renderTopicPanel(topic);
                openTopicDrawer();
            }
        } finally {
            state.quizLoadingTopicIds.delete(topic.topicId);
        }
    }

    function bindTutorForm(topic) {
        const form = topicPanel.querySelector("[data-tutor-form]");
        if (!form) {
            return;
        }

        const clearButton = topicPanel.querySelector("[data-tutor-clear]");
        clearButton?.addEventListener("click", () => {
            state.tutorChatByTopicId.delete(topic.topicId);
            state.tutorLoadingTopicIds.delete(topic.topicId);
            renderTopicPanel(topic);
            openTopicDrawer();
        });

        bindTutorInputAutosize(form);
        bindTutorPromptMenu(topic);

        form.addEventListener("submit", async (event) => {
            event.preventDefault();
            if (state.tutorLoadingTopicIds.has(topic.topicId)) {
                return;
            }

            const question = new FormData(form).get("question")?.toString().trim();
            if (!question) {
                appendTutorMessage(topic.topicId, {
                    role: "assistant",
                    content: "Введите вопрос по теме.",
                    error: true
                });
                renderTopicPanel(topic);
                return;
            }

            await sendTutorQuestion(topic, question);
        });
    }

    function bindTutorPromptMenu(topic) {
        topicPanel.querySelectorAll("[data-tutor-prompt]").forEach((button) => {
            button.addEventListener("click", async () => {
                const prompt = button.dataset.tutorPrompt?.trim();
                button.closest("details")?.removeAttribute("open");
                await sendTutorQuestion(topic, prompt);
            });
        });
    }

    async function sendTutorQuestion(topic, question) {
        const normalizedQuestion = question?.trim();
        if (!normalizedQuestion || state.tutorLoadingTopicIds.has(topic.topicId)) {
            return;
        }

        const history = toTutorHistory(getTutorMessages(topic.topicId));
        appendTutorMessage(topic.topicId, {
            role: "user",
            content: normalizedQuestion
        });
        state.tutorLoadingTopicIds.add(topic.topicId);
        renderTopicPanel(topic);
        openTopicDrawer();
        scrollTutorChatToBottom();

        try {
            const response = await tutorApi.ask(state.roadmap.id, topic.topicId, {
                question: normalizedQuestion,
                history
            });
            appendTutorMessage(topic.topicId, {
                role: "assistant",
                content: response.answer || "Ответ пустой. Попробуйте переформулировать вопрос."
            });
        } catch (error) {
            appendTutorMessage(topic.topicId, {
                role: "assistant",
                content: error instanceof ApiError ? error.message : "AI Tutor сейчас недоступен.",
                error: true,
            });
        } finally {
            state.tutorLoadingTopicIds.delete(topic.topicId);
            if (state.selectedTopicId === topic.topicId && state.topicTab === "tutor") {
                renderTopicPanel(topic);
                openTopicDrawer();
                scrollTutorChatToBottom();
            }
        }
    }

    function bindTutorInputAutosize(form) {
        const input = form.querySelector("[data-tutor-input]");
        if (!input) {
            return;
        }

        const resize = () => {
            input.style.height = "auto";
            input.style.height = `${Math.min(input.scrollHeight, 132)}px`;
        };

        input.addEventListener("input", resize);
        input.addEventListener("keydown", (event) => {
            if (event.key === "Enter" && !event.shiftKey && !event.isComposing) {
                event.preventDefault();
                form.requestSubmit();
            }
        });
        resize();
    }

    function getTutorMessages(topicId) {
        return state.tutorChatByTopicId.get(topicId) || [];
    }

    function appendTutorMessage(topicId, message) {
        const messages = [...getTutorMessages(topicId), message].slice(-24);
        state.tutorChatByTopicId.set(topicId, messages);
    }

    function toTutorHistory(messages) {
        return messages
            .filter((message) => !message.error && !message.loading)
            .map((message) => ({
                role: message.role === "user" ? "user" : "assistant",
                content: message.content
            }))
            .slice(-12);
    }

    function scrollTutorChatToBottom() {
        requestAnimationFrame(() => {
            const chat = topicPanel.querySelector("[data-tutor-chat]");
            if (chat) {
                chat.scrollTop = chat.scrollHeight;
            }
        });
    }

    function bindQuizForm(topic) {
        const form = topicPanel.querySelector("[data-quiz-form]");
        if (!form) {
            return;
        }

        form.addEventListener("submit", async (event) => {
            event.preventDefault();
            const selectedOptionIds = [...form.querySelectorAll("input:checked")]
                .map((input) => Number(input.value))
                .filter(Boolean);

            if (!selectedOptionIds.length) {
                showStatus(statusBox, "warning", "Выберите хотя бы один вариант ответа.");
                return;
            }

            const quizId = Number(form.dataset.quizId);
            const button = form.querySelector("button[type='submit']");
            button.disabled = true;
            button.textContent = "Проверяем...";

            try {
                const result = await quizzesApi.submit({
                    quizId,
                    selectedOptionIds
                });
                state.quizResultByQuizId.set(quizId, result);
            } catch (error) {
                showStatus(statusBox, "error", error instanceof ApiError ? error.message : "Не удалось отправить квиз.");
            } finally {
                renderTopicPanel(topic);
                openTopicDrawer();
            }
        });
    }

    function bindQuizRetry(topic) {
        const button = topicPanel.querySelector("[data-quiz-retry]");
        if (!button) {
            return;
        }

        button.addEventListener("click", () => {
            state.quizErrorByTopicId.delete(topic.topicId);
            renderTopicPanel(topic);
            openTopicDrawer();
            ensureQuizLoaded(topic);
        });
    }

    function syncRoadmapUrl() {
        if (!state.roadmap) {
            return;
        }

        const query = new URLSearchParams({ roadmapId: String(state.roadmap.id) });
        if (state.selectedTopicId != null) {
            query.set("topicId", String(state.selectedTopicId));
        }
        if (state.activePlanId != null) {
            query.set("planId", String(state.activePlanId));
        }

        window.history.replaceState({}, "", `/roadmap?${query.toString()}`);
    }

    function handleError(error) {
        if (error?.status === 401) {
            window.location.replace("/login");
            return;
        }

        const message = error instanceof ApiError
            ? error.message
            : "Не удалось загрузить roadmap.";
        showStatus(statusBox, "error", message);
    }
}
