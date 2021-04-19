package cryptogame.service.auth;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public abstract class AuthService {

    private static final int saltSize = 16;

    private static byte[] generateRandomSalt(int size) {
        SecureRandom rnd = new SecureRandom();
        return rnd.generateSeed(size);
    }
    private static String hmacHelper(byte [] password,byte [] key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec keySpec = new SecretKeySpec(key,"HmacSHA256");
        var hmacInstance = Mac.getInstance("HmacSHA256");
        hmacInstance.init(keySpec);
        return "$" + saltSize + "$" + Hex.encodeHexString(key) + Hex.encodeHexString(hmacInstance.doFinal(password));
    }

    public static String generatePasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeyException {
        var key = generateRandomSalt(saltSize);
        return hmacHelper(password.getBytes(),key);
    }
    public static String generatePasswordHash(String password, byte [] key) throws NoSuchAlgorithmException, InvalidKeyException {
        return hmacHelper(password.getBytes(),key);
    }

    public static boolean comparePasswords(String stored, String plain) throws NoSuchAlgorithmException, InvalidKeyException, DecoderException {
        var splitted = stored.split("\\$");

        int saltSize = Integer.parseInt(splitted[1]);

        var salt = Arrays.copyOfRange(Hex.decodeHex(splitted[2]),0,saltSize);

        return MessageDigest.isEqual(
                stored.getBytes(),
                generatePasswordHash(plain,salt).getBytes()
        );
    }

}
