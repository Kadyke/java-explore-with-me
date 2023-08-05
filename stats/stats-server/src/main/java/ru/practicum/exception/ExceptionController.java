package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.model.ApiError;

import java.time.LocalDateTime;

@RestControllerAdvice("ru.practicum")
public class ExceptionController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final DateException e) {
        return new ApiError(HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getMessage(), LocalDateTime.now());
    }

}

