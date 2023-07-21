/*
Name Yitian Gao
Student ID 1318692
 */

package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DictionaryClient {



    private JButton add;
    private JPanel ROOT;
    private JTextArea wordinput;
    private JLabel word;
    private JTextArea meaninginput;
    private JTextArea response;
    private JButton updateButton;
    private JButton removeButton;
    private JButton queryButton;
    private JTextArea textArea2;

    private static String address = "localhost";
    private static Integer port = 10010;

    public static void main(String[] args) throws IOException {


        try {
            if (args.length ==2){
                if (Integer.parseInt(args[1]) <= 1024 || Integer.parseInt((args[1]))>=49151){
                    System.out.println("Invalid port number: Port Number should be between 1024 and 49151");
                }else{
                    port = Integer.parseInt(args[1]);
                }
                address = args[0];
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid Port Number: Port Number should be between 1024 and 49151!");
            throw new RuntimeException(e);
        }

        JFrame frame = new JFrame("myGUI");
        frame.setContentPane(new DictionaryClient().ROOT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);
//frame.pack();
        frame.setVisible(true);


    }

    public DictionaryClient() {
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordinput.getText();
                String meaning = meaninginput.getText();
                String request = word + "-" + "add" + "-" + meaning;
                try {
                    Socket socket = null;
                    socket = new Socket(address, port);
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    output.writeUTF(request);

                    String res="ffff";

                        res = input.readUTF();

                    System.out.println(res);
                    response.setText(res);

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordinput.getText();

                String request = null;
                if(meaninginput.getText().equals("")){

                    request = word + "-" + "update" + "-" + "default";
                }else {
                    request = word + "-" + "update" + "-" + meaninginput.getText();
                }
                try {
                    Socket socket = null;
                    socket = new Socket(address, port);


                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    output.writeUTF(request);
                    String res = input.readUTF();
                    System.out.println(res);
                    response.setText(res);

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        });
        queryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String word = wordinput.getText();

                String request = word + "-" + "query";
                try {
                    Socket socket = null;

                    socket = new Socket(address, port);

                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    output.writeUTF(request);
                    String res = input.readUTF();
                    System.out.println(res);
                    response.setText(res);

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordinput.getText();
                String request = word + "-" + "remove";
                try {
                    Socket socket = null;
                    socket = new Socket(address, port);


                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                    output.writeUTF(request);
                    String res = input.readUTF();
                    System.out.println(res);
                    response.setText(res);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }


            }
        });
    }

}