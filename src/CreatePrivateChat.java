import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class CreatePrivateChat {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    int con = 1;
    int close = 0;
    String port="7777";

    TextArea taContent;
    JTextField tfMessage;



    CreatePrivateChat(JPanel cpcPanel, JTabbedPane tp){

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
        taContent.setBounds(10, 45, 660, 315);
        taContent.setFont(new Font(Font.MONOSPACED,Font.PLAIN,obj.getFontSize()));
        cpcPanel.add(taContent);

        tfMessage = new JTextField();
        tfMessage.setBounds(10, 370, 580, 25);
        tfMessage.setEditable(false);
        cpcPanel.add(tfMessage);

        JButton btnSend = new JButton("Send");
        btnSend.setBackground(Color.lightGray);
        btnSend.setForeground(Color.black);
        btnSend.setBounds(600, 369, 70, 26);
        cpcPanel.add(btnSend);


        // Action Listeners --------------------------------------------------------------------------------------------


        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    out.println("Server Ended The Chat");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                close = 1;

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


        String username = obj.getUsername();

        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!tfMessage.getText().equals("")){
                    taContent.append("You : " + tfMessage.getText() + "\n");
                    out.println(username + " : " + tfMessage.getText());
                    tfMessage.setText("");
                }
            }
        });

        // send message when enter is pressed after typing the message
        Action action = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Enter pressed");
                if(!tfMessage.getText().equals("")){
                    taContent.append("You : " + tfMessage.getText() + "\n");
                    out.println(username + " : " + tfMessage.getText());
                    tfMessage.setText("");
                }
            }
        };
        tfMessage.addActionListener( action );



    }



    public void startServer(JTabbedPane tp){

        Runnable t = () -> {
            try {
                server = new ServerSocket(Integer.parseInt(port));
                taContent.append("Server is Ready to Connect" + "\n");
                socket = server.accept();
                taContent.append("Connected to Client" + "\n");

                tfMessage.setEditable(true);

                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream());

                startReading(tp);
                startWriting();


                TimeUnit.SECONDS.sleep(1);
                taContent.setText("");
                GetSetData u = new GetSetData();
                out.println("Connection Established with "+u.getUsername());

            } catch (BindException ex){
                Frame f = new Frame();
                JOptionPane.showMessageDialog(f, "You can create only one server at a time!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                tp.remove(tp.getSelectedIndex());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(t).start();

        new Thread(t).interrupt();
    }

    public void startReading(JTabbedPane tp) {
        Runnable r1 = () -> {
            taContent.append("Started Reading from Client" + "\n");

            try {
                while (con == 1) {
                    String msg = br.readLine();
                    if (msg.equals("Client Ended The Chat")) {
//                        taContent.append("Client Ended The Chat" + "\n");
                        Frame f = new Frame();
                        JOptionPane.showMessageDialog(f, "Client Ended the Chat",
                                "Private Chat", JOptionPane.INFORMATION_MESSAGE);
                        tp.remove(tp.getSelectedIndex());
                        tfMessage.setEditable(false);
                        server.close();
                        socket.close();
                        break;

                    }
                    taContent.append(msg + "\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        };
        new Thread(r1).start();
    }

    public void startWriting() {
        Runnable r2 = () -> {
            taContent.append("Started Writing from Client" + "\n");

            try {
                while (!socket.isClosed()) {

                    out.flush();

                    if (close == 1) {
                        server.close();
                        socket.close();
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        };
        new Thread(r2).start();
    }

}
