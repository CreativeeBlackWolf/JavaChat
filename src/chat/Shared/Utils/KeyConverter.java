package chat.Shared.Utils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyConverter {
    
    /** Преобразует {@code java.security.Key} и производные в строку
     * @param key исходный ключ
     * @return строку-ключ
     */
    public static String keyToString(Key key) {
        byte[] byteKey = key.getEncoded();
        return new String(Base64.getEncoder().encodeToString(byteKey));
    }

    /** Преобразует строку в {@code java.security.Key}, 
     * которую можно привести к {@code java.security.PublicKey}
     * или в {@code java.security.PrivateKey}, в зависимости от установленного параметра {@code isPrivate}
     * @param key строка-ключ
     * @param algorithm алгоритм, с помощью которого был сгенерирован изначальный ключ
     * @param isPrivate является ключ приватным или публичным
     * @return объект {@code java.Security.Key}
     */
    public static Key stringToKey(String key, String algorithm, boolean isPrivate) {
        byte[] byteKey = Base64.getDecoder().decode(key);
        try {
            KeyFactory factory = KeyFactory.getInstance(algorithm);
            Key convertedKey;
            if (isPrivate) {
                convertedKey = factory.generatePrivate(
                    algorithm == "RSA" ? new PKCS8EncodedKeySpec(byteKey) : new X509EncodedKeySpec(byteKey)
                );
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
