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
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEmailAlreadyExistException(final EmailAlreadyExistException e) {
        return new ApiError(HttpStatus.CONFLICT, e.getLocalizedMessage(), e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCategoryAlreadyExistException(final CategoryAlreadyExistException e) {
        return new ApiError(HttpStatus.CONFLICT, e.getLocalizedMessage(), e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND, e.getLocalizedMessage(), e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleEventDateException(final EventDateException e) {
        return new ApiError(HttpStatus.FORBIDDEN, e.getLocalizedMessage(), e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCategoryWithEventsException(final CategoryWithEventsException e) {
        return new ApiError(HttpStatus.CONFLICT, e.getLocalizedMessage(), e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventAlreadyPublishedException(final EventAlreadyPublishedException e) {
        return new ApiError(HttpStatus.CONFLICT, e.getLocalizedMessage(), e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleRequestAlreadyExistException(final RequestException e) {
        return new ApiError(HttpStatus.CONFLICT, e.getLocalizedMessage(), e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final BadRequestException e) {
        return new ApiError(HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventIsNotPendingException(final EventIsNotPendingException e) {
        return new ApiError(HttpStatus.CONFLICT, e.getLocalizedMessage(), e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventIsNotPublishedException(final EventIsNotPublishedException e) {
        return new ApiError(HttpStatus.CONFLICT, e.getLocalizedMessage(), e.getMessage(), LocalDateTime.now());
    }
}

