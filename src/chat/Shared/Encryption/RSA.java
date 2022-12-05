package chat.Shared.Encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

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
     * @param keysize -- размер ключа (должен быть степенью двойки)
     * @throws NoSuchAlgorithmException
     */
    public void generateKeyPair(int keysize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keypair = keyPairGenerator.generateKeyPair();

        this.privateKey = keypair.getPrivate();
        this.publicKey = keypair.getPublic();
    }

    public String encrypt(String message) {
        try{
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

    public String decrypt(String message) {
        try{
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] messageBytes = Base64.getDecoder().decode(message);
            byte[] decryptedMessage = cipher.doFinal(messageBytes);

            return new String(decryptedMessage, StandardCharsets.UTF_8);

        } catch(NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException e){
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