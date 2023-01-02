import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.*;

public class sign {


    public static void main(String[] args)  throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
        String msg = "Wah";
        String wrong = "Nah";

        //Generate key pair
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
        SecureRandom secureRandom = new SecureRandom();
        kpg.initialize(512,secureRandom);
        KeyPair keyPair = kpg.generateKeyPair();
        PublicKey pub = keyPair.getPublic();
        PrivateKey priv = keyPair.getPrivate();

//        StringWriter write = new StringWriter();
//        PemWriter pemWriter = new PemWriter(write);
//        pemWriter.writeObject(new PemObject("PUBLIC KEY",pub.getEncoded()));

        //Save public key in pem file
        //


        //Create signature
        Signature sig = Signature.getInstance("DSA");
        sig.initSign(priv);

        //Sign the message
        sig.update(msg.getBytes());
        byte[] sign = sig.sign();

        //Validate signature
        Signature clientSig = Signature.getInstance("DSA");
        clientSig.initVerify(pub);
        clientSig.update(msg.getBytes());
        if (clientSig.verify(sign)){
            System.out.println("all good");
        }
        else
            System.out.println("nah son");
        new myFrame();
    }

}
