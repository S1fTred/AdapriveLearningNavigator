package com.example.adaprivelearningnavigator.service.exception;

/**
 * Некорректные входные данные (400).
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
