package ru.yandex.practicum.filmorate.exceptios.springValidAnnotations;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ContactBlankValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckHasBlank {
    String message() default "The login cannot be empty and contain spaces";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class ContactBlankValidator implements
        ConstraintValidator<CheckHasBlank, String> {

    @Override
    public void initialize(CheckHasBlank contactNumber) {
    }

    @Override
    public boolean isValid(String login,
                           ConstraintValidatorContext cxt) {
        return !login.contains(" ");
    }

}

