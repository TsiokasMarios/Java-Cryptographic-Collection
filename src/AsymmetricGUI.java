import javax.crypto.BadPaddingException;
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

    //Declare all the components

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

    JButton toMenu;

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
        toMenu = new JButton("Back to menu");

        //set components properties
        privateKeyField.setToolTipText ("Public key");

        //adjust size and set layout
        setPreferredSize (new Dimension(944, 613));
        setLayout (null);
        
        generateKeyPairButton.addActionListener(e -> {
            //Options for the dropdown menus
            final String[] sources = {"DRBG","SHA1PRNG","WINDOWS-PRNG"};
            final Integer[] sizes = {512,1024,2048,4096,8192};

            JComboBox<String> randomSources = new JComboBox<>(sources);
            JComboBox<Integer> keySize = new JComboBox<>(sizes);
            //Object aray that stores the comboboxes and their names
            Object[] fields = {
                    "Select source of randomness", randomSources,
                    "Select key size", keySize,
            };
            //Show the dialo
            JOptionPane.showConfirmDialog(null, fields, "Configure key", JOptionPane.OK_CANCEL_OPTION);

            try {
                //Generate the key
                keyPair = AsymmetricEnc.generateKeyPair(keySize.getItemAt(keySize.getSelectedIndex()), randomSources.getItemAt(randomSources.getSelectedIndex()));
                //Get the public and private keys
                publicKey = keyPair.getPublic();
                privateKey = keyPair.getPrivate();
                //Set the private and public key fields to show the key
                privateKeyField.setText(Utils.keyToString(privateKey));
                publicKeyField.setText(Utils.keyToString(publicKey));

            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        });

        saveKeysButton.addActionListener(e -> {
            //Create a file chooser so the user can choose where to save the key
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
            if (publicKey == null) { //Makes sure the user has generated or loaded a public key
                JOptionPane.showMessageDialog(null, "Load or generate a public key.", "Bad key", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (loadedMessageToEncryptField.getText().isEmpty()){ //Make sure the user has entered or loaded a message to encrypt
                JOptionPane.showMessageDialog(null, "Load or enter a message to encrypt.", "No message to encrypt", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                //Encrypt the message that was provided with the public key that was either generated or loaded
                encrypted = AsymmetricEnc.encrypt(publicKey, loadedMessageToEncryptField.getText());
                //Show the encrypted text
                encryptedField.setText(encrypted);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        decryptButton.addActionListener(e -> {
            if (loadedPrivateKeyField.getText() == null) { //Make sure the user has generated or loaded a private key
                JOptionPane.showMessageDialog(null, "Load or enter a private key.", "Bad key", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (loadedEncryptedTextField.getText() == null) { //Make sure the user has entered or loaded a message to decrypt
                JOptionPane.showMessageDialog(null, "Please enter a message to decrypt.", "Bad key", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                //Generate the private key from the string that is in the loaded private key field
                PrivateKey privateKey = Utils.getPrivateKeyFromString(loadedPrivateKeyField.getText(),"RSA");
                //Decrypt the message
                decrypted = AsymmetricEnc.decrypt(privateKey,loadedEncryptedTextField.getText());
                //Show the decrypted message
                decryptedTextField.setText(decrypted);
            }catch (BadPaddingException ex){
                JOptionPane.showMessageDialog(null, "Wrong private key.", "Bad key", JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        loadMessageToEncryptButton.addActionListener(e -> {
            //Create a file choose so the user can load a text file from their system
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                if (f.getName().toLowerCase().endsWith(".txt")) //Makes sure the selected file is a text file
                    loadedMessageToEncryptField.setText(Utils.extractMessage(f.getPath()));
                else {
                    JOptionPane.showMessageDialog(null, "Please select a text file.", "Wrong file type", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        saveEncryptedMessage.addActionListener(e -> {
            //Similar to saving keys
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    Utils.saveEncryptedText(fileToSave.getAbsolutePath(), encryptedField.getText());
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Something went wrong.", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
        });

        loadPrivateKey2.addActionListener(e -> {
            //Similar as load private key
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".pem")){ //Makes sure the file is a pem file
                    JOptionPane.showMessageDialog(null, "Please select a pem file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    String toKey = Utils.getPrivateKeyPem(f.getAbsolutePath());
                    privateKey = Utils.getPrivateKeyFromString(toKey,"RSA");
                } catch (IOException | NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }catch (InvalidKeySpecException ex){
                    JOptionPane.showMessageDialog(null, "Wrong key. Please enter correct private key.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            loadedPrivateKeyField.setText(Utils.keyToString(privateKey));
        });

        loadEncryptedTextButton.addActionListener(e -> {
            //Similar as the load decrypted text
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                if (f.getName().toLowerCase().endsWith(".txt")) {//Makes sure the provided file is a text file
                    String decryptedmsg = Utils.extractMessage(f.getPath());
                    loadedEncryptedTextField.setText(decryptedmsg);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Please select a text file.", "Wrong file type", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        loadPrivateKeyButton.addActionListener(e -> {
            //Create file chooser so the user can select their private key pem file
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".pem")){ //Makes sure the file is a pem file
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
            //Create file chooser so the user can select their public key pem file
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".pem")){ //Makes sure the file is a pem file
                    JOptionPane.showMessageDialog(null, "Please select a pem file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                String key;
                try {
                    key = Utils.getPublicKeyPem(f.getPath());
                    publicKeyField.setText(key);
                    publicKey = Utils.getPublicKeyFromString(key,"RSA");
                } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        //Switch to menu window
        toMenu.addActionListener(e -> GUI.cardLayout.show(GUI.container,"menu"));

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

        //add components to the panel
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
        add(toMenu);

        //set component positions and sizes
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
        toMenu.setBounds(380,600,150,20);
    }
}
