package com.example.adaprivelearningnavigator.service;

import com.example.adaprivelearningnavigator.service.dto.topic_graph.TopicPrereqResponse;
import com.example.adaprivelearningnavigator.service.dto.topic_graph.TopicResponse;

import java.util.List;

public interface RoadmapService {
    List<TopicResponse> getRoadmapTopics(Long goalId);

    List<TopicPrereqResponse> getRoadmapEdges(Long goalId);
}
