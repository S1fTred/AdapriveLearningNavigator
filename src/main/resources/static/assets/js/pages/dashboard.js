import { ApiError, plansApi } from "/assets/js/core/api.js";
import { requireAuth } from "/assets/js/core/guard.js";
import { resolveActivePlan, summarizePlan } from "/assets/js/core/plans.js";
import { initPrivateShell } from "/assets/js/core/shell.js";
import { getPlanDraft, savePlanDraft, setSelectedPlanId } from "/assets/js/core/session.js";
import { clearStatus, escapeHtml, flattenPlanSteps, formatDate, formatHours, renderEmptyState, showStatus } from "/assets/js/core/ui.js";

if (requireAuth()) {
    initPrivateShell("dashboard");

    const form = document.querySelector("#generate-plan-form");
    const statusBox = document.querySelector("#dashboard-status");
    const submitButton = document.querySelector("#generate-submit");
    const plansContainer = document.querySelector("#plans-list");
    const spotlightContainer = document.querySelector("#plan-spotlight");

    prefillDraft();
    loadDashboard().catch((error) => handleError(error, statusBox));

    form?.addEventListener("submit", async (event) => {
        event.preventDefault();
        clearStatus(statusBox);

        const payload = readPlanPayload();
        savePlanDraft({
            goal: payload.goal,
            currentLevel: payload.currentLevel,
            hoursPerWeek: payload.hoursPerWeek,
            knownTopics: payload.knownTopics.join(", ")
        });

        submitButton.disabled = true;
        submitButton.textContent = "Строим маршрут...";
        showStatus(statusBox, "info", "Отправляем запрос в AI-модуль и собираем план.");

        try {
            const plan = await plansApi.generate(payload);
            renderSpotlight(plan);
            await renderPlansList();
            showStatus(statusBox, "success", "План построен. Можно открыть roadmap и недельный план.");
        } catch (error) {
            handleError(error, statusBox);
        } finally {
            submitButton.disabled = false;
            submitButton.textContent = "Построить маршрут";
        }
    });

    function prefillDraft() {
        const draft = getPlanDraft();
        const goalField = form?.querySelector("[name=goal]");
        if (goalField) {
            goalField.value = draft.goal || "";
        }
        const levelSelect = form?.querySelector("[name=currentLevel]");
        if (levelSelect) {
            levelSelect.value = draft.currentLevel || "BEGINNER";
        }
        const hoursField = form?.querySelector("[name=hoursPerWeek]");
        if (hoursField) {
            hoursField.value = draft.hoursPerWeek || 10;
        }
        const knownTopicsField = form?.querySelector("[name=knownTopics]");
        if (knownTopicsField) {
            knownTopicsField.value = draft.knownTopics || "";
        }
    }

    async function loadDashboard() {
        await renderPlansList();
        const activePlan = await resolveActivePlan();
        if (activePlan) {
            renderSpotlight(activePlan);
        } else {
            renderEmptyState(
                spotlightContainer,
                "Пока нет плана",
                "Соберите первый маршрут по цели, уровню и доступному времени. После этого roadmap и недельный план появятся автоматически."
            );
        }
    }

    async function renderPlansList() {
        const page = await plansApi.list(0, 12);
        const items = page.items || [];

        if (!items.length) {
            renderEmptyState(
                plansContainer,
                "История пока пуста",
                "Здесь появятся последние сгенерированные планы. Начните с формы слева."
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
                            <div class="pill-row" style="margin-top:10px;">
                                <span class="badge">${escapeHtml(plan.scenarioType || "BASE")}</span>
                                <span class="badge badge-dark">${escapeHtml(plan.status || "DRAFT")}</span>
                            </div>
                        </div>
                        <div class="list-actions">
                            <a class="button button-secondary" href="/roadmap?planId=${plan.id}">Roadmap</a>
                            <a class="button button-ghost" href="/plan?planId=${plan.id}">Недели</a>
                            <button class="button button-ghost" data-open-plan="${plan.id}">Выбрать</button>
                        </div>
                    </article>
                `).join("")}
            </div>
        `;

        plansContainer.querySelectorAll("[data-open-plan]").forEach((button) => {
            button.addEventListener("click", async () => {
                const planId = Number(button.dataset.openPlan);
                setSelectedPlanId(planId);
                const plan = await plansApi.get(planId);
                renderSpotlight(plan);
            });
        });
    }

    function renderSpotlight(plan) {
        const summary = summarizePlan(plan);
        const nextSteps = flattenPlanSteps(plan).slice(0, 3);

        spotlightContainer.innerHTML = `
            <section class="hero-panel">
                <div class="card panel-card">
                    <p class="eyebrow">Активный маршрут</p>
                    <h3>${escapeHtml(plan.roleName || plan.roleCode || "Персональный план")}</h3>
                    <p>Маршрут создан ${escapeHtml(formatDate(plan.createdAt))}. AI-версия: ${escapeHtml(plan.params?.algoVersion || "—")}.</p>
                    <div class="metric-grid" style="margin-top:20px;">
                        <article class="metric-card card">
                            <p>Недель</p>
                            <strong>${summary.weekCount}</strong>
                            <p>Разложено по недельному лимиту.</p>
                        </article>
                        <article class="metric-card card">
                            <p>Тем</p>
                            <strong>${summary.stepCount}</strong>
                            <p>Шагов внутри текущего маршрута.</p>
                        </article>
                        <article class="metric-card card">
                            <p>Часов</p>
                            <strong>${summary.totalHours}</strong>
                            <p>Суммарная учебная нагрузка.</p>
                        </article>
                        <article class="metric-card card">
                            <p>Лимит</p>
                            <strong>${escapeHtml(String(plan.params?.hoursPerWeek || "—"))}</strong>
                            <p>Часов в неделю по вашему сценарию.</p>
                        </article>
                    </div>
                </div>
                <div class="card panel-card plan-highlight">
                    <p class="eyebrow">Ближайшие шаги</p>
                    <h3>Что делать дальше</h3>
                    <div class="list">
                        ${nextSteps.map((step) => `
                            <article class="list-item">
                                <div>
                                    <h4>${escapeHtml(step.topicTitle || step.topicCode || `Тема ${step.topicId}`)}</h4>
                                    <p>Неделя ${step.weekIndex}, ${escapeHtml(formatHours(step.plannedHours))}</p>
                                </div>
                                <span class="badge">${escapeHtml(step.explanation?.ruleApplied || "AI")}</span>
                            </article>
                        `).join("")}
                    </div>
                    <div class="form-actions">
                        <a class="button button-primary" href="/roadmap?planId=${plan.id}">Открыть roadmap</a>
                        <a class="button button-secondary" href="/plan?planId=${plan.id}">Открыть недельный план</a>
                    </div>
                </div>
            </section>
        `;
    }

    function readPlanPayload() {
        const formData = new FormData(form);
        const knownTopics = String(formData.get("knownTopics") || "")
            .split(/[\n,]/)
            .map((item) => item.trim())
            .filter(Boolean);

        return {
            goal: String(formData.get("goal") || "").trim(),
            currentLevel: String(formData.get("currentLevel") || "BEGINNER"),
            hoursPerWeek: Number(formData.get("hoursPerWeek") || 10),
            knownTopics
        };
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
