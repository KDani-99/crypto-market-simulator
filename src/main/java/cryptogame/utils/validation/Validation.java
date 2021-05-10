package cryptogame.utils.validation;

/**
 * The validation interface that must be implemented by classes
 * that contain validation logic.
 *
 * @param <T> the type of the object.
 */
public interface Validation<T> {

    /**
     * Validates the given object of type T with the
     * implemented logic of the {@link Validation<T>} class.
     *
     * @param object the object to validate
     * @return {@code true} if the validation was successful, {@code false} otherwise
     */
    boolean validate(T object);

    /**
     * Returns the error message of the {@link Validation<T>} class.
     *
     * @return error message String
     */
    String getErrorMessage();

}
