package back_end_pool;

import pdu_data.PDUManager;

import java.io.IOException;
import java.net.*;

public class MonitorUDP_Register extends Thread {

    public void run() {
        try {
            while (true) {
                InetAddress MyIPAddress = null;
                MyIPAddress = InetAddress.getByName("172.16.82.129");
                InetAddress ServerIPAddress = InetAddress.getByName("192.168.1.172");
                PDUManager message = new PDUManager(1, MyIPAddress, System.currentTimeMillis());

                DatagramSocket clientSocket = new DatagramSocket(5555);
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
