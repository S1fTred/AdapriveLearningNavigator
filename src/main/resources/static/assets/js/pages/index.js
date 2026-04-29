import { hasSession } from "/assets/js/core/session.js";

const ctaPrimary = document.querySelector("[data-hero-primary]");
const authSlot = document.querySelector("[data-header-auth]");

if (hasSession()) {
    if (ctaPrimary) {
        ctaPrimary.textContent = "Открыть каталог направлений";
        ctaPrimary.setAttribute("href", "/dashboard");
    }
    if (authSlot) {
        authSlot.innerHTML = "";
    }
}
