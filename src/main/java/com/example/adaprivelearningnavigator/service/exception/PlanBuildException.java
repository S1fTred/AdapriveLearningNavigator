package com.example.adaprivelearningnavigator.service.exception;

/**
 * Ошибка построения плана (например, невозможно уложиться в лимиты, пустой набор тем и т.п.)
 * Обычно 400.
 */
public class PlanBuildException extends RuntimeException {
    public PlanBuildException(String message) {
        super(message);
    }
}
