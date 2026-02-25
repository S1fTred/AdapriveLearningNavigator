package com.example.adaprivelearningnavigator.service.exception;

/**
 * Доступ запрещён (403): недостаточно прав.
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
