import java.io.*;
import java.nio.file.Files;
import java.security.*;


public class DS {
    public static KeyPair generateKeyPair(int keySize, String source) throws NoSuchAlgorithmException {
        //Create the key pair generator with the DSA algorithm
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
        //Create the source random generator with the provided source of randomness
        SecureRandom secureRandom = SecureRandom.getInstance(source);
        //Initialize the generator with the key size that was provided and the secure random generator
        kpg.initialize(keySize,secureRandom);
        //Generate the keypair and return it
        return kpg.generateKeyPair();
    }

    public static void initSignatureToSign(PrivateKey privateKey,Signature signature) throws InvalidKeyException {
        //Initialize the provided signature with the provided private key for signing
        signature.initSign(privateKey);
    }

    public static void initSignatureToVer(PublicKey publicKey, Signature signature) throws InvalidKeyException {
        //Initialize the provided signature with the provided private key for verification
        signature.initVerify(publicKey);
    }

    public static byte[] signData(String path,Signature signature) throws IOException, SignatureException {
        //Create a file input stream with the provided path
        FileInputStream fis = new FileInputStream(path);
        //Created a buffered input stream with the file input stream we created
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fis);
        //Create a 1024 size byte aray
        byte[] buffer = new byte[1024];
        int len;
        //Loop through the buffered input stream
        while ((len = bufferedInputStream.read(buffer)) >= 0) {
            //Sign the data with the provided signature
            signature.update(buffer, 0, len);
        }
        //Close the streams
        fis.close();
        bufferedInputStream.close();
        //return the signed data
        return signature.sign();
    }

    public static void saveSignature(String filename,byte[] signature) throws IOException {
        //Create a file output stream with the provided filename and save it as a .sig file
        FileOutputStream fileOutputStream = new FileOutputStream(filename+".sig");
        //Write the signature bytes and save
        fileOutputStream.write(signature);
        //Close the stream
        fileOutputStream.close();
    }

    public static void validateSignature(String filename, Signature signature) throws IOException, SignatureException {
        //Same as the method to sign the data
        FileInputStream fileInputStream = new FileInputStream(filename);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

        byte[] buffer = new byte[1024];
        int len;
        while (bufferedInputStream.available() != 0) {
            len = bufferedInputStream.read(buffer);
            signature.update(buffer, 0, len);
        }
    }

    public static byte[] loadSignature(String filename) throws IOException {
        //Get the file path
        File path = new File(filename);
        //Return the signature bytes
        return Files.readAllBytes(path.toPath());
    }

}
