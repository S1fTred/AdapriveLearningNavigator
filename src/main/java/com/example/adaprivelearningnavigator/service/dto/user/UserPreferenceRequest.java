package com.example.adaprivelearningnavigator.service.dto.user;

import com.example.adaprivelearningnavigator.domain.enums.ResourceType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UserPreferenceRequest(
        @Size(max = 10, message = "Код языка слишком длинный")
        String preferredLanguage,

        @Min(value = 1, message = "Часов в неделю должно быть не меньше 1")
        @Max(value = 80, message = "Часов в неделю должно быть не больше 80")
        Integer hoursPerWeekDefault,

        @Size(max = 5, message = "Слишком много предпочтительных типов ресурсов")
        Set<@NotNull(message = "Тип ресурса не может быть null") ResourceType> preferredResourceTypes
) {}
