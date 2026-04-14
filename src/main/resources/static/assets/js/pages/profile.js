import { requireAuth } from "/assets/js/core/guard.js";
import { initPrivateShell } from "/assets/js/core/shell.js";
import { getPlanDraft, getTokenMeta, getUserProfile, savePlanDraft } from "/assets/js/core/session.js";
import { clearStatus, showStatus } from "/assets/js/core/ui.js";

if (requireAuth()) {
    initPrivateShell("profile");

    const profile = getUserProfile();
    const { accessPayload, refreshPayload } = getTokenMeta();
    const infoContainer = document.querySelector("#profile-info");
    const form = document.querySelector("#profile-preferences-form");
    const statusBox = document.querySelector("#profile-status");

    renderInfo();
    prefillForm();

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

        showStatus(statusBox, "success", "Локальные настройки сохранены. Они будут подставляться в форму генерации плана.");
    });

    function renderInfo() {
        infoContainer.innerHTML = `
            <div class="tile-grid">
                <article class="card status-card">
                    <p class="eyebrow">Пользователь</p>
                    <h3>${profile.displayName}</h3>
                    <p>${profile.email || "Email недоступен"}</p>
                </article>
                <article class="card status-card">
                    <p class="eyebrow">User ID</p>
                    <h3>${profile.userId || "—"}</h3>
                    <p>Берётся из JWT payload текущей сессии.</p>
                </article>
                <article class="card status-card">
                    <p class="eyebrow">Access exp</p>
                    <h3>${formatExpiry(accessPayload?.exp)}</h3>
                    <p>Frontend использует refresh flow автоматически.</p>
                </article>
                <article class="card status-card">
                    <p class="eyebrow">Refresh exp</p>
                    <h3>${formatExpiry(refreshPayload?.exp)}</h3>
                    <p>После истечения потребуется повторный вход.</p>
                </article>
            </div>
        `;
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
