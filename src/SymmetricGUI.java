import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.EventListener;

public class SymmetricGUI extends JPanel implements EventListener {
    SecretKey secKey;
    String encrypted;
    String decrypted;
    Boolean loadedFromStore = false;

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

        generateKeyButton.addActionListener(e -> {
            try {
                secKey = SymmetricEnc.getSecretEncryptionKey();
                secretKeyField.setText(Utils.keyToString(secKey));

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });


        saveKeyButton.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField pw = new JTextField();
            Object[] fields = {
                    "Enter key name", name,
                    "Enter password", pw,
            };
            JOptionPane.showConfirmDialog(null, fields, "Save key", JOptionPane.OK_CANCEL_OPTION);

            System.out.println(name.getText());
            System.out.println(pw.getText());

            try {
                SymmetricEnc.storeKey(secKey, pw.getText(), name.getText(), name.getText());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

        });


        loadKeyButton.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField pw = new JTextField();
            Object[] fields = {
                    "Enter key name", name,
                    "Enter password", pw,
            };
            JOptionPane.showConfirmDialog(null, fields, "Load key", JOptionPane.OK_CANCEL_OPTION);

            System.out.println(name.getText());
            System.out.println(pw.getText());

            try {
                secKey = SymmetricEnc.retrieveFromKeyStore(pw.getText(), name.getText());
                secretKeyField.setText(Utils.keyToString(secKey));

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
                messageToEncrypt.setText(Utils.extractMessage(f.getPath()));
            }
        });

        encryptButton.addActionListener(e -> {
            try {
                encrypted = SymmetricEnc.encryptText(messageToEncrypt.getText(), secKey);
                encryptedMessage.setText(encrypted);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        saveMessageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    Utils.saveEncryptedText(fileToSave.getAbsolutePath(), encryptedMessage.getText());
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        loadTextToDecryptButton.addActionListener(e -> {
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                String decryptedmsg = Utils.extractMessage(f.getPath());
                loadedTextToDecrypt.setText(decryptedmsg);
            }
        });

        loadKeyToDecryptButton.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField pw = new JTextField();
            Object[] fields = {
                    "Enter key name", name,
                    "Enter password", pw,
            };
            JOptionPane.showConfirmDialog(null, fields, "Load key", JOptionPane.OK_CANCEL_OPTION);

            System.out.println(name.getText());
            System.out.println(pw.getText());

            try {
                secKey = SymmetricEnc.retrieveFromKeyStore(pw.getText(), name.getText());
                loadedKey.setText(Utils.keyToString(secKey));
                loadedFromStore = true;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        decryptButton.addActionListener(e -> {
            try {
                if (loadedFromStore) {
                    decrypted = SymmetricEnc.decryptText(loadedTextToDecrypt.getText(), secKey);
                }
                // rebuild key using SecretKeySpec
                SecretKey originalKey = SymmetricEnc.stringToAESKey(loadedKey.getText());
                decrypted = SymmetricEnc.decryptText(loadedTextToDecrypt.getText(), originalKey);
                loadedFromStore = false;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            decryptedText.setText(decrypted);
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
    }
}

