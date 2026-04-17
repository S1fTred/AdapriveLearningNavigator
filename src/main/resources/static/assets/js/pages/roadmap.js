import { ApiError, plansApi, roadmapsApi } from "/assets/js/core/api.js";
import { requireAuth } from "/assets/js/core/guard.js";
import { resolveActivePlan } from "/assets/js/core/plans.js";
import { resolveActiveRoadmap } from "/assets/js/core/roadmaps.js";
import { initPrivateShell } from "/assets/js/core/shell.js";
import { setSelectedRoadmapId } from "/assets/js/core/session.js";
import { escapeHtml, formatHours, renderEmptyState, showStatus } from "/assets/js/core/ui.js";

if (requireAuth()) {
    initPrivateShell("roadmap");

    const statusBox = document.querySelector("#roadmap-status");
    const switcherContainer = document.querySelector("#roadmap-switcher");
    const metaContainer = document.querySelector("#roadmap-meta");
    const flowContainer = document.querySelector("#roadmap-flow");
    const topicPanel = document.querySelector("#roadmap-topic-panel");

    const state = {
        roadmap: null,
        selectedTopicId: null,
        activePlanId: null,
        plannedTopicIds: new Set(),
        topicTab: "overview"
    };

    loadPage().catch((error) => handleError(error));

    async function loadPage() {
        const query = new URLSearchParams(window.location.search);
        const explicitRoadmapId = Number(query.get("roadmapId")) || null;
        const explicitTopicId = Number(query.get("topicId")) || null;
        const explicitPlanId = Number(query.get("planId")) || null;
        const roadmap = await resolveActiveRoadmap(explicitRoadmapId);

        if (!roadmap) {
            renderEmptyState(
                metaContainer,
                "Roadmap пока нет",
                "В knowledge base пока не заведены направления."
            );
            flowContainer.innerHTML = "";
            topicPanel.innerHTML = "";
            return;
        }

        state.roadmap = roadmap;
        setSelectedRoadmapId(roadmap.id);
        await hydratePlanContext(roadmap.id, explicitPlanId);
        await renderSwitcher(roadmap.id);
        renderMeta(roadmap);
        renderFlow(roadmap);

        const initialTopicId = explicitTopicId || roadmap.topics?.[0]?.topicId || null;
        if (initialTopicId) {
            await selectTopic(initialTopicId);
        } else {
            renderEmptyState(
                topicPanel,
                "Тема не выбрана",
                "Выберите карточку в roadmap, чтобы открыть детали."
            );
        }
    }

    async function hydratePlanContext(roleId, explicitPlanId) {
        state.activePlanId = null;
        state.plannedTopicIds = new Set();

        let plan = null;
        if (explicitPlanId) {
            plan = await plansApi.get(explicitPlanId);
        } else {
            plan = await resolveActivePlan();
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

    async function renderSwitcher(activeRoadmapId) {
        const page = await roadmapsApi.list(0, 40);
        switcherContainer.innerHTML = `
            <div class="card panel-card">
                <p class="eyebrow">Roadmap catalog</p>
                <h3>Переключить направление</h3>
                <div class="form-row panel-top-gap">
                    <label class="field-label" for="roadmap-select">Выберите roadmap</label>
                    <select class="select" id="roadmap-select">
                        ${(page.items || []).map((item) => `
                            <option value="${item.id}" ${item.id === activeRoadmapId ? "selected" : ""}>
                                ${escapeHtml(item.name)} · ${escapeHtml(item.code)}
                            </option>
                        `).join("")}
                    </select>
                </div>
            </div>
        `;

        switcherContainer.querySelector("#roadmap-select")?.addEventListener("change", (event) => {
            const roadmapId = Number(event.target.value);
            const suffix = state.activePlanId ? `&planId=${state.activePlanId}` : "";
            window.location.href = `/roadmap?roadmapId=${roadmapId}${suffix}`;
        });
    }

    function renderMeta(roadmap) {
        const hasPlan = Boolean(state.activePlanId);

        metaContainer.innerHTML = `
            <section class="panel-grid">
                <article class="card panel-card">
                    <p class="eyebrow">Curated roadmap</p>
                    <h3>${escapeHtml(roadmap.name)}</h3>
                    <p>${escapeHtml(roadmap.description || "Roadmap без описания.")}</p>
                    <div class="pill-row panel-top-gap">
                        <span class="badge">${roadmap.topicCount} тем</span>
                        <span class="badge badge-dark">${roadmap.requiredTopicCount} обязательных</span>
                        <span class="badge badge-success">${escapeHtml(formatHours(roadmap.totalEstimatedHours))}</span>
                    </div>
                </article>
                <article class="card panel-card">
                    <p class="eyebrow">Следующий шаг</p>
                    <h3>Из roadmap в личный план</h3>
                    <p>
                        Roadmap описывает структуру направления, а weekly plan раскладывает её по вашему темпу.
                        ${hasPlan ? "Для этого направления уже есть активный план." : "План можно собрать из кабинета."}
                    </p>
                    <div class="form-actions panel-top-gap">
                        <a class="button button-primary" href="/dashboard">Собрать или пересобрать план</a>
                        <a class="button button-secondary" href="${hasPlan ? `/plan?planId=${state.activePlanId}` : "/plan"}">Открыть weekly plan</a>
                    </div>
                </article>
            </section>
        `;
    }

    function renderFlow(roadmap) {
        if (!roadmap.topics?.length) {
            renderEmptyState(
                flowContainer,
                "Roadmap пуст",
                "Для этого направления ещё не заведены темы."
            );
            return;
        }

        flowContainer.innerHTML = `
            <div class="roadmap-topic-grid">
                ${roadmap.topics.map((topic) => {
                    const isSelected = topic.topicId === state.selectedTopicId;
                    const isPlanned = state.plannedTopicIds.has(topic.topicId);
                    return `
                        <button class="roadmap-topic-card ${isSelected ? "is-selected" : ""}" type="button" data-topic-id="${topic.topicId}">
                            <div class="step-meta">
                                <span class="badge badge-dark">${escapeHtml(topic.topicCode)}</span>
                                <span class="badge">${escapeHtml(formatHours(topic.estimatedHours))}</span>
                            </div>
                            <h4>${escapeHtml(topic.topicTitle)}</h4>
                            <p>${escapeHtml(topic.description || "Подробное описание откроется в правой панели.")}</p>
                            <div class="topic-tags roadmap-card-pills">
                                ${topic.isRequired ? `<span class="topic-chip">Обязательная</span>` : `<span class="topic-chip muted">Дополнительная</span>`}
                                ${topic.isCore ? `<span class="topic-chip">Core</span>` : ""}
                                ${isPlanned ? `<span class="topic-chip">В текущем плане</span>` : ""}
                                ${topic.prereqTopicCodes.map((code) => `<span class="topic-chip muted">${escapeHtml(code)}</span>`).join("")}
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

    async function selectTopic(topicId) {
        if (!state.roadmap) {
            return;
        }

        state.selectedTopicId = topicId;
        renderFlow(state.roadmap);

        const topic = await roadmapsApi.getTopic(state.roadmap.id, topicId);
        renderTopicPanel(topic);
    }

    function renderTopicPanel(topic) {
        const isInPlan = state.plannedTopicIds.has(topic.topicId);
        const tab = state.topicTab;

        topicPanel.innerHTML = `
            <div class="card panel-card topic-detail-card">
                <p class="eyebrow">Topic detail</p>
                <h3>${escapeHtml(topic.topicTitle)}</h3>
                <p>${escapeHtml(topic.description || "Подробное описание темы пока не заполнено.")}</p>
                <div class="pill-row panel-top-gap">
                    <span class="badge badge-dark">${escapeHtml(topic.topicCode)}</span>
                    <span class="badge">${escapeHtml(formatHours(topic.estimatedHours))}</span>
                    ${topic.isRequired ? `<span class="badge badge-success">Обязательная</span>` : `<span class="badge">Дополнительная</span>`}
                    ${isInPlan ? `<span class="badge">В активном плане</span>` : ""}
                </div>

                <div class="topic-tab-row panel-top-gap">
                    <button class="topic-tab ${tab === "overview" ? "is-active" : ""}" type="button" data-topic-tab="overview">Обзор</button>
                    <button class="topic-tab ${tab === "resources" ? "is-active" : ""}" type="button" data-topic-tab="resources">Ресурсы</button>
                    <button class="topic-tab ${tab === "tutor" ? "is-active" : ""}" type="button" data-topic-tab="tutor">AI Tutor</button>
                </div>

                <div class="topic-detail-section">
                    ${renderTopicTabContent(topic, tab)}
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

    function renderTopicTabContent(topic, tab) {
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
                        Следующий этап миграции: topic-scoped AI Tutor будет работать из этой панели и
                        отвечать в контексте выбранной темы, её prerequisite и ресурсов.
                    </p>
                    <div class="pill-row">
                        <span class="badge">${quizAvailable ? "Есть квиз" : "Квиз пока не заведен"}</span>
                        <span class="badge badge-dark">${topic.resources.length} ресурсов</span>
                    </div>
                    <button class="button button-secondary panel-top-gap" type="button" disabled>AI Tutor скоро</button>
                </div>
            `;
        }

        return `
            <div class="topic-detail-block">
                <h4>Prerequisite</h4>
                ${topic.prereqs.length ? `
                    <div class="topic-tags">
                        ${topic.prereqs.map((item) => `<span class="topic-chip">${escapeHtml(item.topicTitle)}</span>`).join("")}
                    </div>
                ` : "<p>Эту тему можно брать без обязательных prerequisite.</p>"}
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
