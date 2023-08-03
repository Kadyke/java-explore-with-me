package ru.practicum.exception;

public class EventAlreadyPublishedException extends RuntimeException {
    public EventAlreadyPublishedException() {
        super("Запрещено изменять уже опубликованные события.");
    }
}
