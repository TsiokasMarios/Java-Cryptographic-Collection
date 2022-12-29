import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Base64;


public class SymmetricEnc {
    private static final String AES = "AES";

    public static SecretKey getSecretEncryptionKey() throws Exception{

        KeyGenerator generator = KeyGenerator.getInstance(AES);
        SecureRandom secureRandom = new SecureRandom();

        generator.init(256,secureRandom); // The AES key size in number of bits

        return generator.generateKey();
    }

    public static void storeKey(SecretKey keyToStore, String password, String filepath,String name) throws Exception{
        File file = new File(filepath);
        KeyStore javaKeyStore = KeyStore.getInstance("JCEKS");
        if(!file.exists()){
            javaKeyStore.load(null,null);
        }
        javaKeyStore.setKeyEntry(name,keyToStore,password.toCharArray(),null);
        OutputStream writeStream = new FileOutputStream(filepath);
        javaKeyStore.store(writeStream, password.toCharArray());
    }

    public static SecretKey retrieveFromKeyStore(String filepath,String password,String name){
        try{
            KeyStore keyStore = KeyStore.getInstance("JCEKS");
            InputStream readStream = new FileInputStream(filepath);
            keyStore.load(readStream,password.toCharArray());
            return (SecretKey) keyStore.getKey(name,password.toCharArray());
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public static String encryptText(String plainText,SecretKey secKey) throws Exception{

        Cipher aesCipher = Cipher.getInstance("AES");

        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);

        byte[] cipherText = aesCipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(cipherText);

    }

    public static String decryptText(String CipherText, SecretKey secKey) throws Exception {

        Cipher aesCipher = Cipher.getInstance("AES");

        aesCipher.init(Cipher.DECRYPT_MODE, secKey);

        byte[] cipherText = aesCipher.doFinal(Base64.getDecoder().decode(CipherText));
        return new String(cipherText);
    }

    public static void main(String[] args){
        String wah = "wah";
        String toEncrypt;
        String toDecrypt;
        String encrypted;
        String decrypted;
        try {




//            SecretKey key = getSecretEncryptionKey();
//            encrypted = encryptText(toEncrypt,key);
//            decrypted = decryptText(encrypted,key);
//            System.out.println("Encrypted text: " + Utils.convertToHex(encrypted));
//            System.out.println("Secret key: " + Utils.convertToHex(key.getEncoded()));
//            System.out.println("Decrypted message: " + new String(decrypted));


            //Store key testing

            //Generate secret key

            SecretKey key = getSecretEncryptionKey();
            //Store key in keystore
            storeKey(key,"1234","keystore.keystore","key1");

            //Retrieve message to encrypt

            //Encrypt message
            encrypted = encryptText(wah,key);

            //Save encrypted text
            Utils.saveEncryptedText("text1",encrypted);

            SecretKey key2 = retrieveFromKeyStore("keystore.keystore","1234","key1");
            String w3 = Utils.extractMessage("text1.txt");
            //Decrypt message with generated key
            byte[] ar = Utils.stringToBytes(w3);
//            System.out.println("Encrypted length " + encrypted.length);
//            System.out.println("Encrypted from file length " + ar.length);

//            System.out.println(Utils.stringToBytes(w3).toString());
//            System.out.println(encrypted.toString());

            System.out.println("w3 " + w3);
            byte[] w33 = Utils.stringToBytes(w3);
            System.out.println("w33 length " + w33.length);

//            String w2 = Utils.convertToHex(encrypted);
//            System.out.println("\nw2 " + w2);
//            byte[] w22 = Utils.stringToBytes(w2);
//            System.out.println("w22 length " + w22.length);

            decrypted = decryptText(encrypted,key);
            System.out.println("Decrypted message 1: " + decrypted);
            String decrypted2 = decryptText(w3, key2);
            System.out.println("Decrypted message 2: " + decrypted2);
//            decrypted = decryptText(w33,key);


            //Print stuff
//            System.out.println("Encrypted text: " + Utils.convertToHex(encrypted));
//            System.out.println("Secret key1: " + Utils.convertToHex(key.getEncoded()));
//            System.out.println("Secret key2: " + Utils.convertToHex(key2.getEncoded()));
//            System.out.println("Decrypted message: " + new String(decrypted));



        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
