package com.example.adaprivelearningnavigator.service.dto.topic_graph;

import com.example.adaprivelearningnavigator.domain.enums.PrereqRelationType;
import jakarta.validation.constraints.NotNull;


public record TopicPrereqRequest(
        @NotNull(message = "id пререквизита обязателен")
        Long prereqTopicId,

        @NotNull(message = "id следующей темы обязателен")
        Long nextTopicId,

        PrereqRelationType relationType
) {}
