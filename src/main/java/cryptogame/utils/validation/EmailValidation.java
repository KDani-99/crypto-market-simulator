package cryptogame.utils.validation;

/**
 * Validation class for email type fields.
 * Implements {@link Validation<String>} interface.
 */
public class EmailValidation implements Validation<String> {

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public boolean validate(String email) {

        if(email == null) {
            return false;
        }

        if(email.length() > 125 || email.length() < 6) {
            return false;
        }

        if(email.indexOf(' ') > -1) {
            return false;
        }

        StringBuilder reversedEmail = new StringBuilder(email).reverse();

        var lastDotIndex = reversedEmail.indexOf(".");
        var lastAtIndex = reversedEmail.indexOf("@");

        if(reversedEmail.substring(0,lastDotIndex).length() < 2) {
            // name@email.c -> invalid
            // name@email.co -> valid
            return false;
        }

        if(reversedEmail.substring(lastDotIndex + 1,lastAtIndex).length() < 2) {

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

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorMessage() {
        return "Invalid email address.";
    }
}
