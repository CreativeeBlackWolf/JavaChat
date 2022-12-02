package chat.Shared.Ecryption;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordHash {
    public PasswordHash(){

    }

    public static byte[] generateSalt(){
        byte[] generatedSalt = new byte[16];
        try {
            SecureRandom.getInstanceStrong().nextBytes(generatedSalt);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return generatedSalt;
    }

    public static String getPasswordHash(String password, byte[] salt) {
        try{
            MessageDigest digester = MessageDigest.getInstance("SHA-512");

            byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);

            digester.update(salt);
            digester.update(passwordBytes);

            byte[] hash = digester.digest();
            BigInteger hashInt = new BigInteger(1,hash);

            return hashInt.toString(16);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}