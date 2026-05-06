import { ApiError, plansApi, usersApi } from "/assets/js/core/api.js";
import { requireAuth } from "/assets/js/core/guard.js";
import { initPrivateShell } from "/assets/js/core/shell.js";
import { getUserProfile } from "/assets/js/core/session.js";
import { escapeHtml, formatDate, renderEmptyState } from "/assets/js/core/ui.js";

if (requireAuth()) {
    initPrivateShell("profile");

    const infoContainer = document.querySelector("#profile-info");
    const plansContainer = document.querySelector("#profile-plans");

    const state = {
        profile: null,
        plans: []
    };

    loadProfilePage().catch((error) => renderProfileFallback(error));

    async function loadProfilePage() {
        const [profile, plansPage] = await Promise.all([
            usersApi.me(),
            plansApi.list(0, 16)
        ]);

        const plans = plansPage.items || [];
        state.profile = profile;
        state.plans = plans;
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
                            <button class="button button-danger" type="button" data-delete-plan-id="${plan.id}">Удалить</button>
                        </div>
                    </article>
                `).join("")}
            </div>
        `;

        bindDeletePlanActions();
    }

    function bindDeletePlanActions() {
        plansContainer.querySelectorAll("[data-delete-plan-id]").forEach((button) => {
            button.addEventListener("click", async () => {
                const planId = Number(button.dataset.deletePlanId);
                const plan = state.plans.find((item) => item.id === planId);
                const planName = plan?.roleName || "этот план";
                const confirmed = await confirmPlanDelete(planName);
                if (!confirmed) {
                    return;
                }

                button.disabled = true;
                button.textContent = "Удаляем...";

                try {
                    await plansApi.remove(planId);
                    await loadProfilePage();
                } catch (error) {
                    button.disabled = false;
                    button.textContent = "Удалить";
                    window.alert(error instanceof ApiError ? error.message : "Не удалось удалить план.");
                }
            });
        });
    }

    function confirmPlanDelete(planName) {
        return new Promise((resolve) => {
            const previousActiveElement = document.activeElement;
            const modal = document.createElement("div");
            modal.className = "app-confirm";
            modal.setAttribute("role", "dialog");
            modal.setAttribute("aria-modal", "true");
            modal.setAttribute("aria-labelledby", "delete-plan-title");
            modal.innerHTML = `
                <div class="app-confirm-card" role="document">
                    <div class="app-confirm-mark" aria-hidden="true">!</div>
                    <div class="app-confirm-copy">
                        <p class="eyebrow">Удаление плана</p>
                        <h3 id="delete-plan-title">Удалить план?</h3>
                        <p>План «${escapeHtml(planName)}» исчезнет из списка сохранённых планов. Прогресс по нему больше не будет отображаться.</p>
                    </div>
                    <div class="app-confirm-actions">
                        <button class="button button-ghost" type="button" data-confirm-cancel>Отмена</button>
                        <button class="button button-danger" type="button" data-confirm-delete>Удалить</button>
                    </div>
                </div>
            `;

            const close = (confirmed) => {
                document.removeEventListener("keydown", handleKeydown);
                document.body.classList.remove("has-modal");
                modal.remove();
                if (previousActiveElement instanceof HTMLElement) {
                    previousActiveElement.focus();
                }
                resolve(confirmed);
            };

            const handleKeydown = (event) => {
                if (event.key === "Escape") {
                    close(false);
                }
            };

            modal.addEventListener("click", (event) => {
                if (event.target === modal) {
                    close(false);
                }
            });
            modal.querySelector("[data-confirm-cancel]")?.addEventListener("click", () => close(false));
            modal.querySelector("[data-confirm-delete]")?.addEventListener("click", () => close(true));
            document.addEventListener("keydown", handleKeydown);
            document.body.append(modal);
            document.body.classList.add("has-modal");
            requestAnimationFrame(() => {
                modal.querySelector("[data-confirm-cancel]")?.focus();
            });
        });
    }

    function renderPlansError(error) {
        const message = error instanceof ApiError
            ? error.message
            : "Не удалось загрузить сохранённые планы.";
        renderEmptyState(plansContainer, "Планы временно недоступны", message);
    }
}
