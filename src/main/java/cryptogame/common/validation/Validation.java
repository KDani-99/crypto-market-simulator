package cryptogame.common.validation;

public interface IValidation<T> {

    boolean validate(T object);
    String getErrorMessage();

}
