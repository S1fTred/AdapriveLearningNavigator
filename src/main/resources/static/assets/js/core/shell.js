import { clearSession, getUserProfile } from "/assets/js/core/session.js";

const SIDEBAR_STATE_KEY = "aln.sidebarCollapsed";

function applySidebarState(appShell, toggle, isCollapsed) {
    appShell.classList.toggle("is-sidebar-collapsed", isCollapsed);
    toggle.setAttribute("aria-expanded", String(!isCollapsed));
    toggle.setAttribute("aria-label", isCollapsed ? "Развернуть боковую панель" : "Свернуть боковую панель");
    toggle.setAttribute("title", isCollapsed ? "Развернуть боковую панель" : "Свернуть боковую панель");
    toggle.textContent = isCollapsed ? ">" : "<";
}

export function initPrivateShell(pageKey) {
    const profile = getUserProfile();
    const appShell = document.querySelector(".app-shell");
    const sidebarToggle = document.querySelector("[data-sidebar-toggle]");

    document.querySelectorAll("[data-nav]").forEach((link) => {
        const isActive = link.dataset.nav === pageKey;
        link.classList.toggle("is-active", isActive);
        link.setAttribute("title", link.textContent.trim());
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
        button.setAttribute("title", "Выйти");
        button.addEventListener("click", () => {
            clearSession();
            window.location.replace("/");
        });
    });

    if (appShell && sidebarToggle) {
        const isCollapsed = window.localStorage.getItem(SIDEBAR_STATE_KEY) === "true";
        applySidebarState(appShell, sidebarToggle, isCollapsed);

        sidebarToggle.addEventListener("click", () => {
            const nextState = !appShell.classList.contains("is-sidebar-collapsed");
            applySidebarState(appShell, sidebarToggle, nextState);
            window.localStorage.setItem(SIDEBAR_STATE_KEY, String(nextState));
        });
    }
}
