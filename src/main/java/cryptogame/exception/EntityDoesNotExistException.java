package cryptogame.exception;

public class EntityDoesNotExistException extends Exception {
    public EntityDoesNotExistException(Class<?> entity) {
        super(
                String.format("Entity (type: %s) does not exist",entity.getName())
        );
    }
}
