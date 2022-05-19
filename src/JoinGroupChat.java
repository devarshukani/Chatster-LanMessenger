import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class JoinGroupChat {

    String ip;
    String port;
    int close = 0;
    static TextArea taContent;
    JTextField tfMessage;
    JTextField tfIPadd;

    static DataOutputStream dos;
    static DataInputStream dis;
    static Socket s;
    static boss server;

    static String msgHistory = new String("") ;

    void JoinGroupChatSwing(JPanel jgcPanel, JTabbedPane tp){

        GetSetData obj = new GetSetData();

        // IP Address Label
        JLabel lblIPadd = new JLabel("IP Address");
        lblIPadd.setBounds(10, 10, 80, 25);
        jgcPanel.add(lblIPadd);

        // IP Address Text Box
        tfIPadd = new JTextField("127.0.0.1");
        tfIPadd.setBounds(65, 10, 100, 25);
        jgcPanel.add(tfIPadd);

        // Port Label
        JLabel lblPort = new JLabel("Port");
        lblPort.setBounds(175, 10, 20, 25);
        jgcPanel.add(lblPort);

        // Port Text Box
        JTextField tfPort = new JTextField("7777");
        tfPort.setBounds(200, 10, 50, 25);
        jgcPanel.add(tfPort);

        // Start ServerButton
        JButton btnStart = new JButton("Join Server");
        btnStart.setBackground(Color.lightGray);
        btnStart.setForeground(Color.black);
        btnStart.setBounds(260, 9, 100, 26);
        jgcPanel.add(btnStart);

        // Close Button
        JButton btnClose = new JButton("Close");
        btnClose.setBackground(Color.lightGray);
        btnClose.setForeground(Color.black);
        btnClose.setBounds(610, 9, 60, 26);
        jgcPanel.add(btnClose);

        //Text Area for content
        taContent = new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
        taContent.setEditable(false);
        taContent.setBounds(10, 45, 660, 315);
        taContent.setFont(new Font(Font.MONOSPACED,Font.PLAIN,obj.getFontSize()));
        jgcPanel.add(taContent);

        tfMessage = new JTextField();
        tfMessage.setBounds(10, 370, 580, 25);
        tfMessage.setEditable(false);
        jgcPanel.add(tfMessage);

        JButton btnSend = new JButton("Send");
        btnSend.setBackground(Color.lightGray);
        btnSend.setForeground(Color.black);
        btnSend.setBounds(600, 369, 70, 26);
        jgcPanel.add(btnSend);


        // Action Listeners --------------------------------------------------------------------------------------------


        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

//                try {
//                    out.println("Client Ended The Chat");
//                }
//                catch (Exception ex){
//                    ex.printStackTrace();
//                }

                close = 1;

                try{
                    dos.close();
                    dis.close();
                    s.close();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }

                msgHistory = "";
                tp.remove(tp.getSelectedIndex());
            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnStart.setVisible(false);
                tfPort.setEditable(false);
                tfIPadd.setEditable(false);

                ip = tfIPadd.getText();
                port = tfPort.getText();

                joinServer();
            }
        });


        String username = obj.getUsername();

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = tfMessage.getText();
                tfMessage.setText(null);
                try
                {
                    dos.writeUTF(username + " : " + msg);
                }
                catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        });
    }



    public void joinServer(){
        tfMessage .setEditable(true);

        try
        {
            s = new Socket(ip, Integer.parseInt(port));
            dos = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());

            server = new boss(dis);
            Thread t = new Thread(server);
            t.start();

        }

        catch(IOException e)
        {
            System.out.println("Server unavailable to connect. Press Ctrl+C to exit..");
        }

    }

    public static void updateMessageArea(String msg)
    {
        msgHistory = msgHistory + "\n";
        msgHistory = msgHistory + msg;
        taContent.setText(msgHistory);
//        System.out.println("msgHistory--------------------------------------");
//        System.out.println(msgHistory);
//        System.out.println("msg-------------------------------------");
//        System.out.println(msg);
    }


    public static void reconnect()
    {
        try
        {
            s.close();
            s = new Socket("192.168.0.101", 7777);
            dos = new DataOutputStream(s.getOutputStream());
            dis = new DataInputStream(s.getInputStream());
            server = new boss(dis);
            Thread newConnection = new Thread(server);
            newConnection.start();

        }
        catch(Exception e)
        {
            System.out.println("Exception caught in reconnect().");
        }
    }

}


class boss extends Thread
{
    TextArea taContent;
    DataInputStream disServer;

    public boss(DataInputStream z)
    {
        disServer = z;
    }

    public void run()
    {

        while(true)
        {
            try
            {
                String str = disServer.readUTF();
                JoinGroupChat.updateMessageArea(str);
            }
            catch(IOException e)
            {
                System.out.println("Exception in run method. Reconnecting..");

                JoinGroupChat.reconnect();
                break;
            }

        }
    }
}
