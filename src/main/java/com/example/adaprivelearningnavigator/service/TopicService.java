package com.example.adaprivelearningnavigator.service;

import com.example.adaprivelearningnavigator.service.dto.common.PageResponse;
import com.example.adaprivelearningnavigator.service.dto.topic_graph.TopicCreateRequest;
import com.example.adaprivelearningnavigator.service.dto.topic_graph.TopicPrereqRequest;
import com.example.adaprivelearningnavigator.service.dto.topic_graph.TopicPrereqResponse;
import com.example.adaprivelearningnavigator.service.dto.topic_graph.TopicResponse;
import com.example.adaprivelearningnavigator.service.dto.topic_graph.TopicUpdateRequest;

import java.util.List;

public interface TopicService {
    PageResponse<TopicResponse> getTopics(int page, int size);

    TopicResponse getTopic(Long topicId);

    TopicResponse createTopic(TopicCreateRequest request);

    TopicResponse updateTopic(Long topicId, TopicUpdateRequest request);

    List<TopicPrereqResponse> getTopicPrereqs(Long topicId);

    TopicPrereqResponse addPrereq(TopicPrereqRequest request);
}
