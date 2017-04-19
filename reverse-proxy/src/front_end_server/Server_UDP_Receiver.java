package front_end_server;


import pdu_data.PDUManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server_UDP_Receiver extends Thread{

    public Server_UDP_Receiver(){}

    @Override
    public void run(){
        DatagramSocket incoming_socket;
        PDUManager pdu_received;
        try {
            incoming_socket= new DatagramSocket(5555);
            byte[] data_received = new byte[256];

            while (true){
                DatagramPacket packet_received = new DatagramPacket(data_received,data_received.length);
                incoming_socket.receive(packet_received);

                pdu_received = new PDUManager(packet_received.getData());



            }
        }catch (SocketException e){
            System.out.println("Create socket error");
            e.printStackTrace();
        }catch (IOException e){
            System.out.println("Error receiving data");
            e.printStackTrace();
        }
    }
}
