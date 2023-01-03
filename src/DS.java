import java.io.*;
import java.nio.file.Files;
import java.security.*;


public class DS {

    public static Signature createSignature() throws NoSuchAlgorithmException {
        return Signature.getInstance("DSA");
    }

    public static KeyPair buildKeyPair(int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
        SecureRandom secureRandom = new SecureRandom();
        kpg.initialize(keySize,secureRandom);
        return kpg.generateKeyPair();
    }

    public static void initSignatureToSign(PrivateKey privateKey,Signature signature) throws InvalidKeyException {
        signature.initSign(privateKey);
    }

    public static void initSignatureToVer(PublicKey publicKey, Signature signature) throws InvalidKeyException {
        signature.initVerify(publicKey);
    }

    public static byte[] signData(String path,Signature signature) throws IOException, SignatureException {
        FileInputStream fis = new FileInputStream(path);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fis);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = bufferedInputStream.read(buffer)) >= 0) {
            signature.update(buffer, 0, len);
        }
        bufferedInputStream.close();
        return signature.sign();
    }

    public static void saveSignature(String filename,byte[] signature) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filename+".sig");
        fileOutputStream.write(signature);
        fileOutputStream.close();
    }

    public static void validateSignature(String filename, Signature signature) throws IOException, SignatureException {
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
        File path = new File(filename);
        return Files.readAllBytes(path.toPath());
    }

    public static void main(String[] args)  throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
        String msg = "Wah";
        String wrong = "Nah";

        //Generate key pair

        KeyPair keyPair = buildKeyPair(512);

        PublicKey pub = keyPair.getPublic();
        PrivateKey priv = keyPair.getPrivate();

//        StringWriter write = new StringWriter();
//        PemWriter pemWriter = new PemWriter(write);
//        pemWriter.writeObject(new PemObject("PUBLIC KEY",pub.getEncoded()));

        //Save public key in pem file


        //Create signature
        Signature sig = createSignature();
        initSignatureToSign(priv,sig);

//        System.out.println(sig.);


        //Sign the message

        byte[] sign = signData("text1.txt",sig);

        saveSignature("C:\\Users\\mario\\Desktop\\wah",sign);

        //Validate signature
        Signature clientSig = createSignature();

        initSignatureToVer(pub,clientSig);


        validateSignature("text1.txt",clientSig);

        byte[] signature2 = loadSignature("wah");

        if (clientSig.verify(signature2)){
            System.out.println("all good");
        }
        else
            System.out.println("nah son");
    }

}
