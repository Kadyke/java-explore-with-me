package ru.practicum.exception;

public class EventIsNotPublishedException extends RuntimeException {
    public EventIsNotPublishedException() {
        super("Можно оценивать только опубликованные события.");
    }
}
