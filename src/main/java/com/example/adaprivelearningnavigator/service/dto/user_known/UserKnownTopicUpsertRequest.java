package com.example.adaprivelearningnavigator.service.dto.user_known;


import com.example.adaprivelearningnavigator.domain.enums.KnownSource;
import jakarta.validation.constraints.NotNull;


import java.math.BigDecimal;

public record UserKnownTopicUpsertRequest(
        @NotNull(message = "id темы обязателен")
        Long topicId,

        KnownSource source,
        BigDecimal confidence
) {}