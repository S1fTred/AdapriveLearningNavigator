import { clearSession, getUserProfile } from "/assets/js/core/session.js";

export function initPrivateShell(pageKey) {
    const profile = getUserProfile();

    document.querySelectorAll("[data-nav]").forEach((link) => {
        const isActive = link.dataset.nav === pageKey;
        link.classList.toggle("is-active", isActive);
    });

    const userName = document.querySelector("[data-user-name]");
    const userEmail = document.querySelector("[data-user-email]");
    if (userName) {
        userName.textContent = profile.displayName || "Пользователь";
    }
    if (userEmail) {
        userEmail.textContent = profile.email || "Локальная сессия";
    }

    document.querySelectorAll("[data-logout]").forEach((button) => {
        button.addEventListener("click", () => {
            clearSession();
            window.location.replace("/");
        });
    });
}
