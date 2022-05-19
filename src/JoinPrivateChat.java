import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class JoinPrivateChat {

    String ip;
    String port;
    int con = 1;
    int close = 0;
    String username;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    TextArea taContent;
    JTextField tfMessage;


    JoinPrivateChat(JPanel jpcPanel, JTabbedPane tp){

        GetSetData obj = new GetSetData();

        // IP Address Label
        JLabel lblIPadd = new JLabel("IP Address");
        lblIPadd.setBounds(10, 10, 80, 25);
        jpcPanel.add(lblIPadd);

        // IP Address Text Box
        JTextField tfIPadd = new JTextField("127.0.0.1");
        tfIPadd.setBounds(65, 10, 100, 25);
        jpcPanel.add(tfIPadd);

        // Port Label
        JLabel lblPort = new JLabel("Port");
        lblPort.setBounds(175, 10, 20, 25);
        jpcPanel.add(lblPort);

        // Port Text Box
        JTextField tfPort = new JTextField("7777");
        tfPort.setBounds(200, 10, 50, 25);
        jpcPanel.add(tfPort);

        // Start ServerButton
        JButton btnStart = new JButton("Join Server");
        btnStart.setBackground(Color.lightGray);
        btnStart.setForeground(Color.black);
        btnStart.setBounds(260, 9, 100, 26);
        jpcPanel.add(btnStart);

        // Close Button
        JButton btnClose = new JButton("Close");
        btnClose.setBackground(Color.lightGray);
        btnClose.setForeground(Color.black);
        btnClose.setBounds(610, 9, 60, 26);
        jpcPanel.add(btnClose);

        //Text Area for content
        taContent = new TextArea("",0,0,TextArea.SCROLLBARS_VERTICAL_ONLY);
        taContent.setEditable(false);
        taContent.setBounds(10, 45, 660, 315);
        taContent.setFont(new Font(Font.MONOSPACED,Font.PLAIN,obj.getFontSize()));
        jpcPanel.add(taContent);

        tfMessage = new JTextField();
        tfMessage.setBounds(10, 370, 580, 25);
        tfMessage.setEditable(false);
        jpcPanel.add(tfMessage);

        JButton btnSend = new JButton("Send");
        btnSend.setBackground(Color.lightGray);
        btnSend.setForeground(Color.black);
        btnSend.setBounds(600, 369, 70, 26);
        jpcPanel.add(btnSend);


        // Action Listeners --------------------------------------------------------------------------------------------


        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    out.println("Client Ended The Chat");
                }
                catch (Exception ex){
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
                if(!tfMessage.getText().equals("")){
                    taContent.append("You : " + tfMessage.getText() + "\n");
                    out.println(username + " : " + tfMessage.getText());
                    tfMessage.setText("");
                }
            }
        });



    }



    public void joinServer(){
        try {
            taContent.append("Sending Request to Server"+ "\n");
            socket = new Socket(ip,Integer.parseInt(port));
            taContent.append("Connection Established"+ "\n");

            tfMessage.setEditable(true);

            br =  new BufferedReader(new InputStreamReader( socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startReading() {
        Runnable r3 = ()->{
            taContent.append("Start Reading "+ "\n");
            try {
                while(con==1){
                    String msg = br.readLine();
                    if(msg.equals("Server Ended The Chat")){
                        taContent.append("Server Ended The Chat"+ "\n");
                        tfMessage.setEditable(false);
                        socket.close();
                        break;
                    }
                    taContent.append(msg+ "\n");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        };
        new Thread(r3).start();
    }

    public void startWriting() {
        Runnable r4 = ()->{
            taContent.append("Start Writing"+ "\n");
            try {
                while(!socket.isClosed()){

                    out.flush();

                    if(close == 1){
                        socket.close();
                        break;
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        };
        new Thread(r4).start();
    }
}
