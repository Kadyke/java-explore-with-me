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
@Constraint(validatedBy = ValidatorIsNotPending.class)
public @interface IsNotPending {
    public String message() default "Invalid status: ";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}