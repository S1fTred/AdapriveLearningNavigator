import {
    cacheRoadmap,
    cacheRoadmapTopic,
    cachePlan,
    clearSession,
    getAccessToken,
    getCachedPlan,
    getCachedRoadmap,
    getCachedRoadmapTopic,
    getRefreshToken,
    saveUserProfile,
    setAuthTokens,
    setSelectedPlanId
} from "/assets/js/core/session.js";

export class ApiError extends Error {
    constructor(status, message, errors = [], body = null) {
        super(message || "Ошибка запроса");
        this.name = "ApiError";
        this.status = status;
        this.errors = errors;
        this.body = body;
    }
}

async function parseResponse(response) {
    const contentType = response.headers.get("content-type") || "";
    if (contentType.includes("application/json")) {
        return response.json();
    }
    return response.text();
}

function normalizeError(status, body) {
    if (body && typeof body === "object") {
        return new ApiError(status, body.message || "Ошибка запроса", body.errors || [], body);
    }
    return new ApiError(status, typeof body === "string" && body ? body : "Ошибка запроса");
}

async function refreshTokens() {
    const refreshToken = getRefreshToken();
    if (!refreshToken) {
        clearSession();
        throw new ApiError(401, "Сессия истекла. Войдите снова.");
    }

    const response = await fetch("/auth/refresh", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Accept: "application/json"
        },
        body: JSON.stringify({ refreshToken })
    });

    const body = await parseResponse(response);
    if (!response.ok) {
        clearSession();
        throw normalizeError(response.status, body);
    }

    setAuthTokens(body.accessToken, body.refreshToken);
    return body;
}

export async function apiRequest(path, options = {}) {
    const {
        method = "GET",
        body,
        auth = true,
        skipRefresh = false,
        headers = {}
    } = options;

    const requestHeaders = {
        Accept: "application/json",
        ...headers
    };

    if (body !== undefined) {
        requestHeaders["Content-Type"] = "application/json";
    }

    if (auth && getAccessToken()) {
        requestHeaders.Authorization = `Bearer ${getAccessToken()}`;
    }

    const response = await fetch(path, {
        method,
        headers: requestHeaders,
        body: body !== undefined ? JSON.stringify(body) : undefined
    });

    if (response.status === 401 && auth && !skipRefresh && getRefreshToken()) {
        await refreshTokens();
        return apiRequest(path, { ...options, skipRefresh: true });
    }

    const parsed = await parseResponse(response);
    if (!response.ok) {
        throw normalizeError(response.status, parsed);
    }

    return parsed;
}

export const authApi = {
    async register(payload) {
        const response = await apiRequest("/auth/register", {
            method: "POST",
            auth: false,
            body: payload
        });
        setAuthTokens(response.accessToken, response.refreshToken);
        saveUserProfile({
            email: payload.email,
            displayName: payload.displayName
        });
        return response;
    },

    async login(payload) {
        const response = await apiRequest("/auth/login", {
            method: "POST",
            auth: false,
            body: payload
        });
        setAuthTokens(response.accessToken, response.refreshToken);
        saveUserProfile({
            email: payload.email
        });
        return response;
    },

    async refresh() {
        return refreshTokens();
    }
};

export const usersApi = {
    async me() {
        const response = await apiRequest("/api/users/me");
        saveUserProfile({
            email: response.email,
            displayName: response.displayName,
            createdAt: response.createdAt
        });
        return response;
    }
};

export const plansApi = {
    async list(page = 0, size = 12) {
        return apiRequest(`/api/plans?page=${page}&size=${size}`);
    },

    async get(planId) {
        const cached = getCachedPlan(planId);
        if (cached) {
            return cached;
        }

        const response = await apiRequest(`/api/plans/${planId}`);
        cachePlan(response);
        return response;
    },

    async generate(payload) {
        const response = await apiRequest("/api/plans/generate-with-ai", {
            method: "POST",
            body: payload
        });
        cachePlan(response);
        setSelectedPlanId(response.id);
        return response;
    },

    async buildFromRoadmap(payload) {
        const response = await apiRequest("/api/plans/build-from-roadmap", {
            method: "POST",
            body: payload
        });
        cachePlan(response);
        setSelectedPlanId(response.id);
        return response;
    }
};

export const progressApi = {
    async list(planId) {
        return apiRequest(`/api/plans/${planId}/progress`);
    },

    async update(planId, payload) {
        return apiRequest(`/api/plans/${planId}/progress`, {
            method: "PUT",
            body: payload
        });
    }
};

export const roadmapsApi = {
    async list(page = 0, size = 24) {
        return apiRequest(`/api/roadmaps?page=${page}&size=${size}`);
    },

    async get(roadmapId) {
        const cached = getCachedRoadmap(roadmapId);
        if (cached) {
            return cached;
        }

        const response = await apiRequest(`/api/roadmaps/${roadmapId}`);
        cacheRoadmap(response);
        return response;
    },

    async getTopic(roadmapId, topicId) {
        const cached = getCachedRoadmapTopic(roadmapId, topicId);
        if (cached) {
            return cached;
        }

        const response = await apiRequest(`/api/roadmaps/${roadmapId}/topics/${topicId}`);
        cacheRoadmapTopic(roadmapId, response);
        return response;
    }
};

export const tutorApi = {
    async ask(roadmapId, topicId, payload) {
        return apiRequest(`/api/roadmaps/${roadmapId}/topics/${topicId}/tutor`, {
            method: "POST",
            body: payload
        });
    }
};

export const quizzesApi = {
    async getTopicQuiz(topicId) {
        return apiRequest(`/api/topics/${topicId}/quiz`);
    },

    async getQuestions(quizId) {
        return apiRequest(`/api/quizzes/${quizId}/questions`);
    },

    async submit(payload) {
        return apiRequest("/api/quizzes/attempts", {
            method: "POST",
            body: payload
        });
    },

    async attempts(quizId = null) {
        const query = quizId ? `?quizId=${quizId}` : "";
        return apiRequest(`/api/quizzes/attempts${query}`);
    }
};
