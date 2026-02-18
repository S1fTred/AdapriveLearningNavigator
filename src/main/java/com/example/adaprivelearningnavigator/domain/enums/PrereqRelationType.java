package com.example.adaprivelearningnavigator.domain.enums;

public enum PrereqRelationType {
    REQUIRED("Обязательная"),
    RECOMMENDED("Рекомендуемая");

    private final String displayName;
    PrereqRelationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
