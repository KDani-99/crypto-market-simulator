package cryptogame.utils.validation;

import java.util.regex.Pattern;

/**
 * Validation class for password type fields.
 * Implements {@link Validation<String>} interface.
 */
public class PasswordValidation implements Validation<String> {

    /**
     * The validation pattern that describes how an email should look like.
     *
     * The length must be between 6 and 255 characters.
     * Must not contain any special characters.
     *
     * Valid format may look like: email@domain.com.
     * Invalid format may look like: ema#il@a.
     */
    private static final Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%\\^&\\*])(?=.{6,255})");

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(String password) {
        if(password == null) {
            return false;
        }
        return pattern.matcher(password).find();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorMessage() {
        return "Password must be at least 8 characters long, and must include a number, lowercase and uppercase letter, and a special character.";
    }
}
