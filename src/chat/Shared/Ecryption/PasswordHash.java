package chat.Shared.Ecryption;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHash {
    public PasswordHash(){

    }

    public static String getPasswordHash(String password) throws NoSuchAlgorithmException {

            MessageDigest digester = MessageDigest.getInstance("SHA-512");

            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
            byte[] salt = new byte[16];

            SecureRandom.getInstanceStrong().nextBytes(salt);
            digester.update(salt);
            digester.update(passwordBytes);

            byte[] hash = digester.digest();

        return Base64.getEncoder().encodeToString(hash);
    }
}