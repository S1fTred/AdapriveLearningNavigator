const STORAGE_KEYS = {
    accessToken: "aln.accessToken",
    refreshToken: "aln.refreshToken",
    userProfile: "aln.userProfile",
    selectedPlanId: "aln.selectedPlanId",
    cachedPlans: "aln.cachedPlans",
    planDraft: "aln.planDraft",
    progressByPlan: "aln.progressByPlan"
};

function readJson(key, fallback) {
    try {
        const value = localStorage.getItem(key);
        return value ? JSON.parse(value) : fallback;
    } catch {
        return fallback;
    }
}

function writeJson(key, value) {
    localStorage.setItem(key, JSON.stringify(value));
}

function decodeTokenPayload(token) {
    try {
        const [, payload] = token.split(".");
        if (!payload) {
            return null;
        }

        const normalized = payload.replace(/-/g, "+").replace(/_/g, "/");
        const decoded = atob(normalized);
        return JSON.parse(decoded);
    } catch {
        return null;
    }
}

export function getAccessToken() {
    return localStorage.getItem(STORAGE_KEYS.accessToken);
}

export function getRefreshToken() {
    return localStorage.getItem(STORAGE_KEYS.refreshToken);
}

export function hasSession() {
    return Boolean(getAccessToken() || getRefreshToken());
}

export function setAuthTokens(accessToken, refreshToken) {
    localStorage.setItem(STORAGE_KEYS.accessToken, accessToken);
    localStorage.setItem(STORAGE_KEYS.refreshToken, refreshToken);
}

export function clearSession() {
    localStorage.removeItem(STORAGE_KEYS.accessToken);
    localStorage.removeItem(STORAGE_KEYS.refreshToken);
    localStorage.removeItem(STORAGE_KEYS.selectedPlanId);
}

export function saveUserProfile(profile) {
    const current = getUserProfile();
    writeJson(STORAGE_KEYS.userProfile, {
        ...current,
        ...profile
    });
}

export function getUserProfile() {
    const explicitProfile = readJson(STORAGE_KEYS.userProfile, {});
    const tokenPayload = decodeTokenPayload(getAccessToken() || getRefreshToken() || "");

    const email = explicitProfile.email || tokenPayload?.sub || "";
    const displayName = explicitProfile.displayName || (email ? email.split("@")[0] : "Пользователь");

    return {
        email,
        displayName,
        userId: explicitProfile.userId || tokenPayload?.userId || null
    };
}

export function getTokenMeta() {
    const accessPayload = decodeTokenPayload(getAccessToken() || "");
    const refreshPayload = decodeTokenPayload(getRefreshToken() || "");

    return {
        accessPayload,
        refreshPayload
    };
}

export function setSelectedPlanId(planId) {
    if (planId == null) {
        localStorage.removeItem(STORAGE_KEYS.selectedPlanId);
        return;
    }
    localStorage.setItem(STORAGE_KEYS.selectedPlanId, String(planId));
}

export function getSelectedPlanId() {
    const raw = localStorage.getItem(STORAGE_KEYS.selectedPlanId);
    return raw ? Number(raw) : null;
}

export function cachePlan(plan) {
    const current = readJson(STORAGE_KEYS.cachedPlans, {});
    current[String(plan.id)] = plan;
    writeJson(STORAGE_KEYS.cachedPlans, current);
}

export function getCachedPlan(planId) {
    if (planId == null) {
        return null;
    }
    const cached = readJson(STORAGE_KEYS.cachedPlans, {});
    return cached[String(planId)] || null;
}

export function savePlanDraft(draft) {
    writeJson(STORAGE_KEYS.planDraft, draft);
}

export function getPlanDraft() {
    return readJson(STORAGE_KEYS.planDraft, {
        goal: "",
        currentLevel: "BEGINNER",
        hoursPerWeek: 10,
        knownTopics: ""
    });
}

export function getProgressState(planId) {
    const state = readJson(STORAGE_KEYS.progressByPlan, {});
    return state[String(planId)] || {};
}

export function setTopicProgress(planId, topicId, status) {
    const current = readJson(STORAGE_KEYS.progressByPlan, {});
    const key = String(planId);
    current[key] = current[key] || {};
    current[key][String(topicId)] = status;
    writeJson(STORAGE_KEYS.progressByPlan, current);
}
