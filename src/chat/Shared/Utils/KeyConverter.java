package chat.Shared.Utils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyConverter {
    
    public static String keyToString(Key key) {
        byte[] byteKey = key.getEncoded();
        return new String(Base64.getEncoder().encodeToString(byteKey));
    }

    public static Key stringToKey(String key, String algorithm, boolean isPrivate) {
        byte[] byteKey = Base64.getDecoder().decode(key);
        try {
            KeyFactory factory = KeyFactory.getInstance(algorithm);
            Key convertedKey;
            if (isPrivate) {
                convertedKey = factory.generatePrivate(algorithm == "RSA" ? new PKCS8EncodedKeySpec(byteKey) : new X509EncodedKeySpec(byteKey));
            }
            else {
                convertedKey = factory.generatePublic(new X509EncodedKeySpec(byteKey));
            }
            return convertedKey;
        } catch (NoSuchAlgorithmException | 
                 InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

}
