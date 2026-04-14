import { authApi, ApiError } from "/assets/js/core/api.js";
import { redirectIfAuthenticated } from "/assets/js/core/guard.js";
import { clearStatus, showStatus } from "/assets/js/core/ui.js";

if (!redirectIfAuthenticated()) {
    const form = document.querySelector("#login-form");
    const statusBox = document.querySelector("#login-status");
    const submitButton = document.querySelector("#login-submit");

    form?.addEventListener("submit", async (event) => {
        event.preventDefault();
        clearStatus(statusBox);

        const formData = new FormData(form);
        const payload = {
            email: String(formData.get("email") || "").trim(),
            password: String(formData.get("password") || "")
        };

        submitButton.disabled = true;
        submitButton.textContent = "Входим...";

        try {
            await authApi.login(payload);
            showStatus(statusBox, "success", "Вход выполнен. Перенаправляем в кабинет.");
            window.setTimeout(() => {
                window.location.replace("/dashboard");
            }, 500);
        } catch (error) {
            const message = error instanceof ApiError
                ? error.message
                : "Не удалось выполнить вход. Попробуйте снова.";
            showStatus(statusBox, "error", message);
        } finally {
            submitButton.disabled = false;
            submitButton.textContent = "Войти";
        }
    });
}
