package com.example.adaprivelearningnavigator.service;

import com.example.adaprivelearningnavigator.service.dto.progress.TopicProgressResponse;
import com.example.adaprivelearningnavigator.service.dto.progress.TopicProgressUpsertRequest;

import java.util.List;

public interface ProgressService {
    List<TopicProgressResponse> getPlanProgress(Long userId, Long planId);

    TopicProgressResponse upsertTopicProgress(Long userId, Long planId, TopicProgressUpsertRequest request);
}
