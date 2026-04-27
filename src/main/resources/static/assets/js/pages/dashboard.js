import { ApiError, plansApi, roadmapsApi } from "/assets/js/core/api.js";
import { requireAuth } from "/assets/js/core/guard.js";
import { summarizePlan } from "/assets/js/core/plans.js";
import { resolveActiveRoadmap } from "/assets/js/core/roadmaps.js";
import { initPrivateShell } from "/assets/js/core/shell.js";
import {
    getCachedPlan,
    getPlanDraft,
    savePlanDraft,
    setSelectedPlanId,
    setSelectedRoadmapId
} from "/assets/js/core/session.js";
import {
    annotatePlanForDisplay,
    clearStatus,
    escapeHtml,
    flattenPlanSteps,
    formatDate,
    formatHours,
    formatPlanStepContinuation,
    formatPlanStepTitle,
    renderEmptyState,
    showStatus
} from "/assets/js/core/ui.js";

const CATALOG_CATEGORIES = [
    {
        id: "ROLE_BASED",
        title: "Направления на основе ролей",
        subtitle: "Профессии и карьерные треки"
    },
    {
        id: "SKILL_BASED",
        title: "Направления на основе навыков",
        subtitle: "Технологии, языки и отдельные навыки"
    }
];

if (requireAuth()) {
    initPrivateShell("dashboard");

    const statusBox = document.querySelector("#dashboard-status");
    const roadmapsContainer = document.querySelector("#roadmaps-list");
    const roadmapSearch = document.querySelector("#roadmap-search");
    const roadmapSummary = document.querySelector("#selected-roadmap-summary");
    const selectedRoadmapIdField = document.querySelector("#selected-roadmap-id");
    const selectedRoadmapLink = document.querySelector("#selected-roadmap-link");
    const knownTopicsPicker = document.querySelector("#known-topics-picker");
    const buildForm = document.querySelector("#build-roadmap-plan-form");
    const submitButton = document.querySelector("#build-plan-submit");
    const planBuilderPanel = document.querySelector("[data-dashboard-plan-panel]");
    const roadmapMetaContainer = document.querySelector("#roadmap-meta");
    const flowContainer = document.querySelector("#roadmap-flow");
    const topicPanel = document.querySelector("#roadmap-topic-panel");
    const spotlightContainer = document.querySelector("#plan-spotlight");
    const hoursPerWeekField = document.querySelector("#hoursPerWeek");

    const state = {
        roadmaps: [],
        planItems: [],
        selectedRoadmap: null,
        selectedTopicId: null,
        selectedTopic: null,
        searchQuery: "",
        activeCatalogCategory: "ROLE_BASED",
        activePlanId: null,
        plannedTopicIds: new Set(),
        knownTopicIds: new Set(),
        topicTab: "overview"
    };

    prefillDraft();
    loadDashboard().catch((error) => handleError(error, statusBox));

    roadmapSearch?.addEventListener("input", (event) => {
        state.searchQuery = String(event.target.value || "").trim().toLowerCase();
        renderRoadmapCatalog();
    });

    buildForm?.addEventListener("submit", async (event) => {
        event.preventDefault();
        clearStatus(statusBox);

        if (!state.selectedRoadmap) {
            showStatus(statusBox, "error", "Сначала выберите roadmap.");
            return;
        }

        const payload = {
            roleId: state.selectedRoadmap.id,
            hoursPerWeek: Number(hoursPerWeekField?.value || 10),
            knownTopicIds: readKnownTopicIds(),
            scenarioLabel: null
        };

        savePlanDraft({
            roleId: payload.roleId,
            hoursPerWeek: payload.hoursPerWeek,
            knownTopicIds: payload.knownTopicIds
        });

        submitButton.disabled = true;
        submitButton.textContent = "Собираем план...";
        showStatus(statusBox, "info", "Строим недельный план на основе выбранного roadmap.");

        try {
            const plan = await plansApi.buildFromRoadmap(payload);
            setSelectedPlanId(plan.id);
            mergePlanIntoIndex(plan);
            await hydratePlanContext(state.selectedRoadmap.id, plan.id);
            renderRoadmapMeta();
            renderRoadmapFlow();
            renderSpotlight(plan);
            if (state.selectedTopicId != null) {
                await selectTopic(state.selectedTopicId, { syncUrl: false });
            }
            syncDashboardUrl();
            showStatus(statusBox, "success", "План собран. Ниже можно сразу проверить roadmap, а детали открыть в разделах плана и прогресса.");
        } catch (error) {
            handleError(error, statusBox);
        } finally {
            submitButton.disabled = false;
            submitButton.textContent = "Построить недельный план";
        }
    });

    async function loadDashboard() {
        const query = new URLSearchParams(window.location.search);
        const explicitRoadmapId = Number(query.get("roadmapId")) || null;
        const explicitTopicId = Number(query.get("topicId")) || null;
        const explicitPlanId = Number(query.get("planId")) || null;

        const [roadmapsPage, plansPage] = await Promise.all([
            roadmapsApi.list(0, 120),
            plansApi.list(0, 24)
        ]);

        state.roadmaps = roadmapsPage.items || [];
        state.planItems = plansPage.items || [];

        if (!state.roadmaps.length) {
            renderEmptyState(
                roadmapsContainer,
                "Каталог пока пуст",
                "В базе знаний пока нет опубликованных roadmap."
            );
            renderEmptyState(
                roadmapSummary,
                "Roadmap не выбран",
                "Когда в базе появятся направления, здесь можно будет собрать недельный план."
            );
            renderEmptyState(
                roadmapMetaContainer,
                "Направление не выбрано",
                "После выбора roadmap здесь появится краткая сводка по структуре направления."
            );
            renderEmptyState(
                flowContainer,
                "Roadmap пока недоступен",
                "Здесь появится карта тем выбранного направления."
            );
            renderEmptyState(
                topicPanel,
                "Тема не выбрана",
                "Кликните по теме в roadmap, чтобы открыть её детали."
            );
            renderEmptyState(
                spotlightContainer,
                "Плана по направлению пока нет",
                "После первого построения weekly plan здесь появится краткая сводка."
            );
            return;
        }

        const activeRoadmap = await resolveActiveRoadmap(explicitRoadmapId || getPlanDraft().roleId || null);
        renderRoadmapCatalog();

        if (!activeRoadmap) {
            return;
        }

        await selectRoadmap(activeRoadmap.id, {
            explicitPlanId,
            explicitTopicId,
            syncUrl: false,
            focusPlanBuilder: false
        });
    }

    async function selectRoadmap(roadmapId, options = {}) {
        const roadmap = await roadmapsApi.get(roadmapId);
        const draft = getPlanDraft();

        state.selectedRoadmap = roadmap;
        state.selectedTopicId = null;
        state.selectedTopic = null;
        state.topicTab = "overview";
        state.activeCatalogCategory = roadmap.category || state.activeCatalogCategory;
        state.knownTopicIds = new Set(
            (draft.roleId === roadmapId ? draft.knownTopicIds : [])
                .filter((topicId) => roadmap.topics.some((topic) => topic.topicId === topicId))
        );

        setSelectedRoadmapId(roadmapId);
        selectedRoadmapIdField.value = String(roadmapId);

        await hydratePlanContext(roadmapId, options.explicitPlanId || null);

        selectedRoadmapLink.href = `${buildDashboardUrl({ roadmapId, planId: state.activePlanId })}#catalog-roadmap`;

        renderSelectedRoadmap();
        renderRoadmapCatalog();
        renderKnownTopics();
        renderRoadmapMeta();
        renderRoadmapFlow();
        await renderSpotlightForSelectedRoadmap();

        const initialTopicId = resolveInitialTopicId(roadmap, options.explicitTopicId);
        if (initialTopicId != null) {
            await selectTopic(initialTopicId, { syncUrl: false });
        } else {
            renderEmptyState(
                topicPanel,
                "Тема не выбрана",
                "Выберите карточку в roadmap, чтобы открыть детали."
            );
        }

        if (options.syncUrl !== false) {
            syncDashboardUrl();
        }

        if (options.focusPlanBuilder !== false) {
            focusPlanBuilderPanel();
        }
    }

    function resolveInitialTopicId(roadmap, explicitTopicId) {
        if (explicitTopicId && roadmap.topics.some((topic) => topic.topicId === explicitTopicId)) {
            return explicitTopicId;
        }

        return roadmap.topics?.[0]?.topicId ?? null;
    }

    async function hydratePlanContext(roleId, explicitPlanId = null) {
        state.activePlanId = null;
        state.plannedTopicIds = new Set();

        let plan = null;
        if (explicitPlanId) {
            plan = await plansApi.get(explicitPlanId);
        } else {
            const latestPlanItem = findLatestPlanForRoadmap(roleId);
            if (latestPlanItem) {
                plan = getCachedPlan(latestPlanItem.id) || await plansApi.get(latestPlanItem.id);
            }
        }

        if (!plan || plan.roleId !== roleId) {
            return null;
        }

        state.activePlanId = plan.id;
        for (const week of plan.weeks || []) {
            for (const step of week.steps || []) {
                state.plannedTopicIds.add(step.topicId);
            }
        }

        return plan;
    }

    function renderRoadmapCatalog() {
        if (!state.roadmaps.length) {
            return;
        }

        const items = state.roadmaps.filter((roadmap) => {
            if (!state.searchQuery) {
                return true;
            }

            const haystack = `${roadmap.name} ${roadmap.code} ${roadmap.description || ""}`.toLowerCase();
            return haystack.includes(state.searchQuery);
        });

        if (!items.length) {
            renderEmptyState(
                roadmapsContainer,
                "Ничего не найдено",
                "Попробуйте другой запрос или очистите поиск."
            );
            return;
        }

        const groupedItems = groupRoadmapsByCategory(items);
        const activeItems = groupedItems[state.activeCatalogCategory] || [];

        roadmapsContainer.innerHTML = `
            <div class="roadmap-catalog-tabs" role="tablist" aria-label="Категории roadmap">
                ${CATALOG_CATEGORIES.map((category) => renderCatalogTab(
                    category,
                    groupedItems[category.id]?.length || 0
                )).join("")}
            </div>
            <div class="roadmap-catalog-tab-panel" role="tabpanel">
                ${activeItems.length ? `
                    <div class="roadmap-catalog">
                        ${activeItems.map(renderRoadmapCard).join("")}
                    </div>
                ` : renderCatalogEmptyState()}
            </div>
        `;

        roadmapsContainer.querySelectorAll("[data-roadmap-category]").forEach((button) => {
            button.addEventListener("click", () => {
                state.activeCatalogCategory = button.dataset.roadmapCategory;
                renderRoadmapCatalog();
            });
        });

        roadmapsContainer.querySelectorAll("[data-select-roadmap]").forEach((button) => {
            button.addEventListener("click", async () => {
                const roadmapId = Number(button.dataset.selectRoadmap);
                await selectRoadmap(roadmapId);
            });
        });
    }

    function groupRoadmapsByCategory(items) {
        return items.reduce((groups, roadmap) => {
            const category = roadmap.category === "ROLE_BASED" ? "ROLE_BASED" : "SKILL_BASED";
            groups[category].push(roadmap);
            return groups;
        }, {
            ROLE_BASED: [],
            SKILL_BASED: []
        });
    }

    function renderCatalogTab(category, count) {
        const isActive = state.activeCatalogCategory === category.id;
        return `
            <button
                class="roadmap-catalog-tab ${isActive ? "is-active" : ""}"
                type="button"
                role="tab"
                aria-selected="${isActive}"
                data-roadmap-category="${category.id}"
            >
                <span>
                    <strong>${escapeHtml(category.title)}</strong>
                    <small>${escapeHtml(category.subtitle)}</small>
                </span>
                <b>${count}</b>
            </button>
        `;
    }

    function renderCatalogEmptyState() {
        return `
            <div class="empty-state">
                <h3>В этой вкладке ничего не найдено</h3>
                <p>Попробуйте изменить поисковый запрос или переключить вкладку.</p>
            </div>
        `;
    }

    function renderRoadmapCard(roadmap) {
        const isActive = roadmap.id === state.selectedRoadmap?.id;
        return `
            <article class="card roadmap-catalog-card ${isActive ? "is-selected" : ""}" data-roadmap-card-id="${roadmap.id}">
                <div class="roadmap-card-header">
                    <p class="eyebrow">Roadmap</p>
                    <h4>${escapeHtml(roadmap.name)}</h4>
                    <p class="roadmap-card-description">${escapeHtml(roadmap.description || "Roadmap без описания.")}</p>
                </div>
                <div class="pill-row roadmap-card-pills">
                    <span class="badge">${roadmap.topicCount} тем</span>
                    <span class="badge badge-dark">${roadmap.requiredTopicCount} обязательных</span>
                    <span class="badge badge-success">${escapeHtml(formatHours(roadmap.totalEstimatedHours))}</span>
                </div>
                <div class="list-actions roadmap-card-actions">
                    <button class="button ${isActive ? "button-primary" : "button-secondary"}" type="button" data-select-roadmap="${roadmap.id}">
                        ${isActive ? "Выбрано" : "Выбрать"}
                    </button>
                    <a class="button button-ghost" href="${buildDashboardUrl({ roadmapId: roadmap.id })}#catalog-roadmap">К карте</a>
                </div>
            </article>
        `;
    }

    function renderSelectedRoadmap() {
        if (!state.selectedRoadmap) {
            renderEmptyState(
                roadmapSummary,
                "Roadmap не выбран",
                "Выберите направление из каталога слева."
            );
            return;
        }

        roadmapSummary.innerHTML = `
            <div class="selection-card-inner">
                <p class="eyebrow">Выбранное направление</p>
                <h4>${escapeHtml(state.selectedRoadmap.name)}</h4>
                <p>${escapeHtml(state.selectedRoadmap.description || "Roadmap без описания.")}</p>
                <div class="pill-row roadmap-card-pills">
                    <span class="badge">${state.selectedRoadmap.topicCount} тем</span>
                    <span class="badge badge-dark">${state.selectedRoadmap.requiredTopicCount} обязательных</span>
                    <span class="badge badge-success">${escapeHtml(formatHours(state.selectedRoadmap.totalEstimatedHours))}</span>
                </div>
            </div>
        `;
    }

    function renderKnownTopics() {
        if (!state.selectedRoadmap) {
            knownTopicsPicker.innerHTML = "";
            return;
        }

        const orderedTopics = [...state.selectedRoadmap.topics]
            .sort((left, right) => {
                const leftPriority = left.priority ?? Number.MAX_SAFE_INTEGER;
                const rightPriority = right.priority ?? Number.MAX_SAFE_INTEGER;
                if (leftPriority !== rightPriority) {
                    return leftPriority - rightPriority;
                }
                return left.topicId - right.topicId;
            });

        knownTopicsPicker.innerHTML = `
            ${orderedTopics.map((topic, index) => `
                <label class="topic-check-item">
                    <input
                        type="checkbox"
                        value="${topic.topicId}"
                        ${state.knownTopicIds.has(topic.topicId) ? "checked" : ""}
                    >
                    <span>
                        <small class="topic-check-item-order">Шаг ${topic.priority ?? index + 1}</small>
                        <strong>${escapeHtml(topic.topicTitle)}</strong>
                        <small class="topic-check-item-hours">${escapeHtml(formatHours(topic.estimatedHours))}</small>
                    </span>
                </label>
            `).join("")}
        `;

        knownTopicsPicker.querySelectorAll("input[type=checkbox]").forEach((checkbox) => {
            checkbox.addEventListener("change", () => {
                const topicId = Number(checkbox.value);
                if (checkbox.checked) {
                    state.knownTopicIds.add(topicId);
                } else {
                    state.knownTopicIds.delete(topicId);
                }

                renderRoadmapMeta();
                renderRoadmapFlow();
                if (state.selectedTopicId != null) {
                    selectTopic(state.selectedTopicId, { syncUrl: false }).catch((error) => handleError(error, statusBox));
                }
            });
        });
    }

    function renderRoadmapMeta() {
        if (!state.selectedRoadmap) {
            renderEmptyState(
                roadmapMetaContainer,
                "Направление не выбрано",
                "После выбора roadmap здесь появится краткая сводка."
            );
            return;
        }

        const hasPlan = Boolean(state.activePlanId);
        const knownCount = state.knownTopicIds.size;

        roadmapMetaContainer.innerHTML = `
            <section class="panel-grid panel-grid-top-aligned">
                <article class="card panel-card roadmap-summary-card">
                    <p class="eyebrow">Текущая roadmap</p>
                    <h3>${escapeHtml(state.selectedRoadmap.name)}</h3>
                    <p>${escapeHtml(state.selectedRoadmap.description || "Roadmap без описания.")}</p>
                    <div class="pill-row roadmap-summary-pills">
                        <span class="badge">${state.selectedRoadmap.topicCount} тем</span>
                        <span class="badge badge-dark">${state.selectedRoadmap.requiredTopicCount} обязательных</span>
                        <span class="badge badge-success">${escapeHtml(formatHours(calculateRemainingRoadmapHours(state.selectedRoadmap, state.knownTopicIds)))}</span>
                        ${knownCount ? `<span class="badge">Уже знакомо: ${knownCount}</span>` : ""}
                    </div>
                </article>
                <article class="card panel-card roadmap-quick-plan-card">
                    <p class="eyebrow">План</p>
                    <h3>${hasPlan ? "Личный weekly plan" : "Сборка личного плана"}</h3>
                    <p>${hasPlan
                        ? "Для этого направления уже есть активный план. Его можно открыть или продолжить отслеживать прогресс."
                        : "Сначала отметьте знакомые темы и задайте лимит часов в неделю. После этого сервис соберёт weekly plan."}</p>
                    <div class="form-actions roadmap-quick-plan-actions">
                        ${hasPlan
                            ? `
                                <a class="button button-secondary" href="/plan?planId=${state.activePlanId}">Открыть план</a>
                                <a class="button button-ghost" href="/progress?planId=${state.activePlanId}">Прогресс</a>
                            `
                            : `<a class="button button-secondary" href="#build-roadmap-plan-form">Собрать план</a>`}
                    </div>
                </article>
            </section>
        `;
    }

    function calculateRemainingRoadmapHours(roadmap, knownTopicIds) {
        return (roadmap.topics || [])
            .filter((topic) => !knownTopicIds.has(topic.topicId))
            .reduce((sum, topic) => sum + Number(topic.estimatedHours || 0), 0);
    }

    function renderRoadmapFlow() {
        if (!state.selectedRoadmap?.topics?.length) {
            renderEmptyState(
                flowContainer,
                "Roadmap пуста",
                "Для этого направления ещё не заведены темы."
            );
            return;
        }

        const orderedTopics = [...state.selectedRoadmap.topics]
            .sort((left, right) => {
                const leftPriority = left.priority ?? Number.MAX_SAFE_INTEGER;
                const rightPriority = right.priority ?? Number.MAX_SAFE_INTEGER;
                if (leftPriority !== rightPriority) {
                    return leftPriority - rightPriority;
                }
                return left.topicId - right.topicId;
            });

        flowContainer.innerHTML = `
            <div class="roadmap-topic-grid">
                ${orderedTopics.map((topic) => {
                    const isSelected = topic.topicId === state.selectedTopicId;
                    const isPlanned = state.plannedTopicIds.has(topic.topicId);
                    const isKnown = state.knownTopicIds.has(topic.topicId);

                    return `
                        <button class="roadmap-topic-card ${isSelected ? "is-selected" : ""} ${isKnown ? "is-known" : ""}" type="button" data-topic-id="${topic.topicId}">
                            <div class="step-meta">
                                <span class="badge">${escapeHtml(formatHours(topic.estimatedHours))}</span>
                                <span class="badge">${escapeHtml(`Шаг ${topic.priority ?? "—"}`)}</span>
                            </div>
                            <h4>${escapeHtml(topic.topicTitle)}</h4>
                            <p>${escapeHtml(topic.description || "Подробное описание откроется в правой панели.")}</p>
                            <div class="topic-tags roadmap-card-pills">
                                ${topic.isRequired ? `<span class="topic-chip">Обязательная</span>` : `<span class="topic-chip muted">Дополнительная</span>`}
                                ${topic.isCore ? `<span class="topic-chip">Core</span>` : ""}
                                ${isKnown ? `<span class="topic-chip known">Уже знакома</span>` : ""}
                                ${!isKnown && isPlanned ? `<span class="topic-chip">В текущем плане</span>` : ""}
                            </div>
                        </button>
                    `;
                }).join("")}
            </div>
        `;

        flowContainer.querySelectorAll("[data-topic-id]").forEach((button) => {
            button.addEventListener("click", async () => {
                const topicId = Number(button.dataset.topicId);
                await selectTopic(topicId);
            });
        });
    }

    async function selectTopic(topicId, options = {}) {
        if (!state.selectedRoadmap) {
            return;
        }

        state.selectedTopicId = topicId;
        renderRoadmapFlow();

        const topic = await roadmapsApi.getTopic(state.selectedRoadmap.id, topicId);
        state.selectedTopic = topic;
        renderTopicPanel(topic);

        if (options.syncUrl !== false) {
            syncDashboardUrl();
        }
    }

    function renderTopicPanel(topic) {
        const isInPlan = state.plannedTopicIds.has(topic.topicId);
        const isKnown = state.knownTopicIds.has(topic.topicId);
        const tab = state.topicTab;

        topicPanel.innerHTML = `
            <div class="card panel-card topic-detail-card">
                <p class="eyebrow">Topic detail</p>
                <h3>${escapeHtml(topic.topicTitle)}</h3>
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
                    <button class="topic-tab ${tab === "tutor" ? "is-active" : ""}" type="button" data-topic-tab="tutor">AI Tutor</button>
                </div>

                <div class="topic-detail-section">
                    ${renderTopicTabContent(topic, tab, isKnown)}
                </div>
            </div>
        `;

        topicPanel.querySelectorAll("[data-topic-tab]").forEach((button) => {
            button.addEventListener("click", () => {
                state.topicTab = button.dataset.topicTab;
                renderTopicPanel(topic);
            });
        });
    }

    function renderTopicTabContent(topic, tab, isKnown) {
        if (tab === "resources") {
            return topic.resources.length
                ? `
                    <div class="list">
                        ${topic.resources.map((resource) => `
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

        if (tab === "tutor") {
            const quizAvailable = topic.quiz?.available;
            return `
                <div class="tutor-placeholder">
                    <h4>AI Tutor для темы</h4>
                    <p>
                        Следующий этап развития сервиса: topic-scoped AI Tutor будет работать прямо из этой панели
                        и отвечать в контексте выбранной темы, её зависимостей и ресурсов.
                    </p>
                    <div class="pill-row">
                        <span class="badge">${quizAvailable ? "Есть квиз" : "Квиз пока не заведён"}</span>
                        <span class="badge badge-dark">${topic.resources.length} ресурсов</span>
                    </div>
                    <button class="button button-secondary panel-top-gap" type="button" disabled>AI Tutor скоро</button>
                </div>
            `;
        }

        return `
            ${isKnown ? `
                <div class="topic-detail-block">
                    <h4>Статус</h4>
                    <p>Тема отмечена как уже знакомая. Она остаётся в roadmap для понимания структуры направления, но не включается в новый weekly plan.</p>
                </div>
            ` : ""}

            <div class="topic-detail-block">
                <h4>Что нужно знать перед темой</h4>
                ${topic.prereqs.length ? `
                    <div class="topic-tags">
                        ${topic.prereqs.map((item) => `<span class="topic-chip">${escapeHtml(item.topicTitle)}</span>`).join("")}
                    </div>
                ` : "<p>Эту тему можно брать без обязательных предварительных знаний.</p>"}
            </div>

            <div class="topic-detail-block">
                <h4>Что открывает дальше</h4>
                ${topic.unlocks.length ? `
                    <div class="topic-tags">
                        ${topic.unlocks.map((item) => `<span class="topic-chip muted">${escapeHtml(item.topicTitle)}</span>`).join("")}
                    </div>
                ` : "<p>Это конечная тема внутри текущего roadmap.</p>"}
            </div>

            <div class="topic-detail-block">
                <h4>Квиз</h4>
                <p>${topic.quiz?.available ? `Для темы доступен квиз «${escapeHtml(topic.quiz.title)}».` : "Для темы квиз пока не добавлен."}</p>
            </div>
        `;
    }

    async function renderSpotlightForSelectedRoadmap() {
        if (!state.selectedRoadmap) {
            renderEmptyState(
                spotlightContainer,
                "План не выбран",
                "Сначала выберите направление из каталога."
            );
            return;
        }

        const latestPlanItem = findLatestPlanForRoadmap(state.selectedRoadmap.id);
        if (!latestPlanItem) {
            renderEmptyState(
                spotlightContainer,
                "Плана по этому направлению пока нет",
                "После построения weekly plan здесь появится краткая сводка именно по выбранному roadmap."
            );
            return;
        }

        const cached = getCachedPlan(latestPlanItem.id);
        const plan = cached || await plansApi.get(latestPlanItem.id);
        renderSpotlight(plan);
    }

    function findLatestPlanForRoadmap(roleId) {
        return [...state.planItems]
            .filter((plan) => plan.roleId === roleId)
            .sort((left, right) => new Date(right.createdAt) - new Date(left.createdAt))[0] || null;
    }

    function renderSpotlight(plan) {
        const summary = summarizePlan(plan);
        const displayPlan = annotatePlanForDisplay(plan);
        const nextSteps = flattenPlanSteps(displayPlan).slice(0, 3);

        spotlightContainer.innerHTML = `
            <div class="card panel-card plan-highlight">
                <p class="eyebrow">Последний план по направлению</p>
                <h3>${escapeHtml(plan.roleName || "Личный weekly plan")}</h3>
                <p>Snapshot построен ${escapeHtml(formatDate(plan.createdAt))}. Лимит: ${escapeHtml(String(plan.params?.hoursPerWeek || "—"))} ч/нед.</p>
                <div class="metric-grid panel-top-gap">
                    <article class="metric-card card">
                        <p>Недели</p>
                        <strong>${summary.weekCount}</strong>
                        <p>Сколько недель получилось после раскладки roadmap.</p>
                    </article>
                    <article class="metric-card card">
                        <p>Темы</p>
                        <strong>${summary.stepCount}</strong>
                        <p>Шагов в личном плане.</p>
                    </article>
                    <article class="metric-card card">
                        <p>Часы</p>
                        <strong>${summary.totalHours}</strong>
                        <p>Суммарный объём маршрута.</p>
                    </article>
                    <article class="metric-card card metric-card-compact">
                        <p>Roadmap</p>
                        <strong>${escapeHtml(plan.roleName || "Выбранное направление")}</strong>
                        <p>Основа построения weekly plan.</p>
                    </article>
                </div>
                <div class="list panel-top-gap">
                    ${nextSteps.length ? nextSteps.map((step) => `
                        <article class="list-item plan-highlight-step">
                            <div class="list-item-copy">
                                ${formatPlanStepContinuation(step) ? `<p class="step-part-caption">${escapeHtml(formatPlanStepContinuation(step))}</p>` : ""}
                                <h4>${escapeHtml(formatPlanStepTitle(step))}</h4>
                                <p>Неделя ${step.weekIndex}, ${escapeHtml(formatHours(step.plannedHours))}</p>
                            </div>
                            ${step.partLabel ? `<span class="badge plan-highlight-badge">${escapeHtml(step.partLabel)}</span>` : ""}
                        </article>
                    `).join("") : `<p>В этом плане пока нет шагов для предпросмотра.</p>`}
                </div>
                <div class="form-actions panel-top-gap">
                    <a class="button button-primary" href="/plan?planId=${plan.id}">Открыть weekly plan</a>
                    <a class="button button-secondary" href="/progress?planId=${plan.id}">Открыть прогресс</a>
                </div>
            </div>
        `;
    }

    function mergePlanIntoIndex(plan) {
        state.planItems = [
            {
                id: plan.id,
                roleId: plan.roleId,
                roleName: plan.roleName,
                createdAt: plan.createdAt,
                scenarioType: plan.scenarioType,
                status: plan.status
            },
            ...state.planItems.filter((item) => item.id !== plan.id)
        ];
    }

    function readKnownTopicIds() {
        return Array.from(knownTopicsPicker.querySelectorAll("input[type=checkbox]:checked"))
            .map((input) => Number(input.value))
            .filter((value) => !Number.isNaN(value));
    }

    function prefillDraft() {
        const draft = getPlanDraft();
        if (hoursPerWeekField) {
            hoursPerWeekField.value = draft.hoursPerWeek || 10;
        }
    }

    function focusPlanBuilderPanel() {
        if (!planBuilderPanel) {
            return;
        }

        if (window.matchMedia("(max-width: 1080px)").matches) {
            planBuilderPanel.scrollIntoView({ behavior: "smooth", block: "start" });
        }
    }

    function buildDashboardUrl({ roadmapId = state.selectedRoadmap?.id, topicId = state.selectedTopicId, planId = state.activePlanId } = {}) {
        const query = new URLSearchParams();
        if (roadmapId != null) {
            query.set("roadmapId", String(roadmapId));
        }
        if (topicId != null) {
            query.set("topicId", String(topicId));
        }
        if (planId != null) {
            query.set("planId", String(planId));
        }

        const suffix = query.toString();
        return suffix ? `/dashboard?${suffix}` : "/dashboard";
    }

    function syncDashboardUrl() {
        window.history.replaceState({}, "", buildDashboardUrl());
    }

    function handleError(error, target) {
        if (error?.status === 401) {
            window.location.replace("/login");
            return;
        }

        const message = error instanceof ApiError
            ? error.message
            : "Не удалось загрузить данные каталога.";
        showStatus(target, "error", message);
    }
}
