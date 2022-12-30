import org.bouncycastle.asn1.ASN1Absent;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
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
    Boolean loadedFromStore = false;

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

        JButton bt3 = new JButton("save key");
        c.gridx = 2;
        c.gridy = 0;
        this.add(bt3, c);
        bt3.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField pw = new JTextField();
            Object[] fields = {
                    "Enter key name", name,
                    "Enter password", pw,
            };
            JOptionPane.showConfirmDialog(null,fields,"Save key",JOptionPane.OK_CANCEL_OPTION);

            System.out.println(name.getText());
            System.out.println(pw.getText());

            try {
                SymmetricEnc.storeKey(secKey,pw.getText(),name.getText(),name.getText());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        });

        JButton loadkey1 = new JButton("Load key from keystore");
        c.gridx = 3;
        c.gridy = 0;
        this.add(loadkey1,c);
        loadkey1.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField pw = new JTextField();
            Object[] fields = {
                    "Enter key name", name,
                    "Enter password", pw,
            };
            JOptionPane.showConfirmDialog(null,fields,"Load key",JOptionPane.OK_CANCEL_OPTION);

            System.out.println(name.getText());
            System.out.println(pw.getText());

            try {
                secKey = SymmetricEnc.retrieveFromKeyStore(pw.getText(),name.getText());
                secretKey.setText(SymmetricEnc.keyToString(secKey));

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

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

        JButton loadMsgtoEnc = new JButton("Load message to encrypt");
        c.gridx = 2;
        c.gridy = 3;
        this.add(loadMsgtoEnc,c);
        loadMsgtoEnc.addActionListener(e -> {
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                plainText.setText(Utils.extractMessage(f.getPath()));
            }
        });

        JButton encryptButton = new JButton("Encrypt");
        c.gridx = 1;
        c.gridy = 4;
        this.add(encryptButton,c);


        JLabel encodedTextLabel = new JLabel("Encoded text:");
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 5;
        this.add(encodedTextLabel, c);


        JTextField encoded = new JTextField ("WAKBFKDA;bngsbhieurbgailvberg");
        encoded.setEditable(false);
        encoded.setBorder(null);
        encoded.setForeground(UIManager.getColor("Label.foreground"));
        encoded.setFont(UIManager.getFont("Label.font"));
        c.gridx = 1;
        c.gridy = 5;
        this.add(encoded, c);

        encryptButton.addActionListener(e -> {

            try {
                encrypted = SymmetricEnc.encryptText(plainText.getText(),secKey);
                encoded.setText(encrypted);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton saveEncrypted = new JButton("Save message");
        c.gridx = 2;
        c.gridy = 5;
        this.add(saveEncrypted, c);
        saveEncrypted.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    Utils.saveEncryptedText(fileToSave.getAbsolutePath(),encoded.getText());
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JSeparator separator1 = new JSeparator();
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(separator1, c);

        JLabel decryptLB = new JLabel("Decrypt");
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 7;
        this.add(decryptLB, c);

        JLabel encryptedText = new JLabel("Enter text to decrypt");
        c.gridx = 0;
        c.gridy = 8;
        this.add(encryptedText, c);



        JTextField textToDecrypt = new JTextField();
        c.gridx = 1;
        c.gridy = 8;
        this.add(textToDecrypt, c);

        JButton loadText = new JButton("Load text from system");
        c.gridx = 2;
        c.gridy = 8;
        this.add(loadText,c);
        loadText.addActionListener(e -> {
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                textToDecrypt.setText(Utils.extractMessage(f.getPath()));
                encoded.setText(Utils.extractMessage(f.getPath()));

            }
        });

        JLabel secretKeyLB = new JLabel("Enter secret key");
        c.gridx = 0;
        c.gridy = 9;
        this.add(secretKeyLB, c);

        JTextField enterKey = new JTextField();
        c.gridx = 1;
        c.gridy = 9;
        this.add(enterKey, c);

        JButton loadKey = new JButton("Load secret key from store");
        c.gridx = 2;
        c.gridy = 9;
        this.add(loadKey, c);
        loadKey.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField pw = new JTextField();
            Object[] fields = {
                    "Enter key name", name,
                    "Enter password", pw,
            };
            JOptionPane.showConfirmDialog(null,fields,"Load key",JOptionPane.OK_CANCEL_OPTION);

            System.out.println(name.getText());
            System.out.println(pw.getText());

            try {
                secKey = SymmetricEnc.retrieveFromKeyStore(pw.getText(),name.getText());
                enterKey.setText(SymmetricEnc.keyToString(secKey));
                loadedFromStore = true;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton decrypt = new JButton("Decrypt");

        c.gridx = 1;
        c.gridy = 10;
        this.add(decrypt, c);

        JLabel decodedmsg = new JLabel("Decoded msg here");
        c.gridx = 1;
        c.gridy = 11;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(decodedmsg, c);

        decrypt.addActionListener(e -> {
            try {
                System.out.println(encoded.getText());
                if (loadedFromStore) {
                    System.out.println("test");
                    decrypted = SymmetricEnc.decryptText(encoded.getText(), secKey);
                }
                System.out.println("test2");
                // rebuild key using SecretKeySpec
                System.out.println(SymmetricEnc.stringToAESKey(enterKey.getText()));
                SecretKey originalKey = SymmetricEnc.stringToAESKey(enterKey.getText());
                decrypted = SymmetricEnc.decryptText(encoded.getText(),originalKey);
                loadedFromStore = false;

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            decodedmsg.setText(decrypted);
        });

    }
}
