import { hasSession } from "/assets/js/core/session.js";

const ctaPrimary = document.querySelector("[data-hero-primary]");
const ctaSecondary = document.querySelector("[data-hero-secondary]");
const authSlot = document.querySelector("[data-header-auth]");

if (hasSession()) {
    if (ctaPrimary) {
        ctaPrimary.textContent = "Открыть каталог направлений";
        ctaPrimary.setAttribute("href", "/dashboard");
    }
    if (ctaSecondary) {
        ctaSecondary.textContent = "Перейти к roadmap";
        ctaSecondary.setAttribute("href", "/roadmap");
    }
    if (authSlot) {
        authSlot.innerHTML = `
            <a class="button button-secondary" href="/dashboard">Кабинет</a>
        `;
    }
}
