import { ApiError, plansApi } from "/assets/js/core/api.js";
import { requireAuth } from "/assets/js/core/guard.js";
import { resolveActivePlan } from "/assets/js/core/plans.js";
import { initPrivateShell } from "/assets/js/core/shell.js";
import { setSelectedPlanId } from "/assets/js/core/session.js";
import { escapeHtml, formatDate, formatHours, renderEmptyState, showStatus } from "/assets/js/core/ui.js";

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
                "Сначала соберите маршрут в кабинете. После этого здесь появится недельная раскладка."
            );
            weeksContainer.innerHTML = "";
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
                <div class="form-row" style="margin-top:14px;">
                    <label class="field-label" for="plan-select">Выберите план</label>
                    <select class="select" id="plan-select">
                        ${(page.items || []).map((item) => `
                            <option value="${item.id}" ${item.id === activePlanId ? "selected" : ""}>
                                ${escapeHtml(item.roleName || item.roleCode || `План ${item.id}`)} · ${escapeHtml(formatDate(item.createdAt))}
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
        summaryContainer.innerHTML = `
            <section class="panel-grid">
                <article class="card panel-card">
                    <p class="eyebrow">Недельный ритм</p>
                    <h3>${escapeHtml(plan.roleName || plan.roleCode || "Недельный план")}</h3>
                    <p>Ниже уже разложенные недели с лимитом ${escapeHtml(String(plan.params?.hoursPerWeek || "—"))} ч/нед. Сервис не дробит одну тему на несколько недель, поэтому шаги идут блоками.</p>
                </article>
                <article class="card panel-card">
                    <p class="eyebrow">Действия</p>
                    <h3>Переключение режимов</h3>
                    <div class="form-actions" style="margin-top:18px;">
                        <a class="button button-primary" href="/roadmap?planId=${plan.id}">Открыть roadmap</a>
                        <a class="button button-secondary" href="/dashboard">Вернуться в кабинет</a>
                    </div>
                </article>
            </section>
        `;
    }

    function renderWeeks(plan) {
        weeksContainer.innerHTML = `
            <div class="week-grid">
                ${plan.weeks.map((week) => {
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
                                            <span class="badge badge-dark">${escapeHtml(step.explanation?.ruleApplied || "AI")}</span>
                                        </div>
                                        <h4>${escapeHtml(step.topicTitle || step.topicCode || `Тема ${step.topicId}`)}</h4>
                                        <p>${escapeHtml(step.explanation?.topicPriorityReason || "Пояснение недоступно.")}</p>
                                    </article>
                                `).join("")}
                            </div>
                        </article>
                    `;
                }).join("")}
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
            : "Не удалось загрузить недельный план.";
        showStatus(statusBox, "error", message);
    }
}
