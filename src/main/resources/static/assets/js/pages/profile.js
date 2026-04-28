import { ApiError, plansApi, usersApi } from "/assets/js/core/api.js";
import { requireAuth } from "/assets/js/core/guard.js";
import { initPrivateShell } from "/assets/js/core/shell.js";
import { getUserProfile } from "/assets/js/core/session.js";
import { escapeHtml, formatDate, renderEmptyState } from "/assets/js/core/ui.js";

if (requireAuth()) {
    initPrivateShell("profile");

    const infoContainer = document.querySelector("#profile-info");
    const plansContainer = document.querySelector("#profile-plans");

    loadProfilePage().catch((error) => renderProfileFallback(error));

    async function loadProfilePage() {
        const [profile, plansPage] = await Promise.all([
            usersApi.me(),
            plansApi.list(0, 16)
        ]);

        const plans = plansPage.items || [];
        renderInfo(profile, plans);
        renderSavedPlans(plans);
    }

    function renderProfileFallback(error) {
        const localProfile = getUserProfile();
        renderInfo(localProfile, []);
        renderPlansError(error);
    }

    function renderInfo(profile, plans) {
        const createdAt = profile.createdAt ? formatDate(profile.createdAt) : "Дата пока не указана";
        const plansCount = plans.length;
        const lastPlan = plans[0];
        const lastPlanText = lastPlan
            ? `${lastPlan.roleName || "План обучения"} · ${formatDate(lastPlan.createdAt)}`
            : "Пока нет сохранённых планов";

        infoContainer.innerHTML = `
            <div class="tile-grid">
                <article class="card status-card">
                    <p class="eyebrow">Аккаунт</p>
                    <h3>${escapeHtml(profile.displayName || "Пользователь")}</h3>
                    <p>${escapeHtml(profile.email || "Email не указан")}</p>
                </article>
                <article class="card status-card">
                    <p class="eyebrow">Дата регистрации</p>
                    <h3>${escapeHtml(createdAt)}</h3>
                    <p>С этого момента профиль хранит вашу историю планов.</p>
                </article>
                <article class="card status-card">
                    <p class="eyebrow">Сохранённые планы</p>
                    <h3>${plansCount}</h3>
                    <p>${escapeHtml(lastPlanText)}</p>
                </article>
            </div>
        `;
    }

    function renderSavedPlans(items) {
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
