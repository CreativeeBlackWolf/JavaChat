package chat.Shared.Encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class MessageEncryption {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public MessageEncryption(){
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            KeyPair keypair = keyPairGenerator.generateKeyPair();

            privateKey = keypair.getPrivate();
            publicKey = keypair.getPublic();

        } catch(NoSuchAlgorithmException e){
            e.getMessage();
        }
    }

    public String encrypt(String message, PublicKey publicKey) {
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

    public PrivateKey getPrivateKey(){
        return privateKey;
    }

    public PublicKey getPublicKey(){
        return publicKey;
    }
}
