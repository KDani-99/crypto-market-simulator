package cryptogame.model.common.validation;

@lombok.Data
public class ValidationError {

    private String fieldName;
    private String message;

    public ValidationError(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
}
