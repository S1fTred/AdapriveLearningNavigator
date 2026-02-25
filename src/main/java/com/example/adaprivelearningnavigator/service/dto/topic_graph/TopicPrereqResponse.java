package com.example.adaprivelearningnavigator.service.dto.topic_graph;

import com.example.adaprivelearningnavigator.domain.enums.PrereqRelationType;
import lombok.Builder;

@Builder
public record TopicPrereqResponse(
        Long id,
        Long prereqTopicId,
        Long nextTopicId,
        PrereqRelationType relationType
) {}
