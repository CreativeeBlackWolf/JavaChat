package chat.Shared.Security;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RSA() {
        try {
            generateKeyPair(1024);
        } catch(NoSuchAlgorithmException e) {
            e.getMessage();
        }
    }

    public RSA(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /** Генерирует и моментально применяет новые ключи шифрования
     * @param keysize -- размер ключа
     * @throws NoSuchAlgorithmException
     */
    public void generateKeyPair(int keysize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(4096);
        KeyPair keypair = keyPairGenerator.generateKeyPair();

        this.privateKey = keypair.getPrivate();
        this.publicKey = keypair.getPublic();
    }

    public String encrypt(String message) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedMessage = cipher.doFinal(messageBytes);
            return Base64.getEncoder().encodeToString(encryptedMessage);

        } catch(NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException e){
            return e.getMessage();
        }
    }

    public String encrypt(String message, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            byte[] encryptedMessage = cipher.doFinal(messageBytes);
            return Base64.getEncoder().encodeToString(encryptedMessage);

        } catch(NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException e){
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String message) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] messageBytes = Base64.getDecoder().decode(message);
            byte[] decryptedMessage = cipher.doFinal(messageBytes);

            return new String(decryptedMessage, StandardCharsets.UTF_8);

        } catch(NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException e){
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String message, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] messageBytes = Base64.getDecoder().decode(message);
            byte[] decryptedMessage = cipher.doFinal(messageBytes);

            return new String(decryptedMessage, StandardCharsets.UTF_8);

        } catch(NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException e) {
            return e.getMessage();
        }
    }

    public PublicKey getPublicKey(){
        return publicKey;
    }
    
    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
