import { ApiError, plansApi, roadmapsApi } from "/assets/js/core/api.js";
import { requireAuth } from "/assets/js/core/guard.js";
import { resolveActiveRoadmap } from "/assets/js/core/roadmaps.js";
import { initPrivateShell } from "/assets/js/core/shell.js";
import {
    getPlanDraft,
    savePlanDraft,
    setSelectedPlanId,
    setSelectedRoadmapId
} from "/assets/js/core/session.js";
import {
    clearStatus,
    escapeHtml,
    formatHours,
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
    const selectedPlanActions = document.querySelector("#selected-plan-actions");
    const knownTopicsPicker = document.querySelector("#known-topics-picker");
    const buildForm = document.querySelector("#build-roadmap-plan-form");
    const submitButton = document.querySelector("#build-plan-submit");
    const planBuilderPanel = document.querySelector("[data-dashboard-plan-panel]");
    const hoursPerWeekField = document.querySelector("#hoursPerWeek");

    const state = {
        roadmaps: [],
        planItems: [],
        selectedRoadmap: null,
        searchQuery: "",
        activeCatalogCategory: "ROLE_BASED",
        activePlanId: null,
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
            mergePlanIntoIndex(plan);
            state.activePlanId = plan.id;
            renderSelectedPlanActions();
            renderRoadmapCatalog();
            showStatus(statusBox, "success", "План собран. Его можно открыть из правой панели или продолжить работу с картой направления.");
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
            renderSelectedPlanActions();
            return;
        }

        const activeRoadmap = await resolveActiveRoadmap(explicitRoadmapId || getPlanDraft().roleId || null);
        renderRoadmapCatalog();

        if (activeRoadmap) {
            await selectRoadmap(activeRoadmap.id, { focusPlanBuilder: false });
        }
    }

    async function selectRoadmap(roadmapId, options = {}) {
        const roadmap = await roadmapsApi.get(roadmapId);
        const draft = getPlanDraft();

        state.selectedRoadmap = roadmap;
        state.activeCatalogCategory = roadmap.category || state.activeCatalogCategory;
        state.activePlanId = findLatestPlanForRoadmap(roadmapId)?.id || null;
        state.knownTopicIds = new Set(
            (draft.roleId === roadmapId ? draft.knownTopicIds : [])
                .filter((topicId) => roadmap.topics.some((topic) => topic.topicId === topicId))
        );

        setSelectedRoadmapId(roadmapId);
        selectedRoadmapIdField.value = String(roadmapId);
        selectedRoadmapLink.href = buildRoadmapUrl(roadmapId, state.activePlanId);

        renderSelectedRoadmap();
        renderKnownTopics();
        renderSelectedPlanActions();
        renderRoadmapCatalog();

        if (options.focusPlanBuilder !== false) {
            focusPlanBuilderPanel();
        }
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
        const latestPlan = findLatestPlanForRoadmap(roadmap.id);

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
                    <a class="button button-ghost" href="${buildRoadmapUrl(roadmap.id, latestPlan?.id)}">К карте</a>
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

        const remainingHours = calculateRemainingRoadmapHours(state.selectedRoadmap, state.knownTopicIds);

        roadmapSummary.innerHTML = `
            <div class="selection-card-inner">
                <p class="eyebrow">Выбранное направление</p>
                <h4>${escapeHtml(state.selectedRoadmap.name)}</h4>
                <p>${escapeHtml(state.selectedRoadmap.description || "Roadmap без описания.")}</p>
                <div class="pill-row roadmap-card-pills">
                    <span class="badge">${state.selectedRoadmap.topicCount} тем</span>
                    <span class="badge badge-dark">${state.selectedRoadmap.requiredTopicCount} обязательных</span>
                    <span class="badge badge-success">${escapeHtml(formatHours(remainingHours))}</span>
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
                renderSelectedRoadmap();
            });
        });
    }

    function renderSelectedPlanActions() {
        if (!selectedPlanActions) {
            return;
        }

        if (!state.activePlanId) {
            selectedPlanActions.innerHTML = "";
            return;
        }

        selectedPlanActions.innerHTML = `
            <a class="button button-secondary" href="/plan?planId=${state.activePlanId}">Открыть план</a>
        `;
    }

    function calculateRemainingRoadmapHours(roadmap, knownTopicIds) {
        return (roadmap.topics || [])
            .filter((topic) => !knownTopicIds.has(topic.topicId))
            .reduce((sum, topic) => sum + Number(topic.estimatedHours || 0), 0);
    }

    function findLatestPlanForRoadmap(roleId) {
        return [...state.planItems]
            .filter((plan) => plan.roleId === roleId)
            .sort((left, right) => new Date(right.createdAt) - new Date(left.createdAt))[0] || null;
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

    function buildRoadmapUrl(roadmapId, planId = null) {
        const query = new URLSearchParams({ roadmapId: String(roadmapId) });
        if (planId != null) {
            query.set("planId", String(planId));
        }
        return `/roadmap?${query.toString()}`;
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
