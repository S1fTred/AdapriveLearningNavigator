import { ApiError } from "/assets/js/core/api.js";
import { requireAuth } from "/assets/js/core/guard.js";
import { resolveActivePlan } from "/assets/js/core/plans.js";
import { initPrivateShell } from "/assets/js/core/shell.js";
import { getProgressState, setSelectedPlanId, setTopicProgress } from "/assets/js/core/session.js";
import { escapeHtml, flattenPlanSteps, renderEmptyState, showStatus } from "/assets/js/core/ui.js";

const STATUS_LABELS = {
    NOT_STARTED: "Не начато",
    IN_PROGRESS: "В процессе",
    DONE: "Готово"
};

if (requireAuth()) {
    initPrivateShell("progress");

    const statusBox = document.querySelector("#progress-status");
    const summaryContainer = document.querySelector("#progress-summary");
    const trackerContainer = document.querySelector("#progress-tracker");

    loadPage().catch((error) => handleError(error));

    async function loadPage() {
        const explicitPlanId = Number(new URLSearchParams(window.location.search).get("planId")) || null;
        const plan = await resolveActivePlan(explicitPlanId);

        if (!plan) {
            renderEmptyState(
                summaryContainer,
                "Пока нечего отмечать",
                "Сначала создайте план в кабинете. После этого здесь появится локальный трекер прогресса по темам."
            );
            trackerContainer.innerHTML = "";
            return;
        }

        setSelectedPlanId(plan.id);
        renderTracker(plan);
    }

    function renderTracker(plan) {
        const steps = flattenPlanSteps(plan);
        const progress = getProgressState(plan.id);
        const doneCount = steps.filter((step) => progress[String(step.topicId)] === "DONE").length;
        const percent = steps.length ? Math.round((doneCount / steps.length) * 100) : 0;

        summaryContainer.innerHTML = `
            <section class="panel-grid">
                <article class="card panel-card">
                    <p class="eyebrow">Локальный трекер</p>
                    <h3>${escapeHtml(plan.roleName || plan.roleCode || "Прогресс по плану")}</h3>
                    <p>Это клиентский слой поверх текущего backend. Статусы ниже сохраняются локально в браузере, чтобы не терять фокус между сессиями.</p>
                </article>
                <article class="card panel-card">
                    <p class="eyebrow">Готовность</p>
                    <h3>${percent}% завершено</h3>
                    <div class="progress-bar" style="margin-top:16px;"><span style="width:${percent}%;"></span></div>
                    <p style="margin-top:12px;">Закрыто ${doneCount} из ${steps.length} тем.</p>
                </article>
            </section>
        `;

        trackerContainer.innerHTML = `
            <div class="list">
                ${steps.map((step) => `
                    <article class="list-item">
                        <div>
                            <h4>${escapeHtml(step.topicTitle || step.topicCode || `Тема ${step.topicId}`)}</h4>
                            <p>Неделя ${step.weekIndex}. ${escapeHtml(step.explanation?.topicPriorityReason || "Причина выбора недоступна.")}</p>
                        </div>
                        <div class="form-row" style="min-width:200px;">
                            <label class="field-label" for="progress-${step.topicId}">Статус</label>
                            <select class="select" id="progress-${step.topicId}" data-progress-topic="${step.topicId}">
                                ${Object.entries(STATUS_LABELS).map(([value, label]) => `
                                    <option value="${value}" ${(progress[String(step.topicId)] || "NOT_STARTED") === value ? "selected" : ""}>
                                        ${label}
                                    </option>
                                `).join("")}
                            </select>
                        </div>
                    </article>
                `).join("")}
            </div>
        `;

        trackerContainer.querySelectorAll("[data-progress-topic]").forEach((select) => {
            select.addEventListener("change", () => {
                const topicId = Number(select.dataset.progressTopic);
                setTopicProgress(plan.id, topicId, select.value);
                renderTracker(plan);
                showStatus(statusBox, "success", "Статус темы обновлён локально в браузере.");
            });
        });
    }

    function handleError(error) {
        if (error?.status === 401) {
            window.location.replace("/login");
            return;
        }
        const message = error instanceof ApiError
            ? error.message
            : "Не удалось загрузить трекер прогресса.";
        showStatus(statusBox, "error", message);
    }
}
