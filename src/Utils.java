import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class Utils {
    public static String convertToHex(byte[] text){
        StringBuilder msg = new StringBuilder();
        for (byte b : text) {
            msg.append(Integer.toHexString(0xff & b));
        }
        return msg.toString();
    }

    public static void saveEncryptedText(String name,String text) throws FileNotFoundException{
        try
        {
            FileWriter myWriter = new FileWriter(name+".txt");
            myWriter.write(text);
            myWriter.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String extractMessage(String filePath){
        StringBuilder message = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                // extract the message
                message.append(line);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("message " + message);
        return message.toString();
    }

    public static byte[] stringToBytes(String string) {
        char[] chars = string.toCharArray();

        byte[] bytes = new byte[chars.length];

        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) chars[i];
        }
        return bytes;
    }


    public static String keyToHex(Key secretKey){
        StringBuilder hexKey = new StringBuilder();
        for (int i=0;i<secretKey.getEncoded().length;i++)
            hexKey.append(Integer.toHexString(0xFF & secretKey.getEncoded()[i]));
        return hexKey.toString();
    }

    public static void savePEM(KeyPair keyPair, String filename) throws IOException {
        Utils.privatePEM(keyPair.getPrivate(),filename);
        Utils.publicPEM(keyPair.getPublic(),filename);
    }

    private static void privatePEM(PrivateKey privateKey,String filename) throws IOException {
        PemObject pemObject = new PemObject("PRIVATE KEY",privateKey.getEncoded());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PemWriter pemWriter = new PemWriter(new OutputStreamWriter(byteArrayOutputStream));
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        FileWriter fileWriter = new FileWriter(filename+"_private.pem");
        fileWriter.write(byteArrayOutputStream.toString());
        fileWriter.close();
    }

    private static void publicPEM(PublicKey privateKey, String filename) throws IOException {
        PemObject pemObject = new PemObject("PUBLIC KEY",privateKey.getEncoded());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PemWriter pemWriter = new PemWriter(new OutputStreamWriter(byteArrayOutputStream));
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        FileWriter writer = new FileWriter(filename+"_public.pem");
        writer.write(byteArrayOutputStream.toString());
        writer.close();
    }

    public static String encode (byte[] toEncode){
        return Base64.getEncoder().encodeToString(toEncode);
    }

    public static byte[] decode (String toDecode){
        return Base64.getDecoder().decode(toDecode);
    }


    public static String getPrivateKeyPem(String filename) throws IOException {
        // Read key from file

        StringBuilder strKeyPEM = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        br.readLine(); // this will read the first line
        String line;
        while ((line = br.readLine()) != null) {
            strKeyPEM.append(line);
        }
        br.close();
        strKeyPEM = new StringBuilder(strKeyPEM.toString().replace("-----END PRIVATE KEY-----", ""));

        return strKeyPEM.toString();
    }

    public static String getPublicKeyPem(String filename) throws IOException {
        // Read key from file

        StringBuilder strKeyPEM = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        br.readLine(); // this will read the first line
        String line;
        while ((line = br.readLine()) != null) {
            strKeyPEM.append(line);
        }
        br.close();
        strKeyPEM = new StringBuilder(strKeyPEM.toString().replace("-----END PUBLIC KEY-----", ""));

        return strKeyPEM.toString();
    }

    public static PublicKey getPublicKeyFromString(String publicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(Utils.decode(publicKeyString));

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(keySpecPublic);
    }

    public static PrivateKey getPrivateKeyFromString(String privateKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(Utils.decode(privateKeyString));

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePrivate(keySpecPrivate);
    }

    public static String keyToString(Key secretKey) {
        /* Get key in encoding format */
        byte[] encoded = secretKey.getEncoded();

        /*
         * Encodes the specified byte array into a String using Base64 encoding
         * scheme
         */

        return Utils.encode(encoded);
    }
}

