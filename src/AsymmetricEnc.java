import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class AsymmetricEnc {
    public static KeyPair generateKeyPair(int keySize, String source) throws NoSuchAlgorithmException {
        //Declare the key pair generator and get the RSA instance
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //Create a secure random number generator with the provided source of randomness
        SecureRandom secureRandom = SecureRandom.getInstance(source);
        //Initialize the generator
        keyPairGenerator.initialize(keySize,secureRandom);
        //Generate the key pair with the generator and return it
        return keyPairGenerator.genKeyPair();
    }

    public static String encrypt(PublicKey publicKey, String message) throws Exception {
        //Declare the cipher and get the RSA instance
        Cipher cipher = Cipher.getInstance("RSA");
        //Initialize the cipher to encryption mode
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        //Encrypt the provided message with the cipher and store it in a byte array
        byte[] cipherText = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        //Convert the byte array to a string with the method from the Utils class
        return Utils.encode(cipherText);
    }

    public static String decrypt(PrivateKey privateKey, String encrypted) throws Exception {
        //Declare the cipher and get the RSA instance
        Cipher cipher = Cipher.getInstance("RSA");
        //Initialize the cipher to encryption mode
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //Call the decode method from the Utils class to decode the provided encrypted string
        byte[] decodedText = Utils.decode(encrypted);
        //Decrypt the converted message with the cipher
        byte[] cipherText = cipher.doFinal(decodedText);
        //Return the decrypted text as a string
        return new String(cipherText, StandardCharsets.UTF_8);
    }
}
