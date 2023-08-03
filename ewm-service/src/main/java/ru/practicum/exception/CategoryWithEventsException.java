package ru.practicum.exception;

public class CategoryWithEventsException extends RuntimeException {
    public CategoryWithEventsException() {
        super("У категории существуют события.");
    }
}
