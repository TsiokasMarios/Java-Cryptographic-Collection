import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.EventListener;

public class SymmetricGUI extends JPanel implements EventListener {
    SecretKey secKey;
    String encrypted;
    String decrypted;
    Boolean loadedFromStore = false;

    //Declare the components
    JButton generateKeyButton;
    JButton loadKeyButton;
    JButton saveKeyButton;
    JTextArea messageToEncrypt;
    JTextArea secretKeyField;
    JButton encryptButton;
    JButton loadMessageToEncryptButton;
    JButton saveMessageButton;
    JLabel jcomp9;
    JTextArea jcomp10;
    JTextArea encryptedMessage;
    JButton decryptButton;
    JButton loadKeyToDecryptButton;
    JButton loadTextToDecryptButton;
    JTextField loadedKey;
    JTextField decryptedText;
    JTextField loadedTextToDecrypt;
    JButton toMenu;

    SymmetricGUI() {

        //construct components
        generateKeyButton = new JButton("Generate key");
        loadKeyButton = new JButton("Load key");
        saveKeyButton = new JButton("Save key");
        messageToEncrypt = new JTextArea();
        secretKeyField = new JTextArea();
        encryptButton = new JButton("Encrypt");
        loadMessageToEncryptButton = new JButton("Load message");
        saveMessageButton = new JButton("Save message");
        jcomp9 = new JLabel("Enter text or load from text file");
        jcomp10 = new JTextArea(5, 5);
        encryptedMessage = new JTextArea();
        decryptButton = new JButton("Decrypt");
        loadKeyToDecryptButton = new JButton("Load key");
        loadTextToDecryptButton = new JButton("Load text");
        loadedKey = new JTextField(5);
        decryptedText = new JTextField(5);
        loadedTextToDecrypt = new JTextField(5);
        toMenu = new JButton("Back to menu");

        generateKeyButton.addActionListener(e -> {
            //Sources of randomness options
            final String[] sources = {"DRBG","SHA1PRNG","WINDOWS-PRNG"};
            //Key size options
            final Integer[] sizes = {128,192,256};

            //C
            JComboBox<String> randomSources = new JComboBox<>(sources);
            JComboBox<Integer> keySize = new JComboBox<>(sizes);
            Object[] fields = {
                    "Select source of randomness", randomSources,
                    "Select key size", keySize,
            };
            JOptionPane.showConfirmDialog(null, fields, "Load key", JOptionPane.OK_CANCEL_OPTION);
            try {
                secKey = SymmetricEnc.generateSecretKey(keySize.getItemAt(keySize.getSelectedIndex()), randomSources.getItemAt(randomSources.getSelectedIndex()));
                secretKeyField.setText(Utils.keyToString(secKey));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Something went wrong generating a key.", "Error", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(ex);
            }
        });

        saveKeyButton.addActionListener(e -> {
            //Display a window so the user can enter a name and password to store the key
            JTextField name = new JTextField();
            JTextField pw = new JTextField();
            Object[] fields = {
                    "Enter key name", name,
                    "Enter password", pw,
            };
            JOptionPane.showConfirmDialog(null, fields, "Save key", JOptionPane.OK_CANCEL_OPTION);
            try {
                SymmetricEnc.storeKey(secKey, pw.getText(), name.getText(), name.getText());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Something went wrong. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(ex);
            }
        });


        loadKeyButton.addActionListener(e -> {
            //Display a window so the user can enter a name and password to load a key
            JTextField name = new JTextField();
            JTextField pw = new JTextField();
            Object[] fields = {
                    "Enter key name", name,
                    "Enter password", pw,
            };
            JOptionPane.showConfirmDialog(null, fields, "Load key", JOptionPane.OK_CANCEL_OPTION);

            try {
                secKey = SymmetricEnc.retrieveFromKeyStore(pw.getText(), name.getText());
                if (secKey == null){
                    JOptionPane.showMessageDialog(null, "Wrong credentials were given, please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                secretKeyField.setText(Utils.keyToString(secKey));

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        loadMessageToEncryptButton.addActionListener(e -> {
            //Create a filechooser so the user can load a text file to encrypt
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".txt")){ //If selected file is not a text file prompt the user to select a valid text file
                    JOptionPane.showMessageDialog(null, "Please select a text file.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //Display the message to encrypt
                messageToEncrypt.setText(Utils.extractMessage(f.getPath()));
            }
        });

        encryptButton.addActionListener(e -> {
            if(secKey == null){ //Make sure the user loaded or created a secret key
                JOptionPane.showMessageDialog(null, "Please load or generate a secret key.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                //Encrypted the message with the text from the messageToEncrypt field and with the generated or loaded key
                encrypted = SymmetricEnc.encryptText(messageToEncrypt.getText(), secKey);
                encryptedMessage.setText(encrypted);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        saveMessageButton.addActionListener(e -> {
            //Create a filechooser so the user can choose where to save the file and with what name
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    Utils.saveEncryptedText(fileToSave.getAbsolutePath(), encryptedMessage.getText());
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Something went wrong saving the file. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                }
            }
        });

        loadTextToDecryptButton.addActionListener(e -> {
            //Create a filechooser so the user can load a text file to decrypt
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".txt")){ //If selected file is not a text file prompt the user to select a valid text file
                    JOptionPane.showMessageDialog(null, "Please select a text file.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String decryptedmsg = Utils.extractMessage(f.getPath());
                loadedTextToDecrypt.setText(decryptedmsg);
            }
        });

        loadKeyToDecryptButton.addActionListener(e -> {
            //Display a window so the user can enter a name and password to load a key
            JTextField name = new JTextField();
            JTextField pw = new JTextField();
            Object[] fields = {
                    "Enter key name", name,
                    "Enter password", pw,
            };
            JOptionPane.showConfirmDialog(null, fields, "Load key", JOptionPane.OK_CANCEL_OPTION);

            try {
                secKey = SymmetricEnc.retrieveFromKeyStore(pw.getText(), name.getText());
                if (secKey == null){ //Secret key is null if user provided wrong username or password
                    JOptionPane.showMessageDialog(null, "Wrong key name or password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                //Display the secret key
                loadedKey.setText(Utils.keyToString(secKey));
                //Set the loadedFromStore to true, will be used in decryption
                loadedFromStore = true;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        decryptButton.addActionListener(e -> {
            if (loadedTextToDecrypt.getText().isEmpty()){ //Prompts the user to enter or load a text file to decrypt if they haven't chosen one
                JOptionPane.showMessageDialog(null, "Please load or enter the text to be decrypted.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                if (loadedFromStore) { //If the user loaded a key from a keystore
                    if (secKey == null) {
                        JOptionPane.showMessageDialog(null, "Please load or enter a secret key.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    //Decrypts the text with the loaded secret key
                    decrypted = SymmetricEnc.decryptText(loadedTextToDecrypt.getText(), secKey);
                }
                if (loadedKey.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter or load a secret key.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Convert the string to key
                SecretKey originalKey = SymmetricEnc.stringToAESKey(loadedKey.getText());
                //Decrypt the message
                decrypted = SymmetricEnc.decryptText(loadedTextToDecrypt.getText(), originalKey);
                loadedFromStore = false;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Something went wrong.", "Error", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(ex);
            }
            //Display the decrypted text
            decryptedText.setText(decrypted);
        });

        toMenu.addActionListener(e -> {
            setSize(new Dimension(150,150));
            GUI.cardLayout.show(GUI.container,"menu");
        });


        //adjust size and set layout
        setPreferredSize(new Dimension(883, 530));
        setLayout(null);
        secretKeyField.setLineWrap(true);
        secretKeyField.setBorder(new LineBorder(Color.black,1) {
        });

        messageToEncrypt.setLineWrap(true);
        messageToEncrypt.setBorder(new LineBorder(Color.black,1));

        encryptedMessage.setLineWrap(true);
        encryptedMessage.setBorder(new LineBorder(Color.black,1));

        //add components
        add(generateKeyButton);
        add(loadKeyButton);
        add(saveKeyButton);
        add(messageToEncrypt);
        add(secretKeyField);
        add(encryptButton);
        add(loadMessageToEncryptButton);
        add(saveMessageButton);
        add(jcomp9);
        add(jcomp10);
        add(encryptedMessage);
        add(decryptButton);
        add(loadKeyToDecryptButton);
        add(loadTextToDecryptButton);
        add(loadedKey);
        add(decryptedText);
        add(loadedTextToDecrypt);
        add(toMenu);

        //set component bounds (only needed by Absolute Positioning)
        generateKeyButton.setBounds(10, 25, 135, 25);
        loadKeyButton.setBounds(10, 90, 100, 25);
        saveKeyButton.setBounds(350, 55, 100, 25);
        messageToEncrypt.setBounds(170, 225, 165, 95);
        secretKeyField.setBounds(170, 20, 165, 95);
        encryptButton.setBounds(360, 265, 135, 25);
        loadMessageToEncryptButton.setBounds(5, 265, 130, 25);
        saveMessageButton.setBounds(710, 265, 135, 25);
        jcomp9.setBounds(105, 155, 175, 55);
        jcomp10.setBounds(-380, 335, 100, 75);
        encryptedMessage.setBounds(510, 225, 165, 95);
        decryptButton.setBounds(370, 430, 100, 25);
        loadKeyToDecryptButton.setBounds(10, 395, 100, 25);
        loadTextToDecryptButton.setBounds(10, 450, 100, 25);
        loadedKey.setBounds(130, 395, 220, 40);
        decryptedText.setBounds(505, 395, 165, 95);
        loadedTextToDecrypt.setBounds(130, 450, 220, 40);
        toMenu.setBounds(380,600,150,20);
    }
}

