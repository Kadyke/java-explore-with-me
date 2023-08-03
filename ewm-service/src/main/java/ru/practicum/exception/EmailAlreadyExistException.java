package ru.practicum.exception;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException() {
        super("С данным емайлом пользователь уже существует.");
    }
}
