import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Settings {

    Settings(){

        GetSetData obj = new GetSetData();

        JFrame f = new JFrame("Settings");
        f.setIconImage(Toolkit.getDefaultToolkit().getImage("Assets/Settings.png"));
        f.setSize(290,200);
        f.setLocationRelativeTo(null);
        f.setLayout(null);
        f.setResizable(false);

        // Username Label
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBounds(20,30,100,25);
        lblUsername.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,11));
        f.add(lblUsername);

        // Username Text field
        JTextField tfUsername = new JTextField();
        tfUsername.setBounds(130,30,120,25);
        f.add(tfUsername);

        // Message Font size
        JLabel lblFont = new JLabel("Chat Font Size");
        lblFont.setBounds(20,65,100,25);
        lblFont.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,11));
        f.add(lblFont);

        // Message Font Size spinner
        JSpinner sFont = new JSpinner();
        sFont.setBounds(130,65,60,25);
        sFont.setModel(new SpinnerNumberModel(15,10,20,1));

        if ( sFont.getEditor() instanceof JSpinner.DefaultEditor ) {
            JSpinner.DefaultEditor editor = ( JSpinner.DefaultEditor ) sFont.getEditor();
            editor.getTextField().setEnabled( true );
            editor.getTextField().setEditable( false );
        }

        f.add(sFont);

        // Update Button
        JButton btnUpdate = new JButton("Update");
        btnUpdate.setBounds(90,110,80,26);
        f.add(btnUpdate);

        // Retrieve previously set data
        tfUsername.setText(obj.getUsername());
        sFont.setValue(obj.getFontSize());

        f.setVisible(true);

        // Action Listener ---------------------------------------------------------------------------------------------

        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                f.dispose();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                obj.setUsername(tfUsername.getText());
                obj.setFontSize(sFont.getValue().toString());

                f.dispose();
            }
        });
    }

}
