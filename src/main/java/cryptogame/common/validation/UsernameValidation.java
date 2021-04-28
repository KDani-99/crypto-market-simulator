package cryptogame.common.validation;

import java.util.regex.Pattern;

public class UsernameValidation implements IValidation<String> {

    private final String errorMessage = "Username may only contain alphanumeric characters or single hyphens, length must be between 4 and 50 characters, and cannot begin or end with a hyphen.";

    private static final Pattern pattern = Pattern.compile("(^[a-zA-Z0-9]{1}([a-zA-Z0-9-_]{2,48})[a-zA-Z0-9]{1}$)");
    @Override
    public boolean validate(String username) {
        return pattern.matcher(username).matches();
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
