package cryptogame.services.exception;

import cryptogame.common.validation.ValidationError;

import java.util.Set;

public class ValidationException extends Exception {

    private final Set<ValidationError> validationErrors;

    public ValidationException(Set<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public Set<ValidationError> getValidationErrors() {
        return validationErrors;
    }
}
