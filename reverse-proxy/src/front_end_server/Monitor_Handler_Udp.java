package front_end_server;


import pdu_data.PDUManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Timestamp;

public class Monitor_Handler_Udp extends Thread{

    private Client_Info client_info;
    private PDUManager pdu;
    private int counter;

    public Monitor_Handler_Udp(Client_Info client_info){
        this.client_info = client_info;
        this.pdu = new PDUManager();
        this.counter=1;
    }
    @Override
    public void run(){
        this.pdu.setType(2);
        this.pdu.setTimestamp(System.currentTimeMillis());
        this.pdu.setCounter(counter);
        try{
            DatagramSocket client = new DatagramSocket();

            byte[] data = pdu.buildPDU();

            while(true){
                DatagramPacket send_packet = new DatagramPacket(data,data.length,client_info.getIp_address(),5555);
                client.send(send_packet);

                Thread.sleep(5*1000);

                this.pdu.setTimestamp(System.currentTimeMillis());
                this.pdu.incrementCounter();
                data = this.pdu.buildPDU();
            }
        }catch (SocketException|InterruptedException e){
            System.out.println("Socket Exception");
            e.printStackTrace();
        }catch (IOException e){
            System.out.println("IO Exception");
            e.printStackTrace();
        }
    }

}
