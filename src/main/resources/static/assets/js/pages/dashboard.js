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
    clearStatus,
    escapeHtml,
    flattenPlanSteps,
    formatDate,
    formatHours,
    formatRuleLabel,
    renderEmptyState,
    showStatus
} from "/assets/js/core/ui.js";

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
    const plansContainer = document.querySelector("#plans-list");
    const spotlightContainer = document.querySelector("#plan-spotlight");
    const hoursPerWeekField = document.querySelector("#hoursPerWeek");

    const state = {
        roadmaps: [],
        planItems: [],
        selectedRoadmap: null,
        searchQuery: "",
        knownTopicIds: new Set()
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
            await renderPlansList();
            renderSpotlight(plan);
            showStatus(statusBox, "success", "План собран. Можно открыть roadmap или недельную раскладку.");
        } catch (error) {
            handleError(error, statusBox);
        } finally {
            submitButton.disabled = false;
            submitButton.textContent = "Построить недельный план";
        }
    });

    async function loadDashboard() {
        const roadmapsPage = await roadmapsApi.list(0, 48);
        state.roadmaps = roadmapsPage.items || [];

        if (!state.roadmaps.length) {
            renderEmptyState(
                roadmapsContainer,
                "Каталог пока пуст",
                "В базе знаний пока нет опубликованных roadmap'ов."
            );
            renderEmptyState(
                roadmapSummary,
                "Roadmap не выбран",
                "Когда в базе появятся направления, здесь можно будет собрать недельный план."
            );
            renderEmptyState(
                spotlightContainer,
                "Нет плана по направлению",
                "После первого построения плана здесь появится краткая сводка по выбранному roadmap."
            );
            return;
        }

        const activeRoadmap = await resolveActiveRoadmap(getPlanDraft().roleId || null);
        if (activeRoadmap) {
            await selectRoadmap(activeRoadmap.id);
        }

        renderRoadmapCatalog();
        await renderPlansList();
        await renderSpotlightForSelectedRoadmap();
    }

    async function selectRoadmap(roadmapId) {
        const roadmap = await roadmapsApi.get(roadmapId);
        const draft = getPlanDraft();

        state.selectedRoadmap = roadmap;
        state.knownTopicIds = new Set(
            (draft.roleId === roadmapId ? draft.knownTopicIds : [])
                .filter((topicId) => roadmap.topics.some((topic) => topic.topicId === topicId))
        );

        setSelectedRoadmapId(roadmapId);
        selectedRoadmapIdField.value = String(roadmapId);
        selectedRoadmapLink.href = `/roadmap?roadmapId=${roadmapId}`;

        renderSelectedRoadmap();
        renderRoadmapCatalog();
        renderKnownTopics();
        await renderSpotlightForSelectedRoadmap();
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

        roadmapsContainer.innerHTML = `
            <div class="roadmap-catalog">
                ${items.map((roadmap) => {
                    const isActive = roadmap.id === state.selectedRoadmap?.id;
                    return `
                        <article class="card roadmap-catalog-card ${isActive ? "is-selected" : ""}">
                            <div>
                                <p class="eyebrow">${escapeHtml(roadmap.code || "roadmap")}</p>
                                <h4>${escapeHtml(roadmap.name)}</h4>
                                <p>${escapeHtml(roadmap.description || "Roadmap без описания.")}</p>
                            </div>
                            <div class="pill-row roadmap-card-pills">
                                <span class="badge">${roadmap.topicCount} тем</span>
                                <span class="badge badge-dark">${roadmap.requiredTopicCount} обязательных</span>
                                <span class="badge badge-success">${escapeHtml(formatHours(roadmap.totalEstimatedHours))}</span>
                            </div>
                            <div class="list-actions roadmap-card-actions">
                                <button class="button ${isActive ? "button-primary" : "button-secondary"}" data-select-roadmap="${roadmap.id}">
                                    ${isActive ? "Выбрано" : "Выбрать"}
                                </button>
                                <a class="button button-ghost" href="/roadmap?roadmapId=${roadmap.id}">Открыть</a>
                            </div>
                        </article>
                    `;
                }).join("")}
            </div>
        `;

        roadmapsContainer.querySelectorAll("[data-select-roadmap]").forEach((button) => {
            button.addEventListener("click", async () => {
                const roadmapId = Number(button.dataset.selectRoadmap);
                await selectRoadmap(roadmapId);
            });
        });
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

        knownTopicsPicker.innerHTML = `
            ${state.selectedRoadmap.topics.map((topic) => `
                <label class="topic-check-item">
                    <input
                        type="checkbox"
                        value="${topic.topicId}"
                        ${state.knownTopicIds.has(topic.topicId) ? "checked" : ""}
                    >
                    <span>
                        <strong>${escapeHtml(topic.topicTitle)}</strong>
                        <small>${escapeHtml(topic.topicCode)} · ${escapeHtml(formatHours(topic.estimatedHours))}</small>
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
            });
        });
    }

    async function renderPlansList() {
        const page = await plansApi.list(0, 12);
        const items = page.items || [];
        state.planItems = items;

        if (!items.length) {
            renderEmptyState(
                plansContainer,
                "Планы пока не создавались",
                "После первого build-from-roadmap здесь появятся сохранённые weekly plan snapshots."
            );
            return;
        }

        plansContainer.innerHTML = `
            <div class="list">
                ${items.map((plan) => `
                    <article class="list-item">
                        <div>
                            <h4>${escapeHtml(plan.roleName || plan.roleCode || "План обучения")}</h4>
                            <p>Создан: ${escapeHtml(formatDate(plan.createdAt))}</p>
                            <div class="pill-row roadmap-card-pills">
                                <span class="badge">${escapeHtml(plan.scenarioType || "BASE")}</span>
                                <span class="badge badge-dark">${escapeHtml(plan.status || "DRAFT")}</span>
                            </div>
                        </div>
                        <div class="list-actions">
                            <a class="button button-secondary" href="/roadmap?roadmapId=${plan.roleId}&planId=${plan.id}">Roadmap</a>
                            <a class="button button-ghost" href="/plan?planId=${plan.id}">Недели</a>
                        </div>
                    </article>
                `).join("")}
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
        const nextSteps = flattenPlanSteps(plan).slice(0, 3);

        spotlightContainer.innerHTML = `
            <div class="card panel-card plan-highlight">
                <p class="eyebrow">Последний план по направлению</p>
                <h3>${escapeHtml(plan.roleName || plan.roleCode || "Личный weekly plan")}</h3>
                <p>Snapshot построен ${escapeHtml(formatDate(plan.createdAt))}. Лимит: ${escapeHtml(String(plan.params?.hoursPerWeek || "—"))} ч/нед.</p>
                <div class="metric-grid panel-top-gap">
                    <article class="metric-card card">
                        <p>Недель</p>
                        <strong>${summary.weekCount}</strong>
                        <p>Сколько недель получилось после раскладки roadmap.</p>
                    </article>
                    <article class="metric-card card">
                        <p>Тем</p>
                        <strong>${summary.stepCount}</strong>
                        <p>Шагов в личном плане.</p>
                    </article>
                    <article class="metric-card card">
                        <p>Часов</p>
                        <strong>${summary.totalHours}</strong>
                        <p>Суммарный объём маршрута.</p>
                    </article>
                    <article class="metric-card card metric-card-compact">
                        <p>Roadmap</p>
                        <strong class="metric-card-code">${escapeHtml(plan.roleCode || "—")}</strong>
                        <p>Основа построения weekly plan.</p>
                    </article>
                </div>
                <div class="list panel-top-gap">
                    ${nextSteps.length ? nextSteps.map((step) => `
                        <article class="list-item plan-highlight-step">
                            <div class="list-item-copy">
                                <h4>${escapeHtml(step.topicTitle || step.topicCode || `Тема ${step.topicId}`)}</h4>
                                <p>Неделя ${step.weekIndex}, ${escapeHtml(formatHours(step.plannedHours))}</p>
                            </div>
                            <span class="badge badge-dark plan-highlight-badge">${escapeHtml(formatRuleLabel(step.explanation?.ruleApplied || "ROADMAP"))}</span>
                        </article>
                    `).join("") : `<p>В этом плане пока нет шагов для предпросмотра.</p>`}
                </div>
                <div class="form-actions panel-top-gap">
                    <a class="button button-primary" href="/roadmap?roadmapId=${plan.roleId}&planId=${plan.id}">Открыть roadmap</a>
                    <a class="button button-secondary" href="/plan?planId=${plan.id}">Открыть недельный план</a>
                </div>
            </div>
        `;
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

    function handleError(error, target) {
        if (error?.status === 401) {
            window.location.replace("/login");
            return;
        }

        const message = error instanceof ApiError
            ? error.message
            : "Не удалось загрузить данные кабинета.";
        showStatus(target, "error", message);
    }
}
