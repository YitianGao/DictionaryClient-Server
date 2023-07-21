/*
Name Yitian Gao
Student ID 1318692
 */

package server;

import server.MyRunnable;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;


public class DictionaryServer {



    private static int port = 10010;
    private static String path = "src/Dic.txt";
    public static void main(String[] args) throws IOException {

        if (args.length ==2){
            port = Integer.parseInt(args[0]);
            path = args[1];
        }

            ServerSocket ss = new ServerSocket(port);

            while (true) {
                System.out.println("wait for clients");
                Socket socket = ss.accept();

                // open a thread one user = one thread
                new Thread(new MyRunnable(socket,path)).start();




            }

        }








}

