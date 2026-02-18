package com.example.adaprivelearningnavigator.domain.enums;

public enum PrereqStatus {
    DONE("Уже изучено"), //Уже изучено
    IN_PREVIOUS_STEPS("Запланировано ранее"), //Запланировано ранее
    MISSING("Не изучено"); //Не изучено

    private final String displayName;

    PrereqStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
