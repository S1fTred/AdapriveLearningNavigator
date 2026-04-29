package com.example.adaprivelearningnavigator.ai.service;

import com.example.adaprivelearningnavigator.ai.dto.AiTutorRequest;
import com.example.adaprivelearningnavigator.ai.dto.AiTutorResponse;

public interface AiTutorService {
    AiTutorResponse ask(Long roleId, Long topicId, AiTutorRequest request);
}
