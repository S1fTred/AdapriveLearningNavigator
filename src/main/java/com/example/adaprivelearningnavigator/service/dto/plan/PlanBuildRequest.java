package com.example.adaprivelearningnavigator.service.dto.plan;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record PlanBuildRequest(
        @NotNull(message = "id цели/роли обязателен")
        Long roleId,

        @NotNull(message = "Часов в неделю обязателен")
        @Min(value = 1, message = "Часов в неделю должно быть не меньше 1")
        @Max(value = 80, message = "Часов в неделю должно быть не больше 80")
        Integer hoursPerWeek,

        // what-if / override: если null -> берём из профиля пользователя
        Set<Long> knownTopicIds,

        // метка сценария (для what-if)
        String scenarioLabel
) {}
