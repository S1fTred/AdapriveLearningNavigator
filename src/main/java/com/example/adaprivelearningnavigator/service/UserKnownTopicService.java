package com.example.adaprivelearningnavigator.service;

import com.example.adaprivelearningnavigator.service.dto.user_known.UserKnownTopicResponse;
import com.example.adaprivelearningnavigator.service.dto.user_known.UserKnownTopicUpsertRequest;

import java.util.List;

public interface UserKnownTopicService {
    List<UserKnownTopicResponse> getKnownTopics(Long userId);

    UserKnownTopicResponse upsertKnownTopic(Long userId, UserKnownTopicUpsertRequest request);
}
