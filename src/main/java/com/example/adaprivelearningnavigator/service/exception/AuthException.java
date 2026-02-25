package com.example.adaprivelearningnavigator.service.exception;

/**
 * Ошибки авторизации (401): неверные учётные данные, просроченный токен и т.п.
 */
public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
