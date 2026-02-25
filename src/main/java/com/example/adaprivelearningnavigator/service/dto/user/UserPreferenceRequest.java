package com.example.adaprivelearningnavigator.service.dto.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UserPreferenceRequest(
        @Size(max = 10, message = "Код языка слишком длинный")
        String preferredLanguage,

        @Min(value = 1, message = "Часов в неделю должно быть не меньше 1")
        @Max(value = 80, message = "Часов в неделю должно быть не больше 80")
        Integer hoursPerWeekDefault,

        // MVP: строка "VIDEO,ARTICLE". Позже можно сделать массивом enum.
        @Size(max = 200, message = "Список типов ресурсов слишком длинный")
        String preferredResourceTypes
) {}
