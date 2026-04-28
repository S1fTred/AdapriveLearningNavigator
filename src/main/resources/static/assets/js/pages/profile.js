import { ApiError, plansApi } from "/assets/js/core/api.js";
import { requireAuth } from "/assets/js/core/guard.js";
import { initPrivateShell } from "/assets/js/core/shell.js";
import { getUserProfile } from "/assets/js/core/session.js";
import { escapeHtml, formatDate, renderEmptyState } from "/assets/js/core/ui.js";

if (requireAuth()) {
    initPrivateShell("profile");

    const profile = getUserProfile();
    const infoContainer = document.querySelector("#profile-info");
    const plansContainer = document.querySelector("#profile-plans");

    renderInfo();
    loadSavedPlans().catch((error) => renderPlansError(error));

    function renderInfo() {
        infoContainer.innerHTML = `
            <div class="tile-grid">
                <article class="card status-card">
                    <p class="eyebrow">Аккаунт</p>
                    <h3>${escapeHtml(profile.displayName || "Пользователь")}</h3>
                    <p>${escapeHtml(profile.email || "Email не указан")}</p>
                </article>
                <article class="card status-card">
                    <p class="eyebrow">Мои планы</p>
                    <h3>История</h3>
                    <p>Сохранённые недельные планы доступны ниже на этой странице.</p>
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
                            <a class="button button-secondary" href="/dashboard?roadmapId=${plan.roleId}&planId=${plan.id}">Каталог</a>
                            <a class="button button-ghost" href="/roadmap?roadmapId=${plan.roleId}&planId=${plan.id}">Карта</a>
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
}
