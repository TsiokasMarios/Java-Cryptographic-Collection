import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.EventListener;

public class DSGui extends JPanel implements EventListener {
    byte[] sign;
    KeyPair keyPair;
    PublicKey publicKey;
    PrivateKey privateKey;
    String filepath;
    JButton generateKeyPair;
    JButton loadPrivateKey;
    JButton loadPublicKey;
    JTextArea publickeyfield;
    JTextArea privatekeyfield;
    JButton saveKeys;
    JButton loadFileToSign;
    JTextArea fileToSignName;
    JLabel fileNameLabel1;
    JButton signButton;
    JButton loadSigToVerify;
    JLabel sigNameLabel;
    JTextArea signatureNameField;
    JButton loadFilleToVer;
    JTextArea fileNameField;
    JLabel fileNameLabel2;
    JButton verifySignatureButton;
    JButton saveSignature;
    JLabel pubkeylabel;
    JLabel jcomp20;

    JButton toMenu;

    DSGui() {
        //construct components
        generateKeyPair = new JButton("Generate Key pair");
        loadPrivateKey = new JButton("Load private key");
        loadPublicKey = new JButton("Load public key");
        publickeyfield = new JTextArea(5, 5);
        privatekeyfield = new JTextArea(5, 5);
        saveKeys = new JButton("Save keys");
        loadFileToSign = new JButton("Load file to sign");
        fileToSignName = new JTextArea(5, 5);
        fileNameLabel1 = new JLabel("File name");
        signButton = new JButton("Sign");
        loadSigToVerify = new JButton("Load signature to verify");
        sigNameLabel = new JLabel("Signature name");
        signatureNameField = new JTextArea(5, 5);
        loadFilleToVer = new JButton("Load file to verify");
        fileNameField = new JTextArea(5, 5);
        fileNameLabel2 = new JLabel("File name");
        verifySignatureButton = new JButton("Verify");
        saveSignature = new JButton("Save signature");
        pubkeylabel = new JLabel("Public key");
        jcomp20 = new JLabel("Private key");
        toMenu = new JButton("Back to menu");


        //adjust size and set layout
        setPreferredSize(new Dimension(944, 574));
        setLayout(null);

        generateKeyPair.addActionListener(e -> {
            try {
                keyPair = DS.buildKeyPair(512);
                publicKey = keyPair.getPublic();
                privateKey = keyPair.getPrivate();

                privatekeyfield.setText(Utils.keyToString(privateKey));
                publickeyfield.setText(Utils.keyToString(publicKey));

            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        });

        saveKeys.addActionListener(e -> {
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

        loadFileToSign.addActionListener(e -> {
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                fileToSignName.setText(f.getName());
                filepath = f.getAbsolutePath();
            }
        });

        loadFilleToVer.addActionListener(e -> {
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                fileNameField.setText(f.getName());
            }
        });

        loadSigToVerify.addActionListener(e -> {
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".sig")) {
                    JOptionPane.showMessageDialog(null, "Please select a proper signature file. Signature file ends in .sig.", "Wrong file type", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                signatureNameField.setText(f.getName());
                try {
                    sign = DS.loadSignature(f.getAbsolutePath());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        signButton.addActionListener(e -> {
            if (publickeyfield.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Please generate or load a public key.", "Wrong file type", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (fileToSignName.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Please select a file to sign.", "Wrong file type", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                Signature signature = DS.createSignature();
                System.out.println(privateKey);
                DS.initSignatureToSign(privateKey,signature);
                sign = DS.signData(filepath,signature);
                JOptionPane.showMessageDialog(null, "File signed successfully.", "Wrong file type", JOptionPane.INFORMATION_MESSAGE);
            } catch (NoSuchAlgorithmException | InvalidKeyException | IOException | SignatureException ex) {
                throw new RuntimeException(ex);
            }
        });

        saveSignature.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save signature");

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    DS.saveSignature(fileToSave.getAbsolutePath(),sign);
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Something went wrong.", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        loadPrivateKey.addActionListener(e -> {
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
                    privatekeyfield.setText(toKey);
                } catch (IOException | NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }catch (InvalidKeySpecException ex){
                    JOptionPane.showMessageDialog(null, "Wrong key. Please enter correct private key.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loadPublicKey.addActionListener(e -> {
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
                    String toKey = Utils.getPublicKeyPem(f.getAbsolutePath());
                    publicKey = Utils.getPublicKeyFromString(toKey);
                    publickeyfield.setText(toKey);
                } catch (IOException | NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }catch (InvalidKeySpecException ex){
                    JOptionPane.showMessageDialog(null, "Wrong key. Please enter correct private key.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        verifySignatureButton.addActionListener(e -> {
            if (signatureNameField.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Please load a signature to validate.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (fileNameField.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Please load a file to validate.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (publicKey == null){
                JOptionPane.showMessageDialog(null, "Please load a public key to use for validation.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            try {
                Signature signature = DS.createSignature();
                DS.initSignatureToVer(publicKey,signature);
                DS.validateSignature(filepath,signature);
                if (signature.verify(sign)){
                    JOptionPane.showMessageDialog(null, "Signature is valid.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Invalid signature.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NoSuchAlgorithmException | IOException ex) {
                throw new RuntimeException(ex);
            } catch (SignatureException ex) {
                JOptionPane.showMessageDialog(null, "Invalid signature.", "Error", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(ex);
            }catch (InvalidKeyException ex){
                JOptionPane.showMessageDialog(null, "Wrong key.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        toMenu.addActionListener(e -> {
            GUI.cardLayout.show(GUI.container,"menu");
        });

        privatekeyfield.setLineWrap(true);
        privatekeyfield.setBorder(new LineBorder(Color.black,1));

        publickeyfield.setLineWrap(true);
        publickeyfield.setBorder(new LineBorder(Color.black,1));

        //add components
        add(generateKeyPair);
        add(loadPrivateKey);
        add(loadPublicKey);
        add(publickeyfield);
        add(privatekeyfield);
        add(saveKeys);
        add(loadFileToSign);
        add(fileToSignName);
        add(fileNameLabel1);
        add(signButton);
        add(loadSigToVerify);
        add(sigNameLabel);
        add(signatureNameField);
        add(loadFilleToVer);
        add(fileNameField);
        add(fileNameLabel2);
        add(verifySignatureButton);
        add(saveSignature);
        add(pubkeylabel);
        add(jcomp20);
        add(toMenu);



        //set component bounds (only needed by Absolute Positioning)
        generateKeyPair.setBounds(45, 35, 140, 25);
        loadPrivateKey.setBounds(45, 95, 140, 25);
        loadPublicKey.setBounds(45, 155, 140, 25);
        publickeyfield.setBounds(230, 25, 225, 175);
        privatekeyfield.setBounds(510, 25, 225, 175);
        saveKeys.setBounds(790, 100, 100, 25);
        loadFileToSign.setBounds(40, 325, 130, 25);
        fileToSignName.setBounds(270, 330, 160, 20);
        fileNameLabel1.setBounds(270, 300, 100, 25);
        signButton.setBounds(500, 330, 100, 25);
        loadSigToVerify.setBounds(40, 400, 175, 25);
        sigNameLabel.setBounds(270, 370, 100, 25);
        signatureNameField.setBounds(270, 405, 155, 20);
        loadFilleToVer.setBounds(40, 455, 175, 25);
        fileNameField.setBounds(270, 465, 155, 20);
        fileNameLabel2.setBounds(270, 435, 100, 25);
        verifySignatureButton.setBounds(505, 435, 100, 25);
        saveSignature.setBounds(625, 330, 120, 25);
        pubkeylabel.setBounds(310, 0, 100, 25);
        jcomp20.setBounds(580, 0, 100, 25);
        toMenu.setBounds(380,600,150,20);
    }
}
