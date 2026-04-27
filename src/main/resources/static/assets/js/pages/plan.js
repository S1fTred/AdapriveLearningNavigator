import { ApiError, plansApi } from "/assets/js/core/api.js";
import { requireAuth } from "/assets/js/core/guard.js";
import { resolveActivePlan } from "/assets/js/core/plans.js";
import { initPrivateShell } from "/assets/js/core/shell.js";
import { setSelectedPlanId } from "/assets/js/core/session.js";
import {
    annotatePlanForDisplay,
    escapeHtml,
    formatDate,
    formatHours,
    formatPlanStepContinuation,
    formatPlanStepTitle,
    formatRuleLabel,
    renderEmptyState,
    showStatus
} from "/assets/js/core/ui.js";

if (requireAuth()) {
    initPrivateShell("plan");

    const statusBox = document.querySelector("#plan-status");
    const summaryContainer = document.querySelector("#plan-summary");
    const weeksContainer = document.querySelector("#plan-weeks");
    const switcherContainer = document.querySelector("#plan-switcher");

    loadPage().catch((error) => handleError(error));

    async function loadPage() {
        const explicitPlanId = Number(new URLSearchParams(window.location.search).get("planId")) || null;
        const plan = await resolveActivePlan(explicitPlanId);

        if (!plan) {
            renderEmptyState(
                summaryContainer,
                "План пока не выбран",
                "Сначала соберите маршрут в кабинете. После этого здесь появится недельная раскладка.",
                `<div class="form-actions panel-top-gap"><a class="button button-primary" href="/dashboard">Открыть кабинет</a></div>`
            );
            weeksContainer.innerHTML = "";
            switcherContainer.innerHTML = "";
            return;
        }

        renderSummary(plan);
        renderWeeks(plan);
        await renderSwitcher(plan.id);
    }

    async function renderSwitcher(activePlanId) {
        const page = await plansApi.list(0, 20);
        switcherContainer.innerHTML = `
            <div class="card panel-card">
                <p class="eyebrow">Навигация</p>
                <h3>Другой сохранённый план</h3>
                <div class="form-row panel-top-gap">
                    <label class="field-label" for="plan-select">Выберите план</label>
                    <select class="select" id="plan-select">
                        ${(page.items || []).map((item) => `
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

    function renderSummary(plan) {
        const displayPlan = annotatePlanForDisplay(plan);

        summaryContainer.innerHTML = `
            <section class="panel-grid">
                <article class="card panel-card">
                    <p class="eyebrow">Недельный ритм</p>
                    <h3>${escapeHtml(displayPlan.roleName || "Недельный план")}</h3>
                    <p>Лимит пользователя: ${escapeHtml(String(displayPlan.params?.hoursPerWeek || "—"))} ч/нед</p>
                    <div class="pill-row panel-top-gap">
                        <span class="badge">${displayPlan.weeks.length} недель</span>
                    </div>
                </article>
                <article class="card panel-card">
                    <p class="eyebrow">Действия</p>
                    <h3>Переключение режимов</h3>
                    <div class="form-actions panel-top-gap">
                        <a class="button button-primary" href="/roadmap?roadmapId=${plan.roleId}&planId=${plan.id}">Открыть roadmap</a>
                        <a class="button button-secondary" href="/dashboard">Вернуться в кабинет</a>
                    </div>
                </article>
            </section>
        `;
    }

    function renderWeeks(plan) {
        const displayPlan = annotatePlanForDisplay(plan);

        weeksContainer.innerHTML = `
            <div class="week-grid">
                ${displayPlan.weeks.map((week) => {
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
                                ${week.steps.map((step) => `
                                    <article class="step-card">
                                        <div class="step-meta">
                                            <span class="badge">${escapeHtml(formatHours(step.plannedHours))}</span>
                                            ${step.partLabel ? `<span class="badge">${escapeHtml(step.partLabel)}</span>` : ""}
                                            <span class="badge badge-dark">${escapeHtml(formatRuleLabel(step.explanation?.ruleApplied || "ROADMAP"))}</span>
                                        </div>
                                        ${formatPlanStepContinuation(step) ? `<p class="step-part-caption">${escapeHtml(formatPlanStepContinuation(step))}</p>` : ""}
                                        <h4>${escapeHtml(formatPlanStepTitle(step))}</h4>
                                        <p>${escapeHtml(step.explanation?.topicPriorityReason || "Пояснение недоступно.")}</p>
                                        ${renderPrereqStatus(step)}
                                    </article>
                                `).join("")}
                            </div>
                        </article>
                    `;
                }).join("")}
            </div>
        `;
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
