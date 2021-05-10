package cryptogame.utils.validation;

import cryptogame.model.exception.ValidationException;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract validation class that handles the validation of the given object with type.
 * This class must not be instantiated, so it is an abstract class.
 */
public abstract class BaseValidation {

    /***
     * A helper method that validates each field of the given generic object that has
     * a {@link Validate} annotation.
     *
     * If the validation fails, a new {@link ValidationError} instance will be added to
     * the result Set, which contains all of the {@link ValidationError} errors of the given field.
     *
     * @param field the field with a {@link Validate} annotation
     * @param object the object instance that contains the given field
     * @param validationAnnotation the given {@link Validate} annotation
     * @param <T> the type of the given object instance
     * @return a set with all the {@link ValidationError} errors of the given field
     * @throws Exception if {@link java.lang.Class<T>#getDeclaredConstructor() } method is not found
     */
    private static <T> HashSet<ValidationError> validateField(Field field, T object, Validate validationAnnotation) throws Exception {

        var result = new HashSet<ValidationError>();

        var validatorInstance = (Validation<Object>)validationAnnotation.validatorClass().getDeclaredConstructor().newInstance();
        var value = field.get(object);

        if(!validatorInstance.validate(value)) {
            result.add(new ValidationError(
                    field.getName(),
                    validatorInstance.getErrorMessage()
            ));
        }

        return result;
    }

    /**
     * Validates all the {@link Field}s of the given object of type T
     * with {@link Validate} annotation.
     *
     * A result {@link java.util.Set} with all the validation errors
     * if there has been any, otherwise an empty set will be returned.
     *
     * @param object the object instance to validate
     * @param <T> the type of the given object instance
     * @throws Exception if an unexpected error occurs
     * @throws ValidationException if {@link BaseValidation#validateField(Field, Object, Validate)} throws an error
     */
    public static <T> void validateObject(T object) throws Exception,ValidationException {

        var result = new HashSet<ValidationError>();

        var fields = object.getClass().getDeclaredFields();

        for(var field : fields) {

            var validationAnnotation = field.getAnnotation(Validate.class);
            if(validationAnnotation == null) continue;

            field.setAccessible(true);

            var fieldResult = validateField(field,object,validationAnnotation);
            result.addAll(fieldResult);

        }

        if(result.size() != 0) {
            throw new ValidationException(result);
        }
    }

}
