package cryptogame.common.validation;

import java.util.regex.Pattern;

public class PasswordValidation implements Validation<String> {

    private final String errorMessage = "Password must be at least 8 characters long, and must include a number, lowercase and uppercase letter, and a special character.";

    private static final Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%\\^&\\*])(?=.{6,255})");
    @Override
    public boolean validate(String password) {
        return pattern.matcher(password).find();
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
