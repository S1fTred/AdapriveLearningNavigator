package com.example.adaprivelearningnavigator.domain.enums;

public enum QuizQuestionType {
    SINGLE("Один правильный ответ"), // Один правильный ответ
    MULTIPLE("Несколько правильных ответов"); // Несколько правильных ответов

    private final String displayName;

    QuizQuestionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
