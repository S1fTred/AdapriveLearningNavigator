package com.example.adaprivelearningnavigator.service;

import com.example.adaprivelearningnavigator.service.dto.common.IdResponse;
import com.example.adaprivelearningnavigator.service.dto.common.PageResponse;
import com.example.adaprivelearningnavigator.service.dto.resource.ResourceCreateRequest;
import com.example.adaprivelearningnavigator.service.dto.resource.ResourceResponse;
import com.example.adaprivelearningnavigator.service.dto.resource.TopicResourceRequest;

import java.util.List;

public interface ResourceService {
    PageResponse<ResourceResponse> getResources(int page, int size);

    ResourceResponse createResource(ResourceCreateRequest request);

    List<ResourceResponse> getTopicResources(Long topicId);

    IdResponse attachResourceToTopic(TopicResourceRequest request);
}
