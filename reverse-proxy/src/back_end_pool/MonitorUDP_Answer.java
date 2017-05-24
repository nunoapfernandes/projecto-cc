package back_end_pool;


import pdu_data.PDUManager;
import pdu_data.TypeNotFoundException;

import java.io.IOException;
import java.net.*;

public class MonitorUDP_Answer extends Thread {

    private InetAddress MyIPAddress;
    private DatagramSocket clientSocket;

    public MonitorUDP_Answer(InetAddress ip_address, DatagramSocket datagramSocket){
        this.MyIPAddress = ip_address;
        this.clientSocket = datagramSocket;
    }

    public void run() {

        try {
            InetAddress ServerIPAddress = InetAddress.getByName("10.3.3.10");

            while (!Thread.interrupted()) {
                //RECEBE MENSAGEM E CONSTROI PDUManager
                PDUManager messageReceived = new PDUManager();
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                messageReceived.generatePDU(receiveData);

                //ENVIA UM PDUManager DO TIPO ANSWER, COM O PRÓPRIO IP E O CONTADOR DA ULTIMA MENSAGEM QUE RECEBEU
                PDUManager message = new PDUManager(3, this.MyIPAddress, messageReceived.getTimestamp(), messageReceived.getCounter());
                DatagramPacket sendPacket = new DatagramPacket(message.buildPDU(), message.buildPDU().length, ServerIPAddress, 5555);
                clientSocket.send(sendPacket);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TypeNotFoundException e) {
            e.printStackTrace();
        }
    }
}
