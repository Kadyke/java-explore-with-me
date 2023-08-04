package ru.practicum.exception;

public class DateException extends RuntimeException {
    public DateException() {
        super("Дата начала не может быть позже даты конца.");
    }
}
