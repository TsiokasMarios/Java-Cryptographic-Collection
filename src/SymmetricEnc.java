import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;


public class SymmetricEnc {
    private static final String AES = "AES";

    public static SecretKey getSecretEncryptionKey() throws Exception{

        KeyGenerator generator = KeyGenerator.getInstance(AES);
        SecureRandom secureRandom = new SecureRandom();

        generator.init(256,secureRandom); // The AES key size in number of bits

        return generator.generateKey();
    }

    public static byte[] encryptText(String plainText,SecretKey secKey) throws Exception{

        Cipher aesCipher = Cipher.getInstance("AES");

        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);

        return aesCipher.doFinal(plainText.getBytes());

    }

    public static byte[] decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {

        Cipher aesCipher = Cipher.getInstance("AES");

        aesCipher.init(Cipher.DECRYPT_MODE, secKey);

        return aesCipher.doFinal(byteCipherText);
    }

    public static void main(String[] args){
        String toEncrypt = "";
        byte[] encrypted;
        byte[] decrypted;
        try {

            toEncrypt = Utils.extractMessage("toencrypt.txt");

            SecretKey key = getSecretEncryptionKey();
            encrypted = encryptText(toEncrypt,key);
            decrypted = decryptText(encrypted,key);
            System.out.println("Encrypted text: " + Utils.convertToHex(encrypted));
            System.out.println("Secret key: " + Utils.convertToHex(key.getEncoded()));
            System.out.println("Decrypted message: " + new String(decrypted));

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
