import { plansApi } from "/assets/js/core/api.js";
import { cachePlan, getCachedPlan, getSelectedPlanId, setSelectedPlanId } from "/assets/js/core/session.js";

export async function loadPlanList() {
    return plansApi.list(0, 20);
}

export async function resolveActivePlan(explicitPlanId = null) {
    if (explicitPlanId) {
        setSelectedPlanId(explicitPlanId);
    }

    const selectedPlanId = explicitPlanId || getSelectedPlanId();
    if (selectedPlanId) {
        const cached = getCachedPlan(selectedPlanId);
        if (cached) {
            return cached;
        }
    }

    const page = await loadPlanList();
    const firstPlan = page.items?.[0];
    if (!firstPlan) {
        return null;
    }

    setSelectedPlanId(firstPlan.id);
    const plan = await plansApi.get(firstPlan.id);
    cachePlan(plan);
    return plan;
}

export function summarizePlan(plan) {
    const weeks = plan?.weeks || [];
    const steps = weeks.flatMap((week) => week.steps || []);
    const totalHours = steps.reduce((sum, step) => sum + Number(step.plannedHours || 0), 0);

    return {
        weekCount: weeks.length,
        stepCount: steps.length,
        totalHours
    };
}
