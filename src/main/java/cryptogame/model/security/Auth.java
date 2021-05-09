package cryptogame.model.security;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * AuthService is responsible for securing sensitive user information.
 * This class is abstract and can not be instantiated.
 */
public abstract class Auth {

    /**
     * Static final salt size.
     */
    private static final int saltSize = 16;

    /**
     * Generates cryptographically random salt with the given {@code size}.
     *
     * @param size the size of the salt
     * @return salt bytes array
     */
    private static byte[] generateRandomSalt(int size) {
        SecureRandom rnd = new SecureRandom();
        return rnd.generateSeed(size);
    }

    /**
     * A helper method that returns an HmacSHA256 signed password, combined with the random salt.
     * Each part of the {@link String} is separated by $ delimiter.
     *
     * @param password the bytes of the user's plain text password
     * @param key the key that will be used to sign the password
     * @return hmac signed password
     * @throws NoSuchAlgorithmException if no such algorithm exists
     * @throws InvalidKeyException if the key is invalid
     */
    private static String hmacHelper(byte [] password,byte [] key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec keySpec = new SecretKeySpec(key,"HmacSHA256");
        var hmacInstance = Mac.getInstance("HmacSHA256");
        hmacInstance.init(keySpec);
        return "$" + saltSize + "$" + Hex.encodeHexString(key) + Hex.encodeHexString(hmacInstance.doFinal(password));
    }

    /**
     * Generates the password hash for the given plain text password with a random key.
     *
     * @param password the user's plain text password
     * @return hashed password
     * @throws NoSuchAlgorithmException if no such algorithm exists
     * @throws InvalidKeyException if the key is invalid
     */
    public static String generatePasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeyException {
        var key = generateRandomSalt(saltSize);
        return hmacHelper(password.getBytes(),key);
    }

    /**
     * Generates the password hash for the given plain text password with a {@code key}.
     *
     * @param password the user's plain text password
     * @param key key
     * @return hashed password
     * @throws NoSuchAlgorithmException if no such algorithm exists
     * @throws InvalidKeyException if the key is invalid
     */
    private static String generatePasswordHash(String password, byte [] key) throws NoSuchAlgorithmException, InvalidKeyException {
        return hmacHelper(password.getBytes(),key);
    }

    /**
     * Compares a plain text password and a (stored) hashed password.
     * The implementation of this method allows the change of the salt size
     * so old password can still be compared.
     *
     * @param stored
     * @param plain
     * @return {@code true} if the passwords are equal, {@code false} otherwise
     * @throws NoSuchAlgorithmException if no such algorithm exists
     * @throws InvalidKeyException if the key is invalid
     * @throws DecoderException if the decoder throws an exception
     */
    public static boolean comparePasswords(String stored, String plain) throws NoSuchAlgorithmException, InvalidKeyException, DecoderException {
        var splittedPassword = stored.split("\\$");

        int saltSize = Integer.parseInt(splittedPassword[1]);

        var salt = Arrays.copyOfRange(Hex.decodeHex(splittedPassword[2]),0,saltSize);

        return MessageDigest.isEqual(
                stored.getBytes(),
                generatePasswordHash(plain,salt).getBytes()
        );
    }

}
