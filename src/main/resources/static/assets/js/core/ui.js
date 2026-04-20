export function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll("\"", "&quot;")
        .replaceAll("'", "&#39;");
}

export function formatDate(value) {
    if (!value) {
        return "—";
    }

    return new Intl.DateTimeFormat("ru-RU", {
        dateStyle: "medium",
        timeStyle: "short"
    }).format(new Date(value));
}

export function formatHours(value) {
    const numeric = Number(value ?? 0);
    if (Number.isNaN(numeric)) {
        return "0 ч";
    }

    return `${numeric % 1 === 0 ? numeric.toFixed(0) : numeric.toFixed(1)} ч`;
}

export function showStatus(element, type, message) {
    if (!element) {
        return;
    }

    element.className = `status-box is-visible ${type}`;
    element.textContent = message;
}

export function clearStatus(element) {
    if (!element) {
        return;
    }
    element.className = "status-box";
    element.textContent = "";
}

export function renderEmptyState(container, title, message, actionHtml = "") {
    container.innerHTML = `
        <div class="empty-state">
            <h3>${escapeHtml(title)}</h3>
            <p>${escapeHtml(message)}</p>
            ${actionHtml}
        </div>
    `;
}

export function flattenPlanSteps(plan) {
    return (plan?.weeks || []).flatMap((week) =>
        (week.steps || []).map((step) => ({
            ...step,
            weekIndex: week.weekIndex,
            hoursBudget: week.hoursBudget,
            hoursPlanned: week.hoursPlanned
        }))
    );
}

export function formatRuleLabel(rule) {
    switch (rule) {
        case "AI_LED_ROUTE_BACKEND_VALIDATED":
            return "Подтверждено системой";
        case "KB_REQUIRED_PREREQ_ADDED":
            return "Добавлено по зависимостям";
        case "ROADMAP":
            return "Roadmap";
        default:
            return rule || "Roadmap";
    }
}
