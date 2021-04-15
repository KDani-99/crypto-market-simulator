package cryptogame.model.common.validation;

import java.util.regex.Pattern;

public class PasswordValidation implements IValidation<String>{

    private final String errorMessage = "Invalid password";

    private static final Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%\\^&\\*])(?=.{6,255})");
    @Override
    public boolean validate(String password) {
        return pattern.matcher(password).matches();
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
