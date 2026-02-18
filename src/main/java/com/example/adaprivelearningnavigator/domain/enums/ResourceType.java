package com.example.adaprivelearningnavigator.domain.enums;

public enum ResourceType {
    VIDEO("Видео"),
    ARTICLE("Статья"),
    COURSE("Онлайн-курс"),
    BOOK("Книга"),
    INTERACTIVE("Интерактивный материал");

    private final String displayName;
    ResourceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
