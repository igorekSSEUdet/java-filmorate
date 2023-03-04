package ru.yandex.practicum.filmorate.exceptions;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.LocalDate;

@Documented
@Constraint(validatedBy = ContactDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckIfBefore1895 {
    String message() default "The release date of the film should not be earlier than December 28, 1895";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class ContactDateValidator implements
        ConstraintValidator<CheckIfBefore1895, LocalDate> {

    @Override
    public void initialize(CheckIfBefore1895 contactNumber) {
    }

    @Override
    public boolean isValid(LocalDate dateTime,
                           ConstraintValidatorContext cxt) {
        return !dateTime.isBefore(LocalDate.of(1895, 12, 28));
    }

}