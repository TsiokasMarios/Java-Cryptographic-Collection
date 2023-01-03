import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class AsymmetricEnc {
    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    public static String encrypt(PublicKey publicKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherText = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Utils.encode(cipherText);
    }

    public static String decrypt(PrivateKey privateKey, String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] cipherText = cipher.doFinal(Utils.decode(encrypted));
        return new String(cipherText, StandardCharsets.UTF_8);
    }
}
