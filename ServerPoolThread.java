import java.net.*;
import java.io.*;
import java.util.*;
public class ServerPoolThread implements Runnable {
    ServerSocket ss;
    ServerHost sh;
    Socket connection;
    BufferedReader din;
    public ServerPoolThread(ServerHost host, Socket s) {
        connection=s;
        sh=host;
        System.out.println("new server pool thread started for " + s.getInetAddress());
    }
    public void run() {
        try {
            din = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while(true) {
                try {
                    String in = din.readLine();
                    System.out.println("server has received packet: " + in);
                    sh.inputPacket(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}