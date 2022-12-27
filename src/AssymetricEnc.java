import javax.crypto.Cipher;
import java.security.*;

public class AssymetricEnc {
    public static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }

    public static byte[] encrypt(PrivateKey privateKey, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(message.getBytes());
    }

    public static byte[] decrypt(PublicKey publicKey, byte [] encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(encrypted);
    }

    public static void main(String[] args) throws Exception {
        String toCode = "wah";

        KeyPair keyPair = buildKeyPair();
        byte[] encrypted = encrypt(keyPair.getPrivate(),toCode);

        byte[] decrypted = decrypt(keyPair.getPublic(),encrypted);


    }

    public static void printEncryptedText(byte[] text){
        StringBuilder msg = new StringBuilder();
        for (byte b : text) {
            msg.append(Integer.toHexString(0xff & b));
        }
        System.out.println("Encrypted text: " + msg);
    }
}
