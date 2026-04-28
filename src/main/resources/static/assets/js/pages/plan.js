import { ApiError, plansApi, progressApi } from "/assets/js/core/api.js";
import { requireAuth } from "/assets/js/core/guard.js";
import { resolveActivePlan } from "/assets/js/core/plans.js";
import { initPrivateShell } from "/assets/js/core/shell.js";
import { setSelectedPlanId } from "/assets/js/core/session.js";
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

const PROGRESS_STATUSES = [
    { value: "NOT_STARTED", label: "Не начато" },
    { value: "IN_PROGRESS", label: "В процессе" },
    { value: "DONE", label: "Готово" }
];

const STATUS_LABELS = Object.fromEntries(PROGRESS_STATUSES.map((item) => [item.value, item.label]));

if (requireAuth()) {
    initPrivateShell("plan");

    const statusBox = document.querySelector("#plan-status");
    const summaryContainer = document.querySelector("#plan-summary");
    const weeksContainer = document.querySelector("#plan-weeks");
    const switcherContainer = document.querySelector("#plan-switcher");

    const state = {
        plan: null,
        progress: new Map()
    };

    loadPage().catch(handleError);

    async function loadPage() {
        const explicitPlanId = Number(new URLSearchParams(window.location.search).get("planId")) || null;
        const plan = await resolveActivePlan(explicitPlanId);

        if (!plan) {
            renderEmptyState(
                summaryContainer,
                "План пока не выбран",
                "Сначала соберите маршрут в каталоге. После этого здесь появится недельная раскладка и прогресс по темам.",
                `<div class="form-actions panel-top-gap"><a class="button button-primary" href="/dashboard">Открыть каталог</a></div>`
            );
            weeksContainer.innerHTML = "";
            switcherContainer.innerHTML = "";
            return;
        }

        state.plan = annotatePlanForDisplay(plan);
        state.progress = await loadProgressMap(plan.id);

        renderPlan();
        await renderSwitcher(plan.id);
    }

    async function loadProgressMap(planId) {
        const items = await progressApi.list(planId);
        return new Map((items || []).map((item) => [String(item.topicId), item.status || "NOT_STARTED"]));
    }

    async function renderSwitcher(activePlanId) {
        const page = await plansApi.list(0, 20);
        const items = page.items || [];

        if (items.length <= 1) {
            switcherContainer.innerHTML = "";
            return;
        }

        switcherContainer.innerHTML = `
            <div class="card panel-card">
                <p class="eyebrow">Навигация</p>
                <h3>Другой сохранённый план</h3>
                <div class="form-row panel-top-gap">
                    <label class="field-label" for="plan-select">Выберите план</label>
                    <select class="select" id="plan-select">
                        ${items.map((item) => `
                            <option value="${item.id}" ${item.id === activePlanId ? "selected" : ""}>
                                ${escapeHtml(item.roleName || "План обучения")} · ${escapeHtml(formatDate(item.createdAt))}
                            </option>
                        `).join("")}
                    </select>
                </div>
            </div>
        `;

        switcherContainer.querySelector("#plan-select")?.addEventListener("change", (event) => {
            const planId = Number(event.target.value);
            setSelectedPlanId(planId);
            window.location.href = `/plan?planId=${planId}`;
        });
    }

    function renderPlan() {
        renderSummary(state.plan);
        renderWeeks(state.plan);
        bindProgressActions();
    }

    function renderSummary(plan) {
        const stats = calculateProgressStats(plan);
        const nextStep = findNextStep(plan);

        summaryContainer.innerHTML = `
            <section class="panel-grid">
                <article class="card panel-card">
                    <p class="eyebrow">Недельный план</p>
                    <h3>${escapeHtml(plan.roleName || "План обучения")}</h3>
                    <p>Лимит: ${escapeHtml(String(plan.params?.hoursPerWeek || "—"))} ч/нед. План можно проходить прямо здесь: отмечайте темы по мере работы.</p>
                    <div class="pill-row panel-top-gap">
                        <span class="badge">${plan.weeks.length} недель</span>
                        <span class="badge">${stats.totalTopics} тем</span>
                        <span class="badge badge-success">${escapeHtml(formatHours(stats.totalHours))}</span>
                    </div>
                    <div class="form-actions panel-top-gap">
                        <a class="button button-secondary" href="/dashboard?roadmapId=${plan.roleId}&planId=${plan.id}">Каталог</a>
                        <a class="button button-ghost" href="/roadmap?roadmapId=${plan.roleId}&planId=${plan.id}">Карта направления</a>
                    </div>
                </article>

                <article class="card panel-card plan-progress-card">
                    <p class="eyebrow">Прогресс</p>
                    <h3>${stats.percent}% завершено</h3>
                    <div class="progress-bar panel-top-gap"><span style="width:${stats.percent}%;"></span></div>
                    <div class="plan-progress-metrics">
                        <span>${stats.doneTopics} из ${stats.totalTopics} тем</span>
                        <span>${escapeHtml(formatHours(stats.doneHours))} из ${escapeHtml(formatHours(stats.totalHours))}</span>
                    </div>
                    <p>${nextStep ? `Следующий шаг: ${escapeHtml(formatPlanStepTitle(nextStep))}` : "Все темы в плане отмечены как готовые."}</p>
                </article>
            </section>
        `;
    }

    function renderWeeks(plan) {
        weeksContainer.innerHTML = `
            <div class="week-grid">
                ${plan.weeks.map((week) => renderWeek(week)).join("")}
            </div>
        `;
    }

    function renderWeek(week) {
        const percent = Number(week.hoursBudget) > 0
            ? Math.min(100, (Number(week.hoursPlanned) / Number(week.hoursBudget)) * 100)
            : 0;

        return `
            <article class="card week-card">
                <div class="week-card-header">
                    <div>
                        <p class="eyebrow">Неделя ${week.weekIndex}</p>
                        <h3>${escapeHtml(formatHours(week.hoursPlanned))} / ${escapeHtml(formatHours(week.hoursBudget))}</h3>
                    </div>
                    <span class="badge">${week.steps.length} шага</span>
                </div>
                <div class="progress-bar"><span style="width:${percent}%;"></span></div>
                <div class="step-stack">
                    ${week.steps.map((step) => renderStep(step)).join("")}
                </div>
            </article>
        `;
    }

    function renderStep(step) {
        const status = getStepStatus(step);
        const continuation = formatPlanStepContinuation(step);

        return `
            <article class="step-card plan-step-card is-${status.toLowerCase().replaceAll("_", "-")}" data-topic-id="${step.topicId}">
                <div class="step-meta">
                    <span class="badge">${escapeHtml(formatHours(step.plannedHours))}</span>
                    ${step.partLabel ? `<span class="badge">${escapeHtml(step.partLabel)}</span>` : ""}
                    <span class="badge ${status === "DONE" ? "badge-success" : ""}">${escapeHtml(STATUS_LABELS[status] || "Не начато")}</span>
                </div>
                ${continuation ? `<p class="step-part-caption">${escapeHtml(continuation)}</p>` : ""}
                <h4>${escapeHtml(formatPlanStepTitle(step))}</h4>
                <p>${escapeHtml(step.explanation?.topicPriorityReason || "Тема входит в выбранный roadmap.")}</p>
                ${renderPrereqStatus(step)}
                <div class="progress-control" aria-label="Статус темы">
                    ${PROGRESS_STATUSES.map((item) => `
                        <button
                            class="progress-control-button ${status === item.value ? "is-active" : ""}"
                            type="button"
                            data-progress-topic="${step.topicId}"
                            data-progress-status="${item.value}"
                        >
                            ${escapeHtml(item.label)}
                        </button>
                    `).join("")}
                </div>
            </article>
        `;
    }

    function bindProgressActions() {
        weeksContainer.querySelectorAll("[data-progress-topic]").forEach((button) => {
            button.addEventListener("click", async () => {
                const topicId = Number(button.dataset.progressTopic);
                const status = button.dataset.progressStatus;
                await updateTopicProgress(topicId, status);
            });
        });
    }

    async function updateTopicProgress(topicId, status) {
        if (!state.plan) {
            return;
        }

        clearStatus(statusBox);

        try {
            const response = await progressApi.update(state.plan.id, { topicId, status });
            state.progress.set(String(response.topicId), response.status || status);
            renderPlan();
            showStatus(statusBox, "success", "Прогресс обновлён.");
        } catch (error) {
            handleError(error);
        }
    }

    function calculateProgressStats(plan) {
        const steps = flattenPlanSteps(plan);
        const uniqueTopicIds = new Set(steps.map((step) => String(step.topicId)));
        const doneTopicIds = new Set(
            Array.from(uniqueTopicIds).filter((topicId) => state.progress.get(topicId) === "DONE")
        );
        const doneHours = steps
            .filter((step) => doneTopicIds.has(String(step.topicId)))
            .reduce((sum, step) => sum + Number(step.plannedHours || 0), 0);
        const totalHours = steps.reduce((sum, step) => sum + Number(step.plannedHours || 0), 0);
        const totalTopics = uniqueTopicIds.size;
        const doneTopics = doneTopicIds.size;

        return {
            totalTopics,
            doneTopics,
            totalHours,
            doneHours,
            percent: totalTopics ? Math.round((doneTopics / totalTopics) * 100) : 0
        };
    }

    function findNextStep(plan) {
        return flattenPlanSteps(plan).find((step) => getStepStatus(step) !== "DONE") || null;
    }

    function getStepStatus(step) {
        return state.progress.get(String(step.topicId)) || "NOT_STARTED";
    }

    function renderPrereqStatus(step) {
        const prereqs = step.explanation?.prereqs || [];
        if (!prereqs.length) {
            return "";
        }

        return `
            <div class="topic-tags panel-top-gap">
                ${prereqs.map((item) => `
                    <span class="topic-chip muted">
                        Зависимость: ${escapeHtml(formatPrereqStatus(item.status))}
                    </span>
                `).join("")}
            </div>
        `;
    }

    function formatPrereqStatus(status) {
        switch (status) {
            case "DONE":
                return "уже знакома";
            case "IN_PREVIOUS_STEPS":
                return "будет изучена раньше";
            case "MISSING":
                return "нужно добавить в план";
            default:
                return "учтена в плане";
        }
    }

    function handleError(error) {
        if (error?.status === 401) {
            window.location.replace("/login");
            return;
        }

        const message = error instanceof ApiError
            ? error.message
            : "Не удалось загрузить недельный план.";
        showStatus(statusBox, "error", message);
    }
}
