package ccserver;

import java.io.*;
import data.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class CCServer {

    public static boolean shuttingDown = false;
    private static Database database = new Database();
    private static DatagramSocket serverSocket = null;
    private static final int port = 26500;
    
    public static void main(String[] args) throws IOException {
        database.loadAll();
        
        serverSocket = new DatagramSocket(port);
        System.out.println("Waiting for Clients");
        while(!shuttingDown){
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            serverSocket.receive(packet);
            System.out.println("Received Packet");
            
            Thread th = new Thread( new WorkerThread( packet, database ));
            th.setDaemon(true);
            th.start();
        }
        System.out.println("Server Shutting Down");
        serverSocket.close();
    }
    
    public static void initShutdown() throws IOException{
        shuttingDown = true;
        database.save();
        if(serverSocket != null)
            serverSocket.close();
    }
}
