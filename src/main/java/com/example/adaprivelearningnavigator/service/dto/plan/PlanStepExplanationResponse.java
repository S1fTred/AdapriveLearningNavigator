package com.example.adaprivelearningnavigator.service.dto.plan;


import com.example.adaprivelearningnavigator.domain.enums.PrereqStatus;
import lombok.Builder;

import java.util.List;

@Builder
public record PlanStepExplanationResponse(
        String ruleApplied,
        String topicPriorityReason,
        String resourceReason,
        List<PrereqItem> prereqs
) {
    public record PrereqItem(
            Long prereqTopicId,
            PrereqStatus status
    ) {}
}
