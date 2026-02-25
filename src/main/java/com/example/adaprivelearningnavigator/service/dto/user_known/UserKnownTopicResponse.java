package com.example.adaprivelearningnavigator.service.dto.user_known;


import com.example.adaprivelearningnavigator.domain.enums.KnownSource;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record UserKnownTopicResponse(
        Long topicId,
        KnownSource source,
        BigDecimal confidence,
        Instant markedAt
) {}