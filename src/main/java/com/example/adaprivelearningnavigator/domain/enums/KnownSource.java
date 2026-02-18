package com.example.adaprivelearningnavigator.domain.enums;

public enum KnownSource {
    MANUAL("Отмечено вручную"), // Отмечено вручную
    QUIZ("По результату теста"), // По результату теста
    IMPORT("Импортировано"); // Импортировано

    private final String displayName;
    KnownSource(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
