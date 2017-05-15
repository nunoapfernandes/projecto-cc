package front_end_server;


import pdu_data.PDUManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Map;

public class Server_UDP_Listener extends Thread{

    private Data backpool_server_data;
    private Map<InetAddress,Monitor_Handler_Udp> udp_handlers;
    private int burstSize;

    public Server_UDP_Listener(Data backpool_server_data, Map<InetAddress,Monitor_Handler_Udp> udp_handlers, int burstSize){
        this.backpool_server_data = backpool_server_data;
        this.udp_handlers = udp_handlers;
        this.burstSize = burstSize;
    }

    @Override
    public void run(){
        DatagramSocket incoming_socket;
        PDUManager pdu_received;
        try {
            incoming_socket= new DatagramSocket(5555);
            byte[] data_received = new byte[1024];

            while (!Thread.interrupted()){
                DatagramPacket packet_received = new DatagramPacket(data_received,data_received.length);
                incoming_socket.receive(packet_received);

                pdu_received = new PDUManager(packet_received.getData());

                /** Análise do PDU consoante tipo*/
                /** Pedido de registo de servidor no reverse proxy */
                if(pdu_received.getType() == 1){
                    if (this.backpool_server_data.existsClient(pdu_received.getIp_address())) {
                        this.backpool_server_data.updateLastLog(pdu_received.getIp_address(), System.currentTimeMillis());
                    } else {
                        Client_Info monitor = new Client_Info(pdu_received.getIp_address(), System.currentTimeMillis(), this.burstSize);
                        this.backpool_server_data.addClient(monitor);
                        /** Inicia nova thread de probing para o novo monitor */
                        Monitor_Handler_Udp new_handler_monitor_udp = new Monitor_Handler_Udp(monitor, burstSize);
                        new_handler_monitor_udp.start();
                        /** Insere na lista de monitores já presentes */
                        this.udp_handlers.put(monitor.getIp_address(),new_handler_monitor_udp);
                    }
                } else if(pdu_received.getType() == 3) {
                    Client_Info monitor = this.backpool_server_data.getClientInfo(pdu_received.getIp_address());

                    if (monitor.getBurstCounter() > pdu_received.getCounter())
                        monitor.resetRTT();

                    if (monitor.getBurstCounter() >= pdu_received.getCounter())
                        monitor.addRTTime(System.currentTimeMillis() - pdu_received.getTimestamp());

                    this.backpool_server_data.updateScore(monitor);
                }
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
