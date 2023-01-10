import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.cert.CertificateException;


public class SymmetricEnc {

    public static SecretKey generateSecretKey(int keySize, String source) throws Exception {
        //Create the generator with the AES algorithm
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        //Create the source random generator with the provided source of randomness
        SecureRandom secureRandom = SecureRandom.getInstance(source);
        //Initialize the generator with the key size that was provided and the secure random generator
        generator.init(keySize, secureRandom);
        //Generate the keypair and return it
        return generator.generateKey();
    }

    public static void storeKey(SecretKey keyToStore, String password, String name,String keystoreName) throws Exception{
        File file = new File(keystoreName+".keystore");
        //Create the keystore
        KeyStore javaKeyStore = KeyStore.getInstance("JCEKS");
        if(!file.exists()){
            javaKeyStore.load(null,null);
        }
        //Set the key entry in the keystore
        javaKeyStore.setKeyEntry(name,keyToStore,password.toCharArray(),null);
        //Save the file with the provided name
        OutputStream writeStream = new FileOutputStream(keystoreName+".keystore");
        //Enter the data
        javaKeyStore.store(writeStream, password.toCharArray());
        writeStream.close();
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

        //Create the cipher with the AES algorithm
        Cipher aesCipher = Cipher.getInstance("AES");
        //Initialize the cipher to encrypt with the provided secret key
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        //Encrypt the provided text with the created cipher
        byte[] cipherText = aesCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        //Encode the byte array with the encode method of the Utils class and return it
        return Utils.encode(cipherText);

    }

    public static String decryptText(String encrypted, SecretKey secKey) throws Exception {
        //Create the cipher with the AES algorithm
        Cipher aesCipher = Cipher.getInstance("AES");
        //Initialize the cipher to decrypt with the provided secret key
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        //Decode the encrypted text with the decode method from the Utils class
        //Then decrypt it
        byte[] cipherText = aesCipher.doFinal(Utils.decode(encrypted));
        //Return the text as a string
        return new String(cipherText);
    }

    public static SecretKey stringToAESKey(String keyString){
        //Declare the secret key
        SecretKey key;
        try {
            //Using the decode method from the Utils class decode the key string and store it in a byte aray
            byte[] encodedKey = Utils.decode(keyString);
            //Create the key
            key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
            //And return it
            return key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
