package cryptogame.common.validation;

public class EmailValidation implements IValidation<String> {

    private final String errorMessage = "Invalid email address.";

    @Override
    public boolean validate(String email) {
        if(email.length() > 125 || email.length() < 6) {
            return false;
        }

        if(email.indexOf(' ') > -1) {
            return false;
        }

        StringBuilder reversedEmail = new StringBuilder(email).reverse();

        if(reversedEmail.substring(0,reversedEmail.indexOf(".")).length() < 2) {
            // name@email.c -> invalid
            // name@email.co -> valid
            return false;
        }

        int atCount = 0;
        for(int i=0;i<reversedEmail.length();i++) {
            if(atCount > 1) {
                // name@name@email.com -> invalid
                return false;
            }
            if(reversedEmail.charAt(i) == '@') {
                atCount++;
            }
        }

        return true;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
