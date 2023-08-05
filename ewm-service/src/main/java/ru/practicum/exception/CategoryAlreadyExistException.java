package ru.practicum.exception;

public class CategoryAlreadyExistException extends RuntimeException {
    public CategoryAlreadyExistException() {
        super("Данная категория уже существует");
    }
}
