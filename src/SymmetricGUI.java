import org.bouncycastle.asn1.ASN1Absent;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.EventListener;

public class SymmetricGUI extends JPanel implements EventListener {
    JButton button;
    SecretKey secKey;
    Charset charset = StandardCharsets.US_ASCII;
    String encrypted;
    String decrypted;

    SymmetricGUI() {
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
//        this.setBackground(Color.RED);

        JButton genKey = new JButton("Generate secret key");
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.weighty = 0.1;
        c.gridx = 0;
        c.gridy = 0;
        this.add(genKey, c);


        JTextField secretKey = new JTextField("Secret key here");
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0, 20, 0, 20);
        this.add(secretKey, c);

        //Simulate the look and feel of a jlabel so we can select the text
        secretKey.setEditable(false);
        secretKey.setBorder(null);
        secretKey.setForeground(UIManager.getColor("Label.foreground"));
        secretKey.setFont(UIManager.getFont("Label.font"));

        genKey.addActionListener(e -> {
            try {
                secKey = SymmetricEnc.getSecretEncryptionKey();
                secretKey.setText(SymmetricEnc.keyToString(secKey));

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton bt3 = new JButton("save");
        c.gridx = 2;
        c.gridy = 0;
        this.add(bt3, c);

        JSeparator separator = new JSeparator();

        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(separator, c);

        JLabel encrypt = new JLabel("Encrypt");
        c.gridwidth = 3;
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(encrypt, c);

        JLabel enterClearText = new JLabel("Enter message to encrypt");
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 3;
        this.add(enterClearText, c);

        JTextField plainText = new JTextField();
        c.gridwidth = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        this.add(plainText, c);

        JButton encryptButton = new JButton("Encrypt");
        c.gridx =2;
        c.gridy = 3;
        this.add(encryptButton,c);


        JLabel encodedTextLabel = new JLabel("Encoded text:");
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 4;
        this.add(encodedTextLabel, c);


        JTextField encoded = new JTextField ("WAKBFKDA;bngsbhieurbgailvberg");
        encoded.setEditable(false);
        encoded.setBorder(null);
        encoded.setForeground(UIManager.getColor("Label.foreground"));
        encoded.setFont(UIManager.getFont("Label.font"));
        c.gridx = 1;
        c.gridy = 4;
        this.add(encoded, c);

        encryptButton.addActionListener(e -> {

            try {
                encrypted = SymmetricEnc.encryptText(plainText.getText(),secKey);
                encoded.setText(encrypted);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton saveEncrypted = new JButton("Save");
        c.gridx = 2;
        c.gridy = 4;
        this.add(saveEncrypted, c);

        JSeparator separator1 = new JSeparator();
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(separator1, c);

        JLabel decryptLB = new JLabel("Decrypt");
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 6;
        this.add(decryptLB, c);

        JLabel encryptedText = new JLabel("Enter text to decrypt");
        c.gridx = 0;
        c.gridy = 7;
        this.add(encryptedText, c);

        JTextField textToDecrypt = new JTextField();
        c.gridx = 1;
        c.gridy = 7;
        this.add(textToDecrypt, c);

        JLabel secretKeyLB = new JLabel("Enter secret key");
        c.gridx = 0;
        c.gridy = 8;
        this.add(secretKeyLB, c);

        JTextField enterKey = new JTextField();
        c.gridx = 1;
        c.gridy = 8;
        this.add(enterKey, c);

        JButton decrypt = new JButton("Decrypt");
        c.gridx = 2;
        c.gridy = 8;
        this.add(decrypt, c);

        JLabel decodedmsg = new JLabel("Decoded msg here");
        c.gridx = 1;
        c.gridy = 9;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(decodedmsg, c);

        decrypt.addActionListener(e -> {
            try {
                // rebuild key using SecretKeySpec
                SecretKey originalKey = SymmetricEnc.stringToAESKey(enterKey.getText());
                System.out.println(encryptedText.getText());
                String t = Base64.getEncoder().encodeToString(encryptedText.getText().getBytes());
                System.out.println(t);
                System.out.println(encryptedText.getText());
                decrypted = SymmetricEnc.decryptText(encryptedText.getText(),secKey);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            decodedmsg.setText(decrypted);
        });

    }
//    SymmetricGUI() {
////        this.setBackground(Color.RED);
//        int max_width = 600;
//
//        JButton genKey = new JButton("Generate secret key");
//        this.setLayout(null);
//
//        genKey.setBounds(0,0,100,50);
//
//        this.add(genKey);
//
//        JLabel secretKey = new JLabel("Secret key here");
//        secretKey.setBounds(200,0,100,50);
//        this.add(secretKey);
//
//        JButton bt3 = new JButton("save");
//        bt3.setBounds(max_width=100,0,100,50);
//        this.add(bt3);
//
//        JSeparator separator = new JSeparator();
//        separator.setBounds(0,70,max_width,20);
//
//        this.add(separator);
//
//        JLabel encrypt = new JLabel("Encrypt");
//
//        this.add(encrypt);
//
//        JLabel enterClearText = new JLabel("Enter message to encrypt");
//        this.add(enterClearText);
//
//        JTextField plainText = new JTextField();
//        this.add(plainText);
//
//        JLabel encodedText = new JLabel("Encoded text:");
//
//        this.add(encodedText);
//
//        JLabel encoded = new JLabel("WAKBFKDA;bngsbhieurbgailvberg");
//
//        this.add(encoded);
//
//
//        JButton saveEncrypted = new JButton("Save");
//
//        this.add(saveEncrypted);

//    }
}
