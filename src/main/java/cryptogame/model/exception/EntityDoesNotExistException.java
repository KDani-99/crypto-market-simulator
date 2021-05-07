package cryptogame.model.exception;

/**
 * A custom exception that is thrown when an entity is not found
 * with the given criteria parameters.
 */
public class EntityDoesNotExistException extends Exception {
    /**
     * Creates a new {@link EntityDoesNotExistException} Sets the error message by calling the {@code super} constructor.
     *
     * @param entity the entity object that could not be found
     */
    public EntityDoesNotExistException(Class<?> entity) {
        super(
                String.format("Entity (type: %s) does not exist",entity.getName())
        );
    }
}
