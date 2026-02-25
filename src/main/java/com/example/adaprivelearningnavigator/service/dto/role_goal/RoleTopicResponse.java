package com.example.adaprivelearningnavigator.service.dto.role_goal;

import lombok.Builder;

@Builder
public record RoleTopicResponse(
        Long roleId,
        Long topicId,
        Integer priority,
        boolean isRequired
) {}
