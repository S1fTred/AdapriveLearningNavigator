import { ApiError, plansApi } from "/assets/js/core/api.js";
import { requireAuth } from "/assets/js/core/guard.js";
import { resolveActivePlan, summarizePlan } from "/assets/js/core/plans.js";
import { initPrivateShell } from "/assets/js/core/shell.js";
import { setSelectedPlanId } from "/assets/js/core/session.js";
import { escapeHtml, formatDate, formatHours, renderEmptyState, showStatus } from "/assets/js/core/ui.js";

if (requireAuth()) {
    initPrivateShell("roadmap");

    const statusBox = document.querySelector("#roadmap-status");
    const metaContainer = document.querySelector("#roadmap-meta");
    const flowContainer = document.querySelector("#roadmap-flow");
    const switcherContainer = document.querySelector("#roadmap-switcher");

    loadPage().catch((error) => handleError(error));

    async function loadPage() {
        const explicitPlanId = Number(new URLSearchParams(window.location.search).get("planId")) || null;
        const plan = await resolveActivePlan(explicitPlanId);

        if (!plan) {
            renderEmptyState(
                metaContainer,
                "Нет плана для отображения",
                "Сначала соберите маршрут в личном кабинете, затем возвращайтесь к roadmap."
            );
            flowContainer.innerHTML = "";
            return;
        }

        renderMeta(plan);
        renderFlow(plan);
        await renderSwitcher(plan.id);
    }

    async function renderSwitcher(activePlanId) {
        const page = await plansApi.list(0, 20);
        const options = (page.items || []).map((item) => `
            <option value="${item.id}" ${item.id === activePlanId ? "selected" : ""}>
                ${escapeHtml(item.roleName || item.roleCode || `План ${item.id}`)} · ${escapeHtml(formatDate(item.createdAt))}
            </option>
        `).join("");

        switcherContainer.innerHTML = `
            <div class="card panel-card">
                <p class="eyebrow">Навигация</p>
                <h3>Переключить маршрут</h3>
                <div class="form-row" style="margin-top:14px;">
                    <label class="field-label" for="roadmap-plan-select">Выберите план</label>
                    <select class="select" id="roadmap-plan-select">${options}</select>
                </div>
            </div>
        `;

        switcherContainer.querySelector("#roadmap-plan-select")?.addEventListener("change", (event) => {
            const planId = Number(event.target.value);
            setSelectedPlanId(planId);
            window.location.href = `/roadmap?planId=${planId}`;
        });
    }

    function renderMeta(plan) {
        const summary = summarizePlan(plan);
        metaContainer.innerHTML = `
            <section class="panel-grid">
                <article class="card panel-card">
                    <p class="eyebrow">Текущий маршрут</p>
                    <h3>${escapeHtml(plan.roleName || plan.roleCode || "Roadmap")}</h3>
                    <p>Построен ${escapeHtml(formatDate(plan.createdAt))}. Все узлы ниже уже проверены backend на required coverage, prereq-порядок и недельный лимит.</p>
                    <div class="pill-row" style="margin-top:16px;">
                        <span class="badge">${summary.stepCount} тем</span>
                        <span class="badge badge-dark">${summary.weekCount} недель</span>
                        <span class="badge badge-success">${formatHours(summary.totalHours)}</span>
                    </div>
                </article>
                <article class="card panel-card">
                    <p class="eyebrow">Контекст</p>
                    <h3>Как читать эту карту</h3>
                    <p>Каждая карточка ниже отражает шаг внутри недельного плана. Порядок уже согласован с обязательными пререквизитами и ограничением по часам.</p>
                    <div class="form-actions" style="margin-top:18px;">
                        <a class="button button-primary" href="/plan?planId=${plan.id}">Перейти к неделям</a>
                        <a class="button button-secondary" href="/dashboard">Назад в кабинет</a>
                    </div>
                </article>
            </section>
        `;
    }

    function renderFlow(plan) {
        flowContainer.innerHTML = `
            <div class="roadmap-flow">
                ${plan.weeks.map((week) => `
                    <section class="roadmap-week">
                        <div class="roadmap-marker"></div>
                        <article class="roadmap-week-card">
                            <div class="roadmap-week-head">
                                <div>
                                    <p class="eyebrow">Неделя ${week.weekIndex}</p>
                                    <h3>${escapeHtml(formatHours(week.hoursPlanned))} из ${escapeHtml(formatHours(week.hoursBudget))}</h3>
                                </div>
                                <span class="badge">${week.steps.length} шага</span>
                            </div>
                            <div class="step-stack">
                                ${week.steps.map((step) => `
                                    <article class="step-card">
                                        <div class="step-meta">
                                            <span class="badge badge-dark">${escapeHtml(step.topicCode || `TOPIC-${step.topicId}`)}</span>
                                            <span class="badge">${escapeHtml(formatHours(step.plannedHours))}</span>
                                        </div>
                                        <h4>${escapeHtml(step.topicTitle || `Тема ${step.topicId}`)}</h4>
                                        <p>${escapeHtml(step.explanation?.topicPriorityReason || "Причина выбора недоступна.")}</p>
                                        ${(step.explanation?.prereqs || []).length ? `
                                            <div class="topic-tags" style="margin-top:14px;">
                                                ${(step.explanation.prereqs || []).map((prereq) => `
                                                    <span class="topic-chip ${prereq.status === "DONE" ? "" : "muted"}">
                                                        ${escapeHtml(prereq.prereqTopicTitle || prereq.prereqTopicCode || `Тема ${prereq.prereqTopicId}`)}
                                                        · ${escapeHtml(prereq.status)}
                                                    </span>
                                                `).join("")}
                                            </div>
                                        ` : ""}
                                    </article>
                                `).join("")}
                            </div>
                        </article>
                    </section>
                `).join("")}
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
