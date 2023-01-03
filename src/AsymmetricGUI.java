import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.EventListener;

public class AsymmetricGUI extends JPanel implements EventListener {

    KeyPair keyPair;
    PublicKey publicKey;
    PrivateKey privateKey;
    String encrypted;
    String decrypted;

    JButton generateKeyPairButton;
    JButton loadPrivateKeyButton;
    JTextArea privateKeyField;
    JLabel jcomp4;
    JLabel jcomp5;
    JTextArea publicKeyField;
    JButton loadPublicKeyButton;
    JButton saveKeysButton;
    JButton loadMessageToEncryptButton;
    JTextArea loadedMessageToEncryptField;
    JTextArea encryptedField;
    JButton encryptButton;
    JButton saveEncryptedMessage;
    JButton loadPrivateKey2;
    JButton loadEncryptedTextButton;
    JTextArea loadedPrivateKeyField;
    JTextArea decryptedTextField;
    JButton decryptButton;
    JTextArea loadedEncryptedTextField;

    AsymmetricGUI() {


        //construct components
        generateKeyPairButton = new JButton ("Generate key pair");
        loadPrivateKeyButton = new JButton ("Load private key");
        privateKeyField = new JTextArea (5, 5);
        jcomp4 = new JLabel ("Public key");
        jcomp5 = new JLabel ("Private key");
        publicKeyField = new JTextArea (5, 5);
        loadPublicKeyButton = new JButton ("Load public key");
        saveKeysButton = new JButton ("Save keys");
        loadMessageToEncryptButton = new JButton ("Load message");
        loadedMessageToEncryptField = new JTextArea (5, 5);
        encryptedField = new JTextArea (5, 5);
        encryptButton = new JButton ("Encrypt");
        saveEncryptedMessage = new JButton ("Save message");
        loadPrivateKey2 = new JButton ("Load private key");
        loadEncryptedTextButton = new JButton ("Load encrypted text");
        loadedPrivateKeyField = new JTextArea (5, 5);
        decryptedTextField = new JTextArea (5, 5);
        decryptButton = new JButton ("Decrypt");
        loadedEncryptedTextField = new JTextArea (5, 5);

        //set components properties
        privateKeyField.setToolTipText ("Public key");

        //adjust size and set layout
        setPreferredSize (new Dimension(944, 613));
        setLayout (null);
        
        generateKeyPairButton.addActionListener(e -> {
            try {
                keyPair = AsymmetricEnc.buildKeyPair();
                publicKey = keyPair.getPublic();
                privateKey = keyPair.getPrivate();

                privateKeyField.setText(Utils.keyToString(privateKey));
                publicKeyField.setText(Utils.keyToString(publicKey));

            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        });

        saveKeysButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    Utils.savePEM(keyPair,fileToSave.getAbsolutePath());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Something went wrong saving the file.");
                    throw new RuntimeException(ex);
                }
            }
        });

        encryptButton.addActionListener(e -> {
            if (encrypted == null) {
                JOptionPane.showMessageDialog(null, "Load or generate a public key.", "Bad key", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                encrypted = AsymmetricEnc.encrypt(publicKey, loadedMessageToEncryptField.getText());
                encryptedField.setText(encrypted);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Make .");
                throw new RuntimeException(ex);
            }
        });

        decryptButton.addActionListener(e -> {
            if (loadedPrivateKeyField.getText() == null) {
                JOptionPane.showMessageDialog(null, "Load or enter a private key.", "Bad key", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (loadedEncryptedTextField.getText() == null) {
                JOptionPane.showMessageDialog(null, "Please enter a message to decrypt.", "Bad key", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                PrivateKey privateKey = Utils.getPrivateKeyFromString(loadedPrivateKeyField.getText());
                decrypted = AsymmetricEnc.decrypt(privateKey,loadedEncryptedTextField.getText());
                decryptedTextField.setText(decrypted);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        loadMessageToEncryptButton.addActionListener(e -> {
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                if (f.getName().toLowerCase().endsWith(".txt"))
                    loadedMessageToEncryptField.setText(Utils.extractMessage(f.getPath()));
                else {
                    JOptionPane.showMessageDialog(null, "Please select a text file.", "Wrong file type", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        saveEncryptedMessage.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    Utils.saveEncryptedText(fileToSave.getAbsolutePath(), loadedMessageToEncryptField.getText());
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Something went wrong.", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
        });

        loadPrivateKey2.addActionListener(e -> {
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".pem")){
                    JOptionPane.showMessageDialog(null, "Please select a pem file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    String toKey = Utils.getPrivateKeyPem(f.getAbsolutePath());
                    privateKey = Utils.getPrivateKeyFromString(toKey);
                } catch (IOException | NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }catch (InvalidKeySpecException ex){
                    JOptionPane.showMessageDialog(null, "Wrong key. Please enter correct private key.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            loadedPrivateKeyField.setText(Utils.keyToString(privateKey));
        });

        loadEncryptedTextButton.addActionListener(e -> {
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                String decryptedmsg = Utils.extractMessage(f.getPath());
                loadedEncryptedTextField.setText(decryptedmsg);
            }
        });

        loadPrivateKeyButton.addActionListener(e -> {
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".pem")){
                    JOptionPane.showMessageDialog(null, "Please select a pem file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                String key;
                try {
                    key = Utils.getPrivateKeyPem(f.getPath());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                privateKeyField.setText(key);
            }
        });

        loadPublicKeyButton.addActionListener(e -> {
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".pem")){
                    JOptionPane.showMessageDialog(null, "Please select a pem file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                String key;
                try {
                    key = Utils.getPublicKeyPem(f.getPath());
                    publicKeyField.setText(key);
                    publicKey = Utils.getPublicKeyFromString(key);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        privateKeyField.setLineWrap(true);
        privateKeyField.setBorder(new LineBorder(Color.black,1));

        publicKeyField.setLineWrap(true);
        publicKeyField.setBorder(new LineBorder(Color.black,1));

        loadedMessageToEncryptField.setLineWrap(true);
        loadedMessageToEncryptField.setBorder(new LineBorder(Color.black,1));

        loadedPrivateKeyField.setLineWrap(true);
        loadedPrivateKeyField.setBorder(new LineBorder(Color.black,1));
        
        loadedEncryptedTextField.setLineWrap(true);
        loadedEncryptedTextField.setBorder(new LineBorder(Color.black,1));

        encryptedField.setLineWrap(true);
        encryptedField.setBorder(new LineBorder(Color.black,1));

        decryptedTextField.setLineWrap(true);
        decryptedTextField.setBorder(new LineBorder(Color.black,1));

        //add components
        add(generateKeyPairButton);
        add(loadPrivateKeyButton);
        add(privateKeyField);
        add(jcomp4);
        add(jcomp5);
        add(publicKeyField);
        add(loadPublicKeyButton);
        add(saveKeysButton);
        add(loadMessageToEncryptButton);
        add(loadedMessageToEncryptField);
        add(encryptedField);
        add(encryptButton);
        add(saveEncryptedMessage);
        add(loadPrivateKey2);
        add(loadEncryptedTextButton);
        add(loadedPrivateKeyField);
        add(decryptedTextField);
        add(decryptButton);
        add(loadedEncryptedTextField);

        //set component bounds (only needed by Absolute Positioning)
        generateKeyPairButton.setBounds (15, 20, 150, 35);
        loadPrivateKeyButton.setBounds (15, 70, 150, 25);
        privateKeyField.setBounds (460, 25, 255, 195);
        jcomp4.setBounds (260, 0, 100, 25);
        jcomp5.setBounds (555, 5, 100, 25);
        publicKeyField.setBounds (210, 25, 230, 145);
        loadPublicKeyButton.setBounds (15, 110, 150, 25);
        saveKeysButton.setBounds (740, 75, 100, 25);
        loadMessageToEncryptButton.setBounds (30, 320, 125, 25);
        loadedMessageToEncryptField.setBounds (195, 265, 165, 130);
        encryptedField.setBounds (515, 265, 165, 130);
        encryptButton.setBounds (385, 320, 100, 25);
        saveEncryptedMessage.setBounds (720, 320, 125, 25);
        loadPrivateKey2.setBounds (15, 455, 145, 25);
        loadEncryptedTextButton.setBounds (15, 505, 155, 25);
        loadedPrivateKeyField.setBounds (195, 455, 165, 35);
        decryptedTextField.setBounds (515, 435, 165, 130);
        decryptButton.setBounds (385, 475, 100, 25);
        loadedEncryptedTextField.setBounds (195, 500, 165, 30);
    }
}
