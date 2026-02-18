package com.example.adaprivelearningnavigator.domain.enums;

public enum ProgressStatus {
    NOT_STARTED("Не начато"),
    IN_PROGRESS("В процессе"),
    DONE("Завершено");

    private final String displayName;

    ProgressStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
