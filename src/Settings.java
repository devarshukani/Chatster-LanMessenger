import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Settings {

    Settings(){
        JFrame f = new JFrame("Settings");
        f.setSize(290,200);
        f.setLocationRelativeTo(null);
        f.setLayout(null);
        f.setResizable(false);

        // Username
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(20,30,100,25);
        lblUsername.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,11));
        f.add(lblUsername);

        JTextField tfUsername = new JTextField(System.getProperty("user.name"));
        tfUsername.setBounds(130,30,120,25);
        f.add(tfUsername);

        // Message Font size
        JLabel lblFont = new JLabel("Chat Font Size");
        lblFont.setBounds(20,65,100,25);
        lblFont.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,11));
        f.add(lblFont);

        JSpinner sFont = new JSpinner();
        sFont.setBounds(130,65,60,25);
        f.add(sFont);

        // Update Button
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(90,110,80,26);
        f.add(btnUpdate);


        f.setVisible(true);

        // Action Listener ---------------------------------------------------------------------------------------------

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                f.dispose();
            }
        });
    }

    public static void main(String[] args) {
        new Settings();
    }

}
