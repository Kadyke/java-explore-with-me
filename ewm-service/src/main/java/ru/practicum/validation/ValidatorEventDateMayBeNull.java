package ru.practicum.validation;


import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.EventDateException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.LocalDateTime;

public class ValidatorEventDateMayBeNull implements ConstraintValidator<EventDateAndMayBeNull, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime eventDate, ConstraintValidatorContext cxt) {
        if (eventDate == null) {
            return true;
        }
        LocalDateTime now = LocalDateTime.now();
        if (eventDate.isBefore(now)) {
            throw new BadRequestException("Дата события не может быть в прошлом.");
        }
        Duration duration = Duration.between(now, eventDate);
        if (duration.toHours() < 2) {
            throw new EventDateException("Дата события не может быть раньше через 2 часа от создания.");
        }
        return true;
    }
}
