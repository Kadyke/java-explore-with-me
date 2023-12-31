package ru.practicum.validation;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target({ FIELD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidatorEventDateNotNull.class)
public @interface EventDateAndNotNull {
    public String message() default "Invalid eventDate: ";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}