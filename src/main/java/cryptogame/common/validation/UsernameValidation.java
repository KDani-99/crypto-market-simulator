package cryptogame.common.validation;

import java.util.regex.Pattern;

/**
 * Validation class for username type fields.
 * Implements {@link Validation<String>} interface.
 */
public class UsernameValidation implements Validation<String> {

    /**
     * The validation pattern that describes how an email should look like.
     *
     * May only contain alphanumeric characters, or single hyphens.
     * The length must be between 4 and 50 characters.
     * Must not begin and end with a hyphen.
     */
    private static final Pattern pattern = Pattern.compile("(^[a-zA-Z0-9]{1}([a-zA-Z0-9-_]{2,48})[a-zA-Z0-9]{1}$)");

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(String username) {
        return pattern.matcher(username).matches();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorMessage() {
        return "Username may only contain alphanumeric characters or single hyphens, length must be between 4 and 50 characters, and cannot begin or end with a hyphen.";
    }
}
