package cryptogame.common.validation;

import java.lang.reflect.Field;
import java.util.HashSet;

public abstract class BaseValidation {

    private static <T> HashSet<ValidationError> validateField(Field field, T object, Validate validationAnnotation) throws Exception {

        var result = new HashSet<ValidationError>();

        var validatorInstance = (IValidation<Object>)validationAnnotation.validatorClass().getDeclaredConstructor().newInstance();
        var value = field.get(object);

        if(!validatorInstance.validate(value)) {
            result.add(new ValidationError(
                    field.getName(),
                    validatorInstance.getErrorMessage()
            ));
        }

        return result;
    }

    public static <T> HashSet<ValidationError> validateObject(T object) throws Exception {

        var result = new HashSet<ValidationError>();

        var fields = object.getClass().getDeclaredFields();

        for(var field : fields) {

            var validationAnnotation = field.getAnnotation(Validate.class);
            if(validationAnnotation == null) continue;

            field.setAccessible(true);

            var fieldResult = validateField(field,object,validationAnnotation);
            result.addAll(fieldResult);

        }

        return result;
    }

}
