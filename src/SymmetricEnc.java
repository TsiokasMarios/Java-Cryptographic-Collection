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
        String toEncrypt = "Wah";
        byte[] encrypted;
        byte[] decrypted;
        try {
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



//        try {
//            SecretKey AESKey = SymmetricEnc.createAESKey();
//            StringBuffer hexKey = new StringBuffer();
//
//            System.out.println(hexKey);
//
//            Cipher AESCipher = Cipher.getInstance(AES);
//            AESCipher.init(Cipher.ENCRYPT_MODE,AESKey);
//
//            byte[] coded;
//
//            coded = AESCipher.doFinal(toCode.getBytes());
//
//            System.out.println(coded);
//
//            AESCipher = Cipher.getInstance(AES);
//            AESCipher.init(Cipher.DECRYPT_MODE,AESKey);
//
//            byte[] result = AESCipher.doFinal(coded);
//            StringBuffer hexCipher = new StringBuffer();
//            for (int i = 0; i < result.length; i++) {
//                hexCipher.append(Integer.toHexString(0xff & result[i]));
//            }
//            System.out.println(hexCipher);
//
//
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }

    }
}
