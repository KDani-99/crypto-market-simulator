package cryptogame.common.validation;

/**
 * The {@link ValidationError} class is a POJO that contains
 * info that helps with the identification of the error origin.
 */
@lombok.Data
public class ValidationError {

    /**
     * The name of the field where the error has occurred.
     */
    private String fieldName;
    /**
     * The error message from the validation class.
     */
    private String message;

    /**
     * The constructor of the {@link ValidationError} class.
     *
     * @param fieldName the name of the field
     * @param message the error message from the validation class
     */
    public ValidationError(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
}
