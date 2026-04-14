import { authApi, ApiError } from "/assets/js/core/api.js";
import { redirectIfAuthenticated } from "/assets/js/core/guard.js";
import { clearStatus, showStatus } from "/assets/js/core/ui.js";

if (!redirectIfAuthenticated()) {
    const form = document.querySelector("#register-form");
    const statusBox = document.querySelector("#register-status");
    const submitButton = document.querySelector("#register-submit");

    form?.addEventListener("submit", async (event) => {
        event.preventDefault();
        clearStatus(statusBox);

        const formData = new FormData(form);
        const payload = {
            displayName: String(formData.get("displayName") || "").trim(),
            email: String(formData.get("email") || "").trim(),
            password: String(formData.get("password") || "")
        };

        submitButton.disabled = true;
        submitButton.textContent = "Создаём аккаунт...";

        try {
            await authApi.register(payload);
            showStatus(statusBox, "success", "Аккаунт создан. Перенаправляем в кабинет.");
            window.setTimeout(() => {
                window.location.replace("/dashboard");
            }, 500);
        } catch (error) {
            const message = error instanceof ApiError
                ? error.message
                : "Не удалось создать аккаунт. Попробуйте снова.";
            showStatus(statusBox, "error", message);
        } finally {
            submitButton.disabled = false;
            submitButton.textContent = "Создать аккаунт";
        }
    });
}
