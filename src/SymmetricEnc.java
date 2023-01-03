import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.cert.CertificateException;


public class SymmetricEnc {
    private static final String AES = "AES";

    public static SecretKey getSecretEncryptionKey() throws Exception {

        KeyGenerator generator = KeyGenerator.getInstance(AES);
        SecureRandom secureRandom = new SecureRandom();

        generator.init(256, secureRandom); // The AES key size in number of bits

        System.out.println(generator.generateKey().getEncoded().length);
        return generator.generateKey();
    }

    public static void storeKey(SecretKey keyToStore, String password, String name,String keystoreName) throws Exception{
        File file = new File(keystoreName+".keystore");
        KeyStore javaKeyStore = KeyStore.getInstance("JCEKS");
        if(!file.exists()){
            javaKeyStore.load(null,null);
        }
        javaKeyStore.setKeyEntry(name,keyToStore,password.toCharArray(),null);
        OutputStream writeStream = new FileOutputStream(keystoreName+".keystore");
        javaKeyStore.store(writeStream, password.toCharArray());
    }

    public static SecretKey retrieveFromKeyStore(String password, String name) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {

        //Create keystore instance
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        //Get keystore file from file system
        InputStream readStream = new FileInputStream(name + ".keystore");
        //Load the keystore
        keyStore.load(readStream, password.toCharArray());
        //Return the secret key
        return (SecretKey) keyStore.getKey(name, password.toCharArray());
    }

    public static String encryptText(String plainText,SecretKey secKey) throws Exception{

        Cipher aesCipher = Cipher.getInstance("AES");

        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);

        byte[] cipherText = aesCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Utils.encode(cipherText);

    }

    public static String decryptText(String encrypted, SecretKey secKey) throws Exception {

        Cipher aesCipher = Cipher.getInstance("AES");

        aesCipher.init(Cipher.DECRYPT_MODE, secKey);

        byte[] cipherText = aesCipher.doFinal(Utils.decode(encrypted));
        return new String(cipherText);
    }

    public static SecretKey stringToAESKey(String keyString){
        SecretKey key;
        try {
            byte[] encodedKey = Utils.decode(keyString);
            key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
            return key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
