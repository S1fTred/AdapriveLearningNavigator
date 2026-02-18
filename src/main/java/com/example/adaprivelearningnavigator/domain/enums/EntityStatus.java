package com.example.adaprivelearningnavigator.domain.enums;

import lombok.Getter;

public enum EntityStatus {
    DRAFT("Черновик"),
    ACTIVE("Активен"),
    ARCHIVED("Архив");

    private final String displayName;

    EntityStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
