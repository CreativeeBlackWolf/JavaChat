package chat.Shared.Encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class MessageEncryption {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public MessageEncryption() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keypair = keyPairGenerator.generateKeyPair();

        privateKey = keypair.getPrivate();
        publicKey = keypair.getPublic();
    }

    public String encrypt(String message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
                                                 IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessage = cipher.doFinal(messageBytes);

        return Base64.getEncoder().encodeToString(encryptedMessage);
    }

    public String decrypt(String message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] messageBytes = Base64.getDecoder().decode(message);
        byte[] decryptedMessage = cipher.doFinal(messageBytes);

        return new String(decryptedMessage, StandardCharsets.UTF_8);
    }

}
