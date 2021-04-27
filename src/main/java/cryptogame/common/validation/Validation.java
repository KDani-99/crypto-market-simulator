package cryptogame.common.validation;

import java.util.HashSet;

public abstract class Validation {

    public static <T> HashSet<ValidationError> validateObject(T object) throws Exception {

        var result = new HashSet<ValidationError>();

        var fields = object.getClass().getDeclaredFields();

        for(var field : fields) {

            var validationAnnotation = field.getAnnotation(Validate.class);

            if(validationAnnotation == null) continue;

            field.setAccessible(true);

            var validatorInstance = (IValidation<Object>)validationAnnotation.validatorClass().getDeclaredConstructor().newInstance();
            var value = field.get(object);

            if(!validatorInstance.validate(value)) {
                result.add(new ValidationError(
                        field.getName(),
                        validatorInstance.getErrorMessage()
                ));
            }
        }

        return result;
    }

}
