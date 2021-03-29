package cryptogame.model.exceptions;

public class PasswordMismatchException extends Exception {
    public PasswordMismatchException() {
        super("Passwords must be the same");
    }
}
