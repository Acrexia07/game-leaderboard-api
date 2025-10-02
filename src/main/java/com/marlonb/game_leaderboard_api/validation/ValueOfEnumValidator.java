package com.marlonb.game_leaderboard_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

// Validator for the @ValueOfEnum annotation.
// Checks if the field value matches any constant in the provided enum.
public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, Enum<?>> {

    private Enum<?>[] acceptedValues;

    // Initialize validator: store all enum constant names.
    @Override
    public void initialize(ValueOfEnum constraintAnnotation) {
        acceptedValues = constraintAnnotation.enumClass().getEnumConstants();
    }

    // Validation logic: return true if value is null (use @NotNull to enforce required)
    // or if value exists in enum constants.
    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if(value == null) return true;
        return Arrays.asList(acceptedValues).contains(value);
    }
}
