import { ApiError, plansApi } from "/assets/js/core/api.js";
import { requireAuth } from "/assets/js/core/guard.js";
import { initPrivateShell } from "/assets/js/core/shell.js";
import { getPlanDraft, getTokenMeta, getUserProfile, savePlanDraft } from "/assets/js/core/session.js";
import { clearStatus, escapeHtml, formatDate, renderEmptyState, showStatus } from "/assets/js/core/ui.js";

if (requireAuth()) {
    initPrivateShell("profile");

    const profile = getUserProfile();
    const { accessPayload, refreshPayload } = getTokenMeta();
    const infoContainer = document.querySelector("#profile-info");
    const plansContainer = document.querySelector("#profile-plans");
    const form = document.querySelector("#profile-preferences-form");
    const statusBox = document.querySelector("#profile-status");

    renderInfo();
    prefillForm();
    loadSavedPlans().catch((error) => renderPlansError(error));

    form?.addEventListener("submit", (event) => {
        event.preventDefault();
        clearStatus(statusBox);

        const formData = new FormData(form);
        savePlanDraft({
            goal: String(formData.get("goal") || "").trim(),
            currentLevel: String(formData.get("currentLevel") || "BEGINNER"),
            hoursPerWeek: Number(formData.get("hoursPerWeek") || 10),
            knownTopics: String(formData.get("knownTopics") || "").trim()
        });

        showStatus(statusBox, "success", "Локальные настройки сохранены. Они будут подставляться в сценарии работы с каталогом.");
    });

    function renderInfo() {
        infoContainer.innerHTML = `
            <div class="tile-grid">
                <article class="card status-card">
                    <p class="eyebrow">Пользователь</p>
                    <h3>${escapeHtml(profile.displayName)}</h3>
                    <p>${escapeHtml(profile.email || "Email недоступен")}</p>
                </article>
                <article class="card status-card">
                    <p class="eyebrow">User ID</p>
                    <h3>${escapeHtml(profile.userId || "—")}</h3>
                    <p>Берётся из JWT payload текущей сессии.</p>
                </article>
                <article class="card status-card">
                    <p class="eyebrow">Access exp</p>
                    <h3>${escapeHtml(formatExpiry(accessPayload?.exp))}</h3>
                    <p>Frontend использует refresh flow автоматически.</p>
                </article>
                <article class="card status-card">
                    <p class="eyebrow">Refresh exp</p>
                    <h3>${escapeHtml(formatExpiry(refreshPayload?.exp))}</h3>
                    <p>После истечения потребуется повторный вход.</p>
                </article>
            </div>
        `;
    }

    async function loadSavedPlans() {
        const page = await plansApi.list(0, 16);
        const items = page.items || [];

        if (!items.length) {
            renderEmptyState(
                plansContainer,
                "Планы пока не созданы",
                "После первого построения weekly plan здесь появится история сохранённых планов."
            );
            return;
        }

        plansContainer.innerHTML = `
            <div class="list">
                ${items.map((plan) => `
                    <article class="list-item">
                        <div class="list-item-copy">
                            <h4>${escapeHtml(plan.roleName || "План обучения")}</h4>
                            <p>Создан: ${escapeHtml(formatDate(plan.createdAt))}</p>
                            <div class="pill-row roadmap-card-pills">
                                <span class="badge">${escapeHtml(plan.scenarioType || "BASE")}</span>
                                <span class="badge badge-dark">${escapeHtml(plan.status || "DRAFT")}</span>
                            </div>
                        </div>
                        <div class="list-actions">
                            <a class="button button-secondary" href="/dashboard?roadmapId=${plan.roleId}&planId=${plan.id}#catalog-roadmap">Каталог</a>
                            <a class="button button-ghost" href="/plan?planId=${plan.id}">План</a>
                        </div>
                    </article>
                `).join("")}
            </div>
        `;
    }

    function renderPlansError(error) {
        const message = error instanceof ApiError
            ? error.message
            : "Не удалось загрузить сохранённые планы.";
        renderEmptyState(plansContainer, "Планы временно недоступны", message);
    }

    function prefillForm() {
        const draft = getPlanDraft();
        const goalField = form?.querySelector("[name=goal]");
        if (goalField) {
            goalField.value = draft.goal || "";
        }
        const hoursField = form?.querySelector("[name=hoursPerWeek]");
        if (hoursField) {
            hoursField.value = draft.hoursPerWeek || 10;
        }
        const levelSelect = form?.querySelector("[name=currentLevel]");
        if (levelSelect) {
            levelSelect.value = draft.currentLevel || "BEGINNER";
        }
        const knownTopicsField = form?.querySelector("[name=knownTopics]");
        if (knownTopicsField) {
            knownTopicsField.value = draft.knownTopics || "";
        }
    }

    function formatExpiry(epochSeconds) {
        if (!epochSeconds) {
            return "—";
        }
        return new Intl.DateTimeFormat("ru-RU", {
            dateStyle: "medium",
            timeStyle: "short"
        }).format(new Date(epochSeconds * 1000));
    }
}
