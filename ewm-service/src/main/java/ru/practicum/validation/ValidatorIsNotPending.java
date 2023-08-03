package ru.practicum.validation;


import ru.practicum.model.Status;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidatorIsNotPending implements ConstraintValidator<IsNotPending, Status> {

    @Override
    public boolean isValid(Status status, ConstraintValidatorContext cxt) {
        return status != null && !status.equals(Status.PENDING);
    }
}
