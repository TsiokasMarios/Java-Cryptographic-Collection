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

    public static void saveEncryptedText(String name,String text) throws FileNotFoundException{
        try
        {
            //Create a new writer with the provided name and add the .txt extension
            FileWriter myWriter = new FileWriter(name+".txt");
            //Write the contents to the file
            myWriter.write(text);
            //Close the writer
            myWriter.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String extractMessage(String filePath){
        //Create a new string builder
        StringBuilder message = new StringBuilder();
        try {
            //Create a buffered reader with a filereader with the provided filepath
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            //Read each line until there are no more
            while ((line = br.readLine()) != null) {
                // append it to the string builder message that we created
                message.append(line);
            }
            //Close the buffered red
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Convert the message to string and convert it
        return message.toString();
    }


    public static void savePEM(KeyPair keyPair, String filename) throws IOException {
        //Cals the privatePEM and publicPEM methods to save the keypair
        Utils.privatePEM(keyPair.getPrivate(),filename);
        Utils.publicPEM(keyPair.getPublic(),filename);
    }

    private static void privatePEM(PrivateKey privateKey,String filename) throws IOException {
        //Create a pem object with the provided private key
        PemObject pemObject = new PemObject("PRIVATE KEY",privateKey.getEncoded());
        //Create a byte array output stream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //Create a pemwriter with the byte array output stream we created
        PemWriter pemWriter = new PemWriter(new OutputStreamWriter(byteArrayOutputStream));
        //Write the pem object to the pem writer
        pemWriter.writeObject(pemObject);
        //Close the writer
        pemWriter.close();
        //Create a file writer with the provided filename and add _private to signify it is the private key
        //And add the .pem extension to it
        FileWriter fileWriter = new FileWriter(filename+"_private.pem");
        //Write the byte array output stream to the file writer after converting it to a string
        fileWriter.write(byteArrayOutputStream.toString());
        //Close the stream and the writer
        byteArrayOutputStream.close();
        fileWriter.close();
    }

    private static void publicPEM(PublicKey privateKey, String filename) throws IOException {
        //Same procedure as for the privatePEM method except we are saving a public key instead of private
        PemObject pemObject = new PemObject("PUBLIC KEY",privateKey.getEncoded());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PemWriter pemWriter = new PemWriter(new OutputStreamWriter(byteArrayOutputStream));
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        FileWriter writer = new FileWriter(filename+"_public.pem");
        writer.write(byteArrayOutputStream.toString());
        writer.close();
    }

    //Gets a byte array and returns a string encoded in Base64
    public static String encode (byte[] toEncode){
        return Base64.getEncoder().encodeToString(toEncode);
    }

    //Gets a string and returns byte array decoded in Base64
    public static byte[] decode (String toDecode){
        return Base64.getDecoder().decode(toDecode);
    }

    public static String getPrivateKeyPem(String filename) throws IOException {
        //Create a string builder
        StringBuilder strKeyPEM = new StringBuilder();
        //Create a buffered reader with a filereader with the provided filename
        BufferedReader br = new BufferedReader(new FileReader(filename));
        br.readLine(); // this will read the first line because we do not need it
        String line;
        //Keep reading lines until it does not find one
        while ((line = br.readLine()) != null) {
            //Append the line to the string builder
            strKeyPEM.append(line);
        }
        //Close the reader
        br.close();
        //Remove the last line of the string because it is not needed
        strKeyPEM = new StringBuilder(strKeyPEM.toString().replace("-----END PRIVATE KEY-----", ""));
        //Convert the string builder to a string and return it
        return strKeyPEM.toString();
    }

    public static String getPublicKeyPem(String filename) throws IOException {
        //Same procedure as the getPrivateKeyPem method

        StringBuilder strKeyPEM = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        br.readLine();
        String line;
        while ((line = br.readLine()) != null) {
            strKeyPEM.append(line);
        }
        br.close();
        strKeyPEM = new StringBuilder(strKeyPEM.toString().replace("-----END PUBLIC KEY-----", ""));

        return strKeyPEM.toString();
    }

    public static PublicKey getPublicKeyFromString(String publicKeyString,String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //Create a new public key specification with the provided public key string after decoding
        X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(Utils.decode(publicKeyString));
        //Create a new keyfactory with the provided algorithm
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        //Generate the public key with the keyspec we created and return it
        return keyFactory.generatePublic(keySpecPublic);
    }

    public static PrivateKey getPrivateKeyFromString(String privateKeyString, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //Create a new private key specification with the provided public key string after decoding
        PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(Utils.decode(privateKeyString));
        //Create a new keyfactory with the provided algorithm
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        //Generate the public key with the keyspec we created and return it
        return keyFactory.generatePrivate(keySpecPrivate);
    }

    public static String keyToString(Key secretKey) {
        // Get key in encoding format
        byte[] encoded = secretKey.getEncoded();
        //Encode the byte array to a string
        return Utils.encode(encoded);
    }
}

