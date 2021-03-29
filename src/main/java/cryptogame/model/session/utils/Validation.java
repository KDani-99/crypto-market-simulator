package cryptogame.model.session.utils;

import java.util.regex.Pattern;

public abstract class Validation {

    private static final Pattern usernamePattern = Pattern.compile("[a-zA-Z0-9_.\\-]{4,50}$");
    private static final Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%\\^&\\*])(?=.{6,255})");

    public static boolean validateEmail(String email) {

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

    public static boolean validatePassword(String password) {
        return passwordPattern.matcher(password).find();
    }

    public static boolean validateUsername(String username) {
        return usernamePattern.matcher(username).find();
    }

}
