package ccserver;

import java.io.*;
import java.net.Socket;
import data.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkerThread implements Runnable {

    private DatagramPacket clientPacket = null;
    private boolean loggedIn = false;
    private byte[] in = null;
    private byte[] out = null;
    private Database database = null;
    private Account currentUser = null;
    private int port = 26500;
    InetAddress address = null;

    public WorkerThread(DatagramPacket packet, Database database) {
        this.clientPacket = packet;
        this.database = database;
        address = packet.getAddress();
    }

    public void run() {
            in = clientPacket.getData();
            System.out.println(new String(in));
            String inp = new String(in);
            PDUManager pdu = new PDUManager(inp);
            System.out.println(pdu.buildPDU());
                switch (pdu.getType()) {
                    case "01":
                        System.out.println("Received HELLO");
                        break;
                    case "02":
                        System.out.println("Received REGISTER");
                        break;
                    case "03":
                        System.out.println("Received LOGIN");
                        if(login(pdu.getData(),pdu.getData()))
                            reply(new PDUManager("00","00","00","14","00","00", "data").buildPDU());
                        break;
                    case "04":
                        System.out.println("Received LOGOUT");
                        break;
                    case "05":
                        System.out.println("Received QUIT");
                        break;
                    case "06":
                        System.out.println("Received END");
                        break;
                    case "07":
                        System.out.println("Received LIST_CHALLENGES");
                        break;
                    case "08":
                        System.out.println("Received MAKE_CHALLENGE");
                        break;
                    case "09":
                        System.out.println("Received ACCEPT_CHALLENGE");
                        break;
                    case "10":
                        System.out.println("Received DELETE_CHALLENGE");
                        break;
                    case "11":
                        System.out.println("Received ANSWER");
                        break;
                    case "12":
                        System.out.println("Received RETRANSMIT");
                        break;
                    case "13":
                        System.out.println("Received LIST_RANKING");
                        break;    
                    default:
                        System.out.println(inp + " is an invalid command");
                        break;
                }
            
    }

    public void reply(String s){
        try {
            byte[] buf = s.getBytes();
            InetAddress address = InetAddress.getByName(null);
            
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            socket.send(packet);
            System.out.println("Sent Packet " + new String(buf));
        } catch (Exception ex) {
            Logger.getLogger(WorkerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized boolean login(String username, String password) {
        return true;
        /*
        Account user = database.findUser(username);
        if (user != null) {
            if (user.validate(password)) {
                this.currentUser = user;
                this.loggedIn = true;
                System.out.println("Login successful from " + currentUser.getUsername());
                return true;
            }
        } else {
            System.out.println("Login failed, credentials not valid");
            return false;
        }
        return false;
                */
    }


  
}
