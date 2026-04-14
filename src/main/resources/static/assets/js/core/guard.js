import { clearSession, getUserProfile, hasSession } from "/assets/js/core/session.js";

export function requireAuth() {
    if (!hasSession()) {
        window.location.replace("/login");
        return false;
    }
    return true;
}

export function redirectIfAuthenticated() {
    if (hasSession()) {
        window.location.replace("/dashboard");
        return true;
    }
    return false;
}

export function handleFatalAuth(error, fallbackMessageElement) {
    if (error?.status === 401) {
        clearSession();
        window.location.replace("/login");
        return true;
    }

    if (fallbackMessageElement) {
        fallbackMessageElement.textContent = `${getUserProfile().displayName}: не удалось проверить сессию.`;
    }

    return false;
}
