package cryptogame.model.exceptions;

public class InvalidUsernameException extends Exception {
    public InvalidUsernameException(int min, int max) {
        super("Username may only contains alphanumeric characters and must be between "+min+" and "+max +" characters");
    }
}
