import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CreateGroupChat {
    String port="7777";
    public TextArea taContent;
    static ServerSocket ss;
    static DataInputStream dis;
    static DataOutputStream dos;
    static Scanner in = new Scanner(System.in);
    public static user users[] = new user[10];
    public static int totalClientsOnline=0;


    void CreateGroupChatSwing(JPanel cpcPanel, JTabbedPane tp){

        GetSetData obj = new GetSetData();

        String ipaddress = null;
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 443);
            ipaddress = socket.getLocalAddress().getHostAddress();
        }
        catch (Exception e){
            e.printStackTrace();
            Frame f = new Frame();
            JOptionPane.showMessageDialog(f, "You are not Connected to any Network",
                    "Error", JOptionPane.ERROR_MESSAGE);
            tp.remove(tp.getSelectedIndex());
        }

        // IP Address Label
        JLabel lblIPadd = new JLabel("IP Address");
        lblIPadd.setBounds(10, 10, 80, 25);
        cpcPanel.add(lblIPadd);

        // IP Address Text Box
        JTextField tfIPadd = new JTextField(ipaddress);
        tfIPadd.setBounds(65, 10, 100, 25);
        tfIPadd.setEditable(false);
        cpcPanel.add(tfIPadd);

        // Port Label
        JLabel lblPort = new JLabel("Port");
        lblPort.setBounds(175, 10, 20, 25);
        cpcPanel.add(lblPort);

        // Port Text Box
        JTextField tfPort = new JTextField("7777");
        tfPort.setBounds(200, 10, 50, 25);
        cpcPanel.add(tfPort);

        // Start ServerButton
        JButton btnStart = new JButton("Start Server");
        btnStart.setBackground(Color.lightGray);
        btnStart.setForeground(Color.black);
        btnStart.setBounds(260, 9, 100, 26);
        cpcPanel.add(btnStart);

        // Close Button
        JButton btnClose = new JButton("Close");
        btnClose.setBackground(Color.lightGray);
        btnClose.setForeground(Color.black);
        btnClose.setBounds(610, 9, 60, 26);
        cpcPanel.add(btnClose);

        //Text Area for content
        taContent = new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
        taContent.setEditable(false);
        taContent.setBounds(10, 45, 660, 350);
        taContent.setFont(new Font(Font.MONOSPACED,Font.PLAIN,obj.getFontSize()));
        cpcPanel.add(taContent);


        // Action Listeners --------------------------------------------------------------------------------------------

        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    ss.close();
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }

                tp.remove(tp.getSelectedIndex());

            }
        });

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnStart.setVisible(false);
                tfPort.setEditable(false);

                port = tfPort.getText();

                startServer(tp);
            }
        });

    }

    public void startServer(JTabbedPane tp){

        Runnable t = () ->{
            try
            {
                ss = new ServerSocket(Integer.parseInt(port));
                taContent.append("Server is Running ... \n");
                for(int i=0;i<10;i++)
                {
                    users[i] = new user(i+1,ss.accept(),taContent);
                    totalClientsOnline++;
                }

            }

            catch(BindException e)
            {
                Frame f = new Frame();
                JOptionPane.showMessageDialog(f, "You can create only one server at a time!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                tp.remove(tp.getSelectedIndex());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        };
        new Thread(t).start();

    }

    public void sendMessageToAll(String msg)
    {
        for(int c=0;c<totalClientsOnline;c++)
        {
            try
            {
                users[c].sendMessage(msg);
            }

            catch(Exception e){}
        }
    }
}



class user extends Thread
{
    CreateGroupChat tirth = new CreateGroupChat();
    int userID;
    public Socket userSocket;
    public DataInputStream userDIS;
    public DataOutputStream userDOS;
    public Thread t;
    OutputStream os;


    public user(int id,Socket a,TextArea taContent)
    {
        try
        {
            userID = id;
            userSocket = a;
            userDIS = new DataInputStream(userSocket.getInputStream());
            userDOS = new DataOutputStream(userSocket.getOutputStream());
            System.out.println(userID+ " client connected.");
            taContent.append(userID+ " client connected. \n");

            t = new Thread(this);
            t.start();
        }
        catch(Exception e)
        {
            System.out.println("Exception caught in constructor.");
        }
    }


    public void run()
    {
        Scanner in = new Scanner(System.in);
        String message;
        while(true)
        {
            try
            {
                message = userDIS.readUTF();
                tirth.sendMessageToAll(message);
            }

            catch(Exception e)
            {

            }

        }
    }

    public void sendMessage(String s)
    {
        try
        {
            userDOS.writeUTF(s);
        }

        catch(Exception e){}
    }
}


