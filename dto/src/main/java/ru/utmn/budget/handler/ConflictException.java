package ru.utmn.budget.handler;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
