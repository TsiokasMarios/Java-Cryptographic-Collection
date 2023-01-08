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

    //Declare the components
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
    JButton loadFileToVer;
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
        loadFileToVer = new JButton("Load file to verify");
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

            //Sources of randomness options
            final String[] sources = {"DRBG","SHA1PRNG","WINDOWS-PRNG"};
            //Key size options
            final Integer[] sizes = {512,1024,2048,3072};

            //Comboboxes so the user can select source of randomness and key size
            JComboBox<String> randomSources = new JComboBox<>(sources);
            JComboBox<Integer> keySize = new JComboBox<>(sizes);
            Object[] fields = {
                    "Select source of randomness", randomSources,
                    "Select key size", keySize,
            };
            JOptionPane.showConfirmDialog(null, fields, "Load key", JOptionPane.OK_CANCEL_OPTION);
            try {
                //Generate the keypair with the keysize and source of randomnes
                keyPair = DS.generateKeyPair(keySize.getItemAt(keySize.getSelectedIndex()), randomSources.getItemAt(randomSources.getSelectedIndex()));
                //Get public and private key
                publicKey = keyPair.getPublic();
                privateKey = keyPair.getPrivate();

                //Show the keys in their respective fields
                privatekeyfield.setText(Utils.keyToString(privateKey));
                publickeyfield.setText(Utils.keyToString(publicKey));

            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        });

        saveKeys.addActionListener(e -> {
            //Create filechooser so teh user can select where to save the keys
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
            //Creates a filechooser so the user can select the file to sign
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

        loadFileToVer.addActionListener(e -> {
            //Creates a file chooser that lets the user choose a file to verify with a signature
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                fileNameField.setText(f.getName());
                filepath=f.getAbsolutePath();
            }
        });

        loadSigToVerify.addActionListener(e -> {
            //Creates a file chooser that lets the user choose a signature to verify
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".sig")) { //Make sure the file selected is a signature file
                    JOptionPane.showMessageDialog(null, "Please select a proper signature file. Signature file ends in .sig.", "Wrong file type", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                signatureNameField.setText(f.getName()); //Show signature file name
                try {
                    sign = DS.loadSignature(f.getAbsolutePath());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        signButton.addActionListener(e -> {
            if (publickeyfield.getText().isEmpty()){ //Make sure the public key field is not empty
                JOptionPane.showMessageDialog(null, "Please generate or load a public key.", "Wrong file type", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (fileToSignName.getText().isEmpty()){ //Make sure the user has chosen a file to sign
                JOptionPane.showMessageDialog(null, "Please select a file to sign.", "Wrong file type", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                //Create the signature
                Signature signature = Signature.getInstance("DSA");
                //Initialize the signature
                DS.initSignatureToSign(privateKey,signature);
                //Sign the data
                sign = DS.signData(filepath,signature);
                JOptionPane.showMessageDialog(null, "File signed successfully.", "Wrong file type", JOptionPane.INFORMATION_MESSAGE);
            } catch (NoSuchAlgorithmException | InvalidKeyException | IOException | SignatureException ex) {
                throw new RuntimeException(ex);
            }
        });

        saveSignature.addActionListener(e -> {
            //Create a filechooser so the user can choose where to save the signature
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save signature");

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                try {
                    DS.saveSignature(fileToSave.getAbsolutePath(),sign); //Save the signed data
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null, "Something went wrong.", "Error", JOptionPane.ERROR_MESSAGE);
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        loadPrivateKey.addActionListener(e -> {
            //Create file chooser so the user can load a private key file from their system
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".pem")){ //If the file is not a .pem file ask the user to select a valid pem file
                    JOptionPane.showMessageDialog(null, "Please select a pem file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    //Get private key
                    String toKey = Utils.getPrivateKeyPem(f.getAbsolutePath());
                    //Convert the string to a key
                    privateKey = Utils.getPrivateKeyFromString(toKey,"DSA");
                    //display the key
                    privatekeyfield.setText(toKey);
                } catch (IOException | NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }catch (InvalidKeySpecException ex){
                    JOptionPane.showMessageDialog(null, "Wrong key. Please enter correct private key.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loadPublicKey.addActionListener(e -> {
            //Create file chooser so the user can load a public key file from their system
            JFileChooser file = new JFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            file.setFileHidingEnabled(false);
            if (file.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                java.io.File f = file.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".pem")){ //If the file is not a .pem file ask the user to select a valid pem file
                    JOptionPane.showMessageDialog(null, "Please select a pem file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    //Get public key
                    String toKey = Utils.getPublicKeyPem(f.getAbsolutePath());
                    //Convert the string to a key
                    publicKey = Utils.getPublicKeyFromString(toKey,"DSA");
                    //Display the key
                    publickeyfield.setText(toKey);
                } catch (IOException | NoSuchAlgorithmException ex) {
                    throw new RuntimeException(ex);
                }catch (InvalidKeySpecException ex){
                    JOptionPane.showMessageDialog(null, "Wrong key. Please enter correct private key.", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        verifySignatureButton.addActionListener(e -> {
            if (signatureNameField.getText().isEmpty()){ //Make sure the user has loaded a signature
                JOptionPane.showMessageDialog(null, "Please load a signature to validate.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (fileNameField.getText().isEmpty()){ //Make sure the user has loaded a file
                JOptionPane.showMessageDialog(null, "Please load a file to validate.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (publicKey == null){ //Make sure the user has loaded or generate a public key
                JOptionPane.showMessageDialog(null, "Please load a public key to use for validation.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            try {
                //Create the signature
                Signature signature = Signature.getInstance("DSA");
                //Initialize the signature in verification mode
                DS.initSignatureToVer(publicKey,signature);
                //Validate the signature
                DS.validateSignature(filepath,signature);
                if (signature.verify(sign)){ //If the signature is valid display a success message
                    JOptionPane.showMessageDialog(null, "Signature is valid.", "Error", JOptionPane.INFORMATION_MESSAGE);
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
            setSize(new Dimension(150,150));
            GUI.cardLayout.show(GUI.container,"menu");
        });

        privatekeyfield.setLineWrap(true);
        privatekeyfield.setBorder(new LineBorder(Color.black,1));

        publickeyfield.setLineWrap(true);
        publickeyfield.setBorder(new LineBorder(Color.black,1));

        //add components to the panel
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
        add(loadFileToVer);
        add(fileNameField);
        add(fileNameLabel2);
        add(verifySignatureButton);
        add(saveSignature);
        add(pubkeylabel);
        add(jcomp20);
        add(toMenu);



        //set component positions and sizes
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
        loadFileToVer.setBounds(40, 455, 175, 25);
        fileNameField.setBounds(270, 465, 155, 20);
        fileNameLabel2.setBounds(270, 435, 100, 25);
        verifySignatureButton.setBounds(505, 435, 100, 25);
        saveSignature.setBounds(625, 330, 120, 25);
        pubkeylabel.setBounds(310, 0, 100, 25);
        jcomp20.setBounds(580, 0, 100, 25);
        toMenu.setBounds(380,600,150,20);
    }
}
