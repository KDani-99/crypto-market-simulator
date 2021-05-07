package cryptogame.model.exception;

import cryptogame.common.validation.ValidationError;

import java.util.Set;

/**
 * A custom exception that is thrown when a validation error
 * occurs.
 */
public class ValidationException extends Exception {

    /**
     * The final {@link java.util.Set} that contains every validation errors
     * from an object.
     */
    private final Set<ValidationError> validationErrors;

    /**
     * Creates a new {@link ValidationException} and sets the validation errors.
     *
     * @param validationErrors the {@link java.util.Set} containing every validation errors
     */
    public ValidationException(Set<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    /**
     * Returns the validation errors {@link java.util.Set}
     *
     * @return the {@link java.util.Set} with all the validation errors
     */
    public Set<ValidationError> getValidationErrors() {
        return validationErrors;
    }
}
