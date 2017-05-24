package back_end_pool;

import pdu_data.PDUManager;

import java.io.IOException;
import java.net.*;

public class MonitorUDP_Register extends Thread {

    private InetAddress MyIPAddress;
    private DatagramSocket clientSocket;

    public MonitorUDP_Register(InetAddress ip_address, DatagramSocket datagramSocket){
        this.MyIPAddress = ip_address;
        this.clientSocket = datagramSocket;
    }

    public void run() {
        try {
            while (!Thread.interrupted()) {
                InetAddress ServerIPAddress = InetAddress.getByName("10.3.3.10");
                PDUManager message = new PDUManager(1, this.MyIPAddress);

                DatagramPacket sendPacket = new DatagramPacket(message.buildPDU(), message.buildPDU().length, ServerIPAddress, 5555);
                clientSocket.send(sendPacket);
                Thread.sleep(5 * 1000);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {}
    }
}
