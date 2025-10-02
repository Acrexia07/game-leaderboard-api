package com.marlonb.game_leaderboard_api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

// Custom annotation to validate that a field's value matches one of the enum constants.
@Documented
@Constraint(validatedBy = ValueOfEnumValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueOfEnum {

    // The enum class to validate against.
    Class<? extends Enum<?>> enumClass();

    // Default validation message if value is not in enum.
    String message() default "must be any of the choices {enumClass}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
