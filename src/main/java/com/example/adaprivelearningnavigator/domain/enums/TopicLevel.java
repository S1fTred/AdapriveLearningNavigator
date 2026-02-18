package com.example.adaprivelearningnavigator.domain.enums;

public enum TopicLevel {
    BASIC("Базовый"),
    INTERMEDIATE("Средний"),
    ADVANCED("Продвинутый");

    private final String displayName;

    TopicLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
