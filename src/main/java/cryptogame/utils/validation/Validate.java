package cryptogame.utils.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation that can be used to mark
 * fields that require validation.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Validate {

    /**
     * The validator class of the field.
     *
     * @return validator class
     */
    Class<? extends Validation<?>> validatorClass();

}
