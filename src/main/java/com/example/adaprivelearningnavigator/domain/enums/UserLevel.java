package com.example.adaprivelearningnavigator.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum UserLevel {
    BEGINNER,
    BASIC,
    INTERMEDIATE,
    ADVANCED;

    @JsonCreator
    public static UserLevel fromJson(String value) {
        if (value == null) {
            return null;
        }

        return Arrays.stream(values())
                .filter(level -> level.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported user level: " + value));
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
