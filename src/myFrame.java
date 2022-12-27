
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class myFrame extends JFrame implements ActionListener {

    JButton symmetric;
    JButton asymmetric;
    JButton sign;
    JPanel redPanel;
    JPanel menu;
    JPanel symmetric_panel;
    JPanel asymmetric_panel;
    JPanel sign_panel;
    myFrame() {
        //******************************************************//
        //Menu screen
        menu = new JPanel();
        menu.setBackground(Color.BLUE);
        menu.setLayout(new BoxLayout(menu, BoxLayout.PAGE_AXIS));

        symmetric = new JButton();
        symmetric.setText("Symmetric");
        symmetric.setAlignmentX(Component.CENTER_ALIGNMENT);
        symmetric.addActionListener(this);

        asymmetric = new JButton();
        asymmetric.setText("Asymmetric");
        asymmetric.setAlignmentX(Component.CENTER_ALIGNMENT);

        sign = new JButton();
        sign.setText("Sign");
        sign.setAlignmentX(Component.CENTER_ALIGNMENT);

        menu.add(Box.createRigidArea(new Dimension(0,150)));
        menu.add(symmetric);

        menu.add(asymmetric);

        menu.add(sign);

        //******************************************************//

        //******************************************************//
        //Symetric
        symmetric_panel = new JPanel();
        symmetric_panel.setVisible(false);
        symmetric_panel.setBackground(Color.BLACK);
        symmetric_panel.setLayout(new BoxLayout(symmetric_panel, BoxLayout.PAGE_AXIS));


        //******************************************************//

        this.add(symmetric_panel);
        this.add(menu);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500,500);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == symmetric){
            hideMenu();
//            symmetric_panel.setVisible(true);
            showPanel(symmetric_panel);
        }
        else if(e.getSource() == asymmetric){
            hideMenu();
            showPanel(asymmetric_panel);
        }
        else if (e.getSource() == sign) {
            hideMenu();
            showPanel(sign_panel);
        }
    }

    public void hideMenu(){
        menu.setVisible(false);
    }

    public void showPanel(JPanel panel){
        System.out.println("test");
        panel.setVisible(true);
    }

}
