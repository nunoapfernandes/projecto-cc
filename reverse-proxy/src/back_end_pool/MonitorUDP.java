package back_end_pool;


import pdu_data.PDUManager;
import pdu_data.TypeNotFoundException;

import java.io.IOException;
import java.net.*;

public class MonitorUDP {



    public static void main(String[] args) throws IOException, TypeNotFoundException {

        InetAddress MyIPAddress = InetAddress.getByName("172.16.82.129");
        InetAddress ServerIPAddress = InetAddress.getByName("192.168.1.172");
        PDUManager message = new PDUManager(1, MyIPAddress);

        DatagramSocket clientSocket = new DatagramSocket(5555);
        DatagramPacket sendPacket = new DatagramPacket(message.buildPDU(), message.buildPDU().length, ServerIPAddress, 5555);
        clientSocket.send(sendPacket);

        while (true) {
            //RECEBE MENSAGEM E CONSTROI PDUManager
            PDUManager messageReceived = new PDUManager();
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            messageReceived.generatePDU(receiveData);

            //ENVIA UM PDUManager DO TIPO ANSWER, COM O PRÓPRIO IP E O CONTADOR DA ULTIMA MENSAGEM QUE RECEBEU
            message = new PDUManager(3, MyIPAddress, messageReceived.getCounter());
            sendPacket = new DatagramPacket(message.buildPDU(), message.buildPDU().length, ServerIPAddress, 5555);
            clientSocket.send(sendPacket);

        }
    }
}
