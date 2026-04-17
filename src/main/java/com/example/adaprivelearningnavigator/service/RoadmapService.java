package com.example.adaprivelearningnavigator.service;

import com.example.adaprivelearningnavigator.service.dto.common.PageResponse;
import com.example.adaprivelearningnavigator.service.dto.roadmap.RoadmapDetailResponse;
import com.example.adaprivelearningnavigator.service.dto.roadmap.RoadmapSummaryResponse;
import com.example.adaprivelearningnavigator.service.dto.roadmap.RoadmapTopicDetailResponse;

public interface RoadmapService {
    PageResponse<RoadmapSummaryResponse> getRoadmaps(int page, int size);

    RoadmapDetailResponse getRoadmap(Long roleId);

    RoadmapTopicDetailResponse getRoadmapTopic(Long roleId, Long topicId);
}
