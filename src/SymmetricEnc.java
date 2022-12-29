import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.SecureRandom;


public class SymmetricEnc {
    private static final String AES = "AES";


    public static SecretKey getSecretEncryptionKey() throws Exception{

        KeyGenerator generator = KeyGenerator.getInstance(AES);
        SecureRandom secureRandom = new SecureRandom();

        generator.init(256,secureRandom); // The AES key size in number of bits

        System.out.println(generator.generateKey().getEncoded().length);
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

    public static SecretKey stringtokey(String keyString){
        Key key;
        try {
            key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "AES");
            return (SecretKey)key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args){
        String toEncrypt = "";
        byte[] encrypted;
        byte[] decrypted;
        Charset charset = Charset.forName("ASCII");
        try {

            toEncrypt = Utils.extractMessage("toencrypt.txt");

            SecretKey key = getSecretEncryptionKey();
            encrypted = encryptText(toEncrypt,key);
            decrypted = decryptText(encrypted,key);
//            System.out.println("Encrypted text: " + Utils.convertToHex(encrypted).length());
//            System.out.println("Secret key: " + Utils.convertToHex(key.getEncoded()));
//            System.out.println("Decrypted message: " + new String(decrypted));

            String testKey = String.valueOf(Utils.convertToHex(key.getEncoded()));
            byte[] keybytes = testKey.getBytes(charset);
            System.out.println(keybytes.length);
//            byte[] test = key.getEncoded();
//            System.out.println(test.length);
//            SecretKey originalKey = new SecretKeySpec(test, 0, test.length, "AES");


        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
