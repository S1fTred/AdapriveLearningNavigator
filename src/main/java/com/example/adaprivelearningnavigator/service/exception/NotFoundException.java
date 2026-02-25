package com.example.adaprivelearningnavigator.service.exception;

/**
 * Не найдено (404): сущность отсутствует.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
