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

export function annotatePlanForDisplay(plan) {
    if (!plan?.weeks?.length) {
        return {
            ...(plan || {}),
            weeks: []
        };
    }

    const annotatedWeeks = (plan.weeks || []).map((week) => ({
        ...week,
        steps: (week.steps || []).map((step) => ({
            ...step,
            displayTitle: step.topicTitle || "Тема без названия",
            partIndex: null,
            partCount: 1,
            partLabel: ""
        }))
    }));

    let run = [];

    const flushRun = () => {
        if (!run.length) {
            return;
        }

        const partCount = run.length;
        run.forEach((step, index) => {
            step.partCount = partCount;
            if (partCount > 1) {
                step.partIndex = index + 1;
                step.partLabel = `Часть ${index + 1} из ${partCount}`;
            }
        });

        run = [];
    };

    for (const week of annotatedWeeks) {
        for (const step of week.steps) {
            if (!run.length || run[run.length - 1].topicId === step.topicId) {
                run.push(step);
                continue;
            }

            flushRun();
            run.push(step);
        }
    }

    flushRun();

    return {
        ...plan,
        weeks: annotatedWeeks
    };
}

export function formatPlanStepTitle(step) {
    return step?.displayTitle || step?.topicTitle || "Тема без названия";
}

export function formatPlanStepContinuation(step) {
    const partIndex = Number(step?.partIndex || 0);
    const partCount = Number(step?.partCount || 1);

    if (partCount <= 1) {
        return "";
    }

    if (partIndex <= 1) {
        return "Старт темы";
    }

    return "Продолжение темы";
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
