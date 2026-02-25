package com.example.adaprivelearningnavigator.service.exception;

/**
 * Конфликт состояния (409): нарушение уникальности, несогласованность данных и т.п.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
