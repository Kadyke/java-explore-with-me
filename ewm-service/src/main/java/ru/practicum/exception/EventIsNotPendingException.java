package ru.practicum.exception;

public class EventIsNotPendingException extends RuntimeException {
    public EventIsNotPendingException() {
        super("Можно публиковать или отменять только события в состоянии рассмотрения.");
    }
}
