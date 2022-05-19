import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.lang.*;

public class Main {

    Main(){
        JFrame f = new JFrame("Chatster | Lan Messaging Application");
        f.setIconImage(Toolkit.getDefaultToolkit().getImage("Assets/chatster.png"));
        f.setSize(700,500);
        f.setResizable(false);
        f.setLayout(new GridLayout());
        f.setLocationRelativeTo(null);

        JMenuBar mb = new JMenuBar();

        // Menu items in Menu bar
        JMenu mMessenger = new JMenu("Messenger");
        JMenu mOptions = new JMenu("Options");
        JMenu mHelp = new JMenu("Help");

        // Sub menu item of Messenger
        JMenuItem miGroupChat = new JMenuItem("Group Chat",new ImageIcon("Assets/GroupChat.png"));
        JMenuItem miPrivateChat = new JMenuItem("Private Chat",new ImageIcon("Assets/PrivateChat.png"));
        JMenuItem miExit = new JMenuItem("Exit",new ImageIcon("Assets/Exit.png"));
        mMessenger.add(miGroupChat);
        mMessenger.add(miPrivateChat);
        mMessenger.addSeparator();
        mMessenger.add(miExit);

        // Sub menu items of Tools
        JMenuItem miSettings = new JMenuItem("Settings",new ImageIcon("Assets/Settings.png"));
        mOptions.add(miSettings);

        // Sub menu items of Help
        JMenuItem miHelp = new JMenuItem("Help",new ImageIcon("Assets/Help.png"));
        JMenuItem miAbout = new JMenuItem("About",new ImageIcon("Assets/About.png"));
        mHelp.add(miHelp);
        mHelp.addSeparator();
        mHelp.add(miAbout);

        // Set Size of Menu items and Sub Menus items
        mMessenger.setPreferredSize(new Dimension(70, 27));
        miGroupChat.setPreferredSize(new Dimension(150, 27));
        miPrivateChat.setPreferredSize(new Dimension(150, 27));
        miExit.setPreferredSize(new Dimension(150, 27));
        miSettings.setPreferredSize(new Dimension(150, 27));
        miHelp.setPreferredSize(new Dimension(150, 27));
        miAbout.setPreferredSize(new Dimension(150, 27));

        mb.add(mMessenger);
        mb.add(mOptions);
        mb.add(mHelp);

        f.setJMenuBar(mb);

        // Tab View
        JTabbedPane tp = new JTabbedPane();
        tp.setTabPlacement(JTabbedPane.TOP);




        f.add(tp, BorderLayout.CENTER);
        f.setVisible(true);

        // Action Listeners --------------------------------------------------------------------------------------------

        // Window Frame close program end
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });

        // Messenger Menu ----------------------------------------------------------------------------------------------

        //Group Chat Click event
        miGroupChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int messageType = JOptionPane.QUESTION_MESSAGE;
                String[] options = {"Create Group Chat", "Join Group Chat"};
                int code = JOptionPane.showOptionDialog(f,
                        "Do you want to create or join chat ?",
                        "Group Chat", 0, messageType,
                        null, options, null);


                // Create group chat
                if(code == 0 ){
                    JPanel cgcPanel=new JPanel();
                    cgcPanel.setLayout(null);
                    cgcPanel.setBackground(Color.LIGHT_GRAY);

                    CreateGroupChat obj = new CreateGroupChat();
                    obj.CreateGroupChatSwing(cgcPanel, tp);

                    tp.add("Create Group Chat",cgcPanel);

                }
                // Join group chat
                else if(code == 1){
                    JPanel jgcPanel=new JPanel();
                    jgcPanel.setLayout(null);
                    jgcPanel.setBackground(Color.LIGHT_GRAY);

                    JoinGroupChat obj = new JoinGroupChat();
                    obj.JoinGroupChatSwing(jgcPanel, tp);

                    tp.add("Join Group Chat",jgcPanel);
                }

            }
        });

        //Private Chat Click event
        miPrivateChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int messageType = JOptionPane.QUESTION_MESSAGE;
                String[] options = {"Create Private Chat", "Join Private Chat"};
                int code = JOptionPane.showOptionDialog(f,
                        "Do you want to create or join chat ?",
                        "Private Chat", 0, messageType,
                        null, options, null);


                // Create private chat
                if(code == 0 ){
                    JPanel cpcPanel=new JPanel();
                    cpcPanel.setLayout(null);
                    cpcPanel.setBackground(Color.LIGHT_GRAY);

                    new CreatePrivateChat(cpcPanel, tp);

                    tp.add("Create Private Chat",cpcPanel);

                }
                // Join private chat
                else if(code == 1){
                    JPanel jpcPanel=new JPanel();
                    jpcPanel.setLayout(null);
                    jpcPanel.setBackground(Color.LIGHT_GRAY);

                    new JoinPrivateChat(jpcPanel,tp);

                    tp.add("Join Private Chat",jpcPanel);
                }


            }
        });

        // Exit Click event
        miExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Options Menu ------------------------------------------------------------------------------------------------

        // Settings click event
        miSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Settings clicked");
                new Settings();
            }
        });

        // Help Menu ---------------------------------------------------------------------------------------------------

        // Help click event
        miHelp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        // About click event
        miAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {

            e.printStackTrace();
        }
        new Main();
    }
}
