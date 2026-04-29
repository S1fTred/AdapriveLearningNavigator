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
        quizResultByQuizId: new Map(),
        tutorAnswerByTopicId: new Map()
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
            <div class="card panel-card topic-detail-card">
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

    function renderTutorTab(topic) {
        const savedAnswer = state.tutorAnswerByTopicId.get(topic.topicId);

        return `
            <div class="tutor-box">
                <div class="topic-detail-block">
                    <h4>AI Tutor по теме</h4>
                    <p>Задайте вопрос именно по этой теме. Ассистент ответит в контексте выбранной roadmap, зависимостей и места темы в маршруте.</p>
                </div>
                <form class="form-grid" data-tutor-form>
                    <label class="form-row">
                        <span class="field-label">Ваш вопрос</span>
                        <textarea class="textarea" name="question" rows="5" maxlength="1200" placeholder="Например: объясни простыми словами и покажи небольшой пример"></textarea>
                    </label>
                    <div class="form-actions">
                        <button class="button button-primary" type="submit">Спросить AI Tutor</button>
                    </div>
                </form>
                ${savedAnswer ? `
                    <div class="tutor-answer ${savedAnswer.error ? "is-error" : ""}">
                        <h4>${savedAnswer.error ? "Не удалось получить ответ" : "Ответ AI Tutor"}</h4>
                        <p>${escapeHtml(savedAnswer.answer)}</p>
                    </div>
                ` : ""}
            </div>
        `;
    }

    function renderQuizTab(topic) {
        const quiz = state.quizByTopicId.get(topic.topicId);

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
                            <legend>${questionIndex + 1}. ${escapeHtml(question.text)}</legend>
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

        try {
            const quiz = await quizzesApi.getTopicQuiz(topic.topicId);
            state.quizByTopicId.set(topic.topicId, quiz);
            if (state.selectedTopicId === topic.topicId && state.topicTab === "quiz") {
                renderTopicPanel(topic);
                openTopicDrawer();
            }
        } catch (error) {
            showStatus(statusBox, "error", error instanceof ApiError ? error.message : "Не удалось загрузить квиз.");
        }
    }

    function bindTutorForm(topic) {
        const form = topicPanel.querySelector("[data-tutor-form]");
        if (!form) {
            return;
        }

        form.addEventListener("submit", async (event) => {
            event.preventDefault();
            const question = new FormData(form).get("question")?.toString().trim();
            if (!question) {
                state.tutorAnswerByTopicId.set(topic.topicId, {
                    error: true,
                    answer: "Введите вопрос по теме."
                });
                renderTopicPanel(topic);
                return;
            }

            const button = form.querySelector("button[type='submit']");
            button.disabled = true;
            button.textContent = "AI Tutor думает...";

            try {
                const response = await tutorApi.ask(state.roadmap.id, topic.topicId, { question });
                state.tutorAnswerByTopicId.set(topic.topicId, {
                    error: false,
                    answer: response.answer || "Ответ пустой. Попробуйте переформулировать вопрос."
                });
            } catch (error) {
                state.tutorAnswerByTopicId.set(topic.topicId, {
                    error: true,
                    answer: error instanceof ApiError ? error.message : "AI Tutor сейчас недоступен."
                });
            } finally {
                renderTopicPanel(topic);
                openTopicDrawer();
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
