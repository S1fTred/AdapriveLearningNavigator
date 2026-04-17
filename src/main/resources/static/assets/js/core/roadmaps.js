import { roadmapsApi } from "/assets/js/core/api.js";
import { getCachedRoadmap, getSelectedRoadmapId, setSelectedRoadmapId } from "/assets/js/core/session.js";

export async function resolveActiveRoadmap(explicitRoadmapId = null) {
    if (explicitRoadmapId) {
        setSelectedRoadmapId(explicitRoadmapId);
    }

    const selectedRoadmapId = explicitRoadmapId || getSelectedRoadmapId();
    if (selectedRoadmapId) {
        const cached = getCachedRoadmap(selectedRoadmapId);
        if (cached) {
            return cached;
        }
        return roadmapsApi.get(selectedRoadmapId);
    }

    const page = await roadmapsApi.list(0, 20);
    const firstRoadmap = page.items?.[0];
    if (!firstRoadmap) {
        return null;
    }

    setSelectedRoadmapId(firstRoadmap.id);
    return roadmapsApi.get(firstRoadmap.id);
}
