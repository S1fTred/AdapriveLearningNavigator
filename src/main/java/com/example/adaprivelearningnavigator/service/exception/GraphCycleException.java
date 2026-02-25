package com.example.adaprivelearningnavigator.service.exception;

/**
 * Цикл в графе зависимостей тем (DAG нарушен) — обычно 409.
 */
public class GraphCycleException extends RuntimeException {
    public GraphCycleException(String message) {
        super(message);
    }
}