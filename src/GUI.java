import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {
    public static CardLayout cardLayout = new CardLayout();

    public static JPanel container = new JPanel();
    JPanel menu = new JPanel();
    JPanel symmetric = new SymmetricGUI();
    JPanel asymmetric = new AsymmetricGUI();
    JPanel sign = new DSGui();

    JButton goToSymmetric = new JButton();
    JButton goToAsymmetric = new JButton();
    JButton goToSign = new JButton();
    JButton bt3 = new JButton();

    GUI(){
        container.setLayout(cardLayout);
        //***************************************************//
        //Buttons
        goToSymmetric.setText("Symmetric encryption");
        goToSymmetric.addActionListener(this);
        goToSymmetric.setAlignmentX(Component.CENTER_ALIGNMENT);

        goToAsymmetric.setText("Asymmetric encryption");
        goToAsymmetric.addActionListener(this);
        goToAsymmetric.setAlignmentX(Component.CENTER_ALIGNMENT);

        goToSign.setText("Digital signature");
        goToSign.addActionListener(this);
        goToSign.setAlignmentX(Component.CENTER_ALIGNMENT);



        //***************************************************//
        //Starting menu
        menu.setLayout(new BoxLayout(menu, BoxLayout.PAGE_AXIS));
        menu.setBackground(Color.BLUE);
        menu.add(goToSymmetric);
        menu.add(goToAsymmetric);
        menu.add(goToSign);
        menu.add(Box.createRigidArea(new Dimension(0,150)));


        setSize(900,800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        container.add(menu,"menu");
        container.add(symmetric,"symmetric");
        container.add(asymmetric,"asymmetric");
        container.add(sign,"sign");
        cardLayout.show(container,"menu");
    }
    public static void main(String[] args) {
        GUI c = new GUI();
        c.add(container);
        c.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == goToSymmetric) {
            this.setTitle("Symmetric encryption");
            cardLayout.show(container, "symmetric");
        }
        else if (e.getSource() == goToAsymmetric) {
            cardLayout.show(container,"asymmetric");
        } else if (e.getSource() == goToSign) {
            cardLayout.show(container, "sign");
        }
    }



}
