import { roadmapsApi } from "/assets/js/core/api.js";
import { filterMvpRoadmaps } from "/assets/js/core/roadmap-catalog.js";
import { getCachedRoadmap, getSelectedRoadmapId, setSelectedRoadmapId } from "/assets/js/core/session.js";

export async function resolveActiveRoadmap(explicitRoadmapId = null) {
    if (explicitRoadmapId) {
        setSelectedRoadmapId(explicitRoadmapId);
    }

    const selectedRoadmapId = explicitRoadmapId || getSelectedRoadmapId();
    const page = await roadmapsApi.list(0, 120);
    const visibleRoadmaps = filterMvpRoadmaps(page.items || []);
    const visibleSelectedRoadmap = visibleRoadmaps.find((roadmap) => roadmap.id === selectedRoadmapId);

    if (!visibleSelectedRoadmap && selectedRoadmapId) {
        const firstVisibleRoadmap = visibleRoadmaps[0];
        if (!firstVisibleRoadmap) {
            return null;
        }

        setSelectedRoadmapId(firstVisibleRoadmap.id);
        return roadmapsApi.get(firstVisibleRoadmap.id);
    }

    if (selectedRoadmapId) {
        const cached = getCachedRoadmap(selectedRoadmapId);
        if (cached) {
            return cached;
        }
        return roadmapsApi.get(selectedRoadmapId);
    }

    const firstRoadmap = visibleRoadmaps[0];
    if (!firstRoadmap) {
        return null;
    }

    setSelectedRoadmapId(firstRoadmap.id);
    return roadmapsApi.get(firstRoadmap.id);
}
