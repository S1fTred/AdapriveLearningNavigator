package com.example.adaprivelearningnavigator.domain.enums;

public enum ScenarioType {
    BASE("Базовый"),
    WHAT_IF("Альтернативный сценарий");

    private final String displayName;
    ScenarioType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return  displayName;
    }
}
