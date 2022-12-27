import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.util.EventListener;

public class SymmetricGUI extends JPanel implements EventListener {
    JButton button;

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


        JLabel secretKey = new JLabel("Secret key here");
        c.gridx = 1;
        c.gridy = 0;
        c.insets = new Insets(0,20,0,20);
        this.add(secretKey, c);

            genKey.addActionListener(e -> {
                try {
                    SecretKey secKey = SymmetricEnc.getSecretEncryptionKey();
//                    secretKey.setText(SymmetricEnc.);

                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });

        JButton bt3 = new JButton("save");
        c.gridx = 2;
        c.gridy = 0;
        this.add(bt3,c);

        JSeparator separator = new JSeparator();

        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(separator,c);

        JLabel encrypt = new JLabel("Encrypt");
        c.gridwidth = 3;
        c.gridx = 1;
        c.gridy = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(encrypt,c);

        JLabel enterClearText = new JLabel("Enter message to encrypt");
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 3;
        this.add(enterClearText,c);

        JTextField plainText = new JTextField();
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 3;
        this.add(plainText,c);

        JLabel encodedText = new JLabel("Encoded text:");
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 4;
        this.add(encodedText,c);

        JLabel encoded = new JLabel("WAKBFKDA;bngsbhieurbgailvberg");
        c.gridx = 1;
        c.gridy = 4;
        this.add(encoded,c);

        JButton saveEncrypted = new JButton("Save");
        c.gridx = 2;
        c.gridy = 4;
        this.add(saveEncrypted,c);

        JSeparator separator1 = new JSeparator();
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(separator1,c);

        JLabel decryptLB = new JLabel("Decrypt");
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 6;
        this.add(decryptLB,c);

        JLabel encryptedText = new JLabel("Enter text to decrypt");
        c.gridx = 0;
        c.gridy = 7;
        this.add(encryptedText,c);

        JTextField textToDecrypt = new JTextField();
        c.gridx = 1;
        c.gridy = 7;
        this.add(textToDecrypt,c);

        JLabel secretKeyLB = new JLabel("Enter secret key");
        c.gridx = 0;
        c.gridy = 8;
        this.add(secretKeyLB,c);

        JTextField enterKey = new JTextField();
        c.gridx = 1;
        c.gridy = 8;
        this.add(enterKey,c);

        JButton decrypt = new JButton("Decrypt");
        c.gridx = 2;
        c.gridy = 8;
        this.add(decrypt,c);

        JLabel decodedmsg = new JLabel("Decoded msg here");
        c.gridx = 1;
        c.gridy = 9;
        c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(decodedmsg,c);

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
