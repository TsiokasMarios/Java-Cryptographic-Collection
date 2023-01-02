import org.bouncycastle.crypto.fips.FipsDRBG;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class AssymetricEnc {
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


    public static void main(String[] args) throws Exception {
        String toCode = "wah";

        String pub = Utils.getPublicKeyPem("keypair1_public.pem");
        String priv = Utils.getPrivateKeyPem("keypair1_private.pem");

        PrivateKey privateKey = Utils.getPrivateKeyFromString(priv);
        PublicKey publicKey = Utils.getPublicKeyFromString(pub);

        System.out.println();

        String encrypted = encrypt(publicKey,toCode);

        String decoded = decrypt(privateKey,encrypted);

        System.out.println(decoded);

//        System.out.println(Utils.keyToString(publicKey));
    }
}
