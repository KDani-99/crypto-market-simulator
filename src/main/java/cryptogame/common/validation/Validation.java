package cryptogame.common.validation;

public interface Validation<T> {

    boolean validate(T object);
    String getErrorMessage();

}
