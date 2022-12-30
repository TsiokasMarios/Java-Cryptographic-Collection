import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {
    public static CardLayout cardLayout = new CardLayout();

    public static JPanel container = new JPanel();
    JPanel menu = new JPanel();
    JPanel symmetric = new SymmetricGUI();
    JPanel asymmetric = new JPanel();
    JPanel sign = new JPanel();

    JButton goToSymmetric = new JButton();
    JButton goToAsymmetric = new JButton();
    JButton goToSign = new JButton();
    JButton bt3 = new JButton();

    GUI(){
        container.setLayout(cardLayout);
        //***************************************************//
        //Buttons
        goToSymmetric.setText("sym 1");
        goToSymmetric.addActionListener(this);
        goToSymmetric.setAlignmentX(Component.CENTER_ALIGNMENT);

        goToAsymmetric.setText("ass 2");
        goToAsymmetric.addActionListener(this);
        goToAsymmetric.setAlignmentX(Component.CENTER_ALIGNMENT);

        bt3.setText("Generate key");
        bt3.addActionListener(this);
        goToAsymmetric.setAlignmentX(Component.CENTER_ALIGNMENT);

        //***************************************************//
        //Starting menu
        menu.setLayout(new BoxLayout(menu, BoxLayout.PAGE_AXIS));
        menu.setBackground(Color.BLUE);
        menu.add(goToSymmetric);
        menu.add(goToAsymmetric);
        menu.add(Box.createRigidArea(new Dimension(0,150)));

        //***************************************************//
        //Symmetric "page"
//        symmetric.setLayout(new BoxLayout(symmetric, BoxLayout.PAGE_AXIS));
//        symmetric.add(bt3);
//        symmetric.setBackground(Color.RED);
        //***************************************************//
        //Asymmetric "page"
        asymmetric.setLayout(new BoxLayout(asymmetric, BoxLayout.PAGE_AXIS));
//        asymmetric.add(bt3);
        asymmetric.setBackground(Color.GREEN);
        //***************************************************//

        setSize(900,800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        container.add(menu,"menu");
        container.add(symmetric,"symmetric");
        container.add(asymmetric,"asymmetric");
        cardLayout.show(container,"symmetric");
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
        }
    }



}
