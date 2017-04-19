package front_end_server;


import pdu_data.PDUManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Server_UDP_Listener extends Thread{

    private Data backpool_server_data;
    private Map<Client_Info,Monitor_Handler_Udp> udp_handlers;

    public Server_UDP_Listener(Data backpool_server_data, Map<Client_Info,Monitor_Handler_Udp> udp_handlers){
        this.backpool_server_data = backpool_server_data;
        this.udp_handlers = udp_handlers;
    }

    @Override
    public void run(){
        DatagramSocket incoming_socket;
        PDUManager pdu_received = new PDUManager();
        try {
            incoming_socket= new DatagramSocket(5555);
            byte[] data_received = pdu_received.buildPDU();

            while (true){
                DatagramPacket packet_received = new DatagramPacket(data_received,data_received.length);
                incoming_socket.receive(packet_received);

                pdu_received = new PDUManager(packet_received.getData());

                /** Análise do PDU consoante tipo*/

                /** Pedido de registo de servidor no reverse proxy */
                if(pdu_received.getType() == 1){
                    /** Cria novo registo de monitor */
                    Client_Info new_monitor_udp = new Client_Info();
                    /** Associa ao registo o ip recevido pelo PDU */
                    new_monitor_udp.setIp_address(pdu_received.getIp_address());
                    /** Insere os dados do novo monitor */
                    backpool_server_data.Score(new_monitor_udp);
                    /** Inicia nova thread de probing para o novo monitor */
                    Monitor_Handler_Udp new_handler_monitor_udp = new Monitor_Handler_Udp(new_monitor_udp);
                    /** Insere na lista de monitores já presentes */
                    udp_handlers.put(new_monitor_udp,new_handler_monitor_udp);
                }

                else if(pdu_received.getType() == 3){
                    /** Caso seja do tipo 3 - ANSWER, o monitor já existe nos dados, logo vai-se buscar as suas informações */
                    Client_Info old_monitor = backpool_server_data.getClientInfo(pdu_received.getIp_address());
                    /** Caso o contador recebido seja o mesmo do último enviado, calcula-se o RTT pela diferença entre os timestamps */
                    if(pdu_received.getCounter() == old_monitor.getLastSentCounter()) {
                        old_monitor.setRound_trip_time(System.currentTimeMillis()-pdu_received.getTimestamp());
                    }

                    /** Caso o contador recebido tenha uma diferença maior que 1 sobre o último recebido, significa que se perdeu packets */
                    if(pdu_received.getCounter()-old_monitor.getLastReceivedCounter() > 1){
                        old_monitor.incrementPacket_loss(pdu_received.getCounter()-old_monitor.getLastReceivedCounter()-1);
                    }
                    old_monitor.setLastReceivedCounter(pdu_received.getCounter());
                    backpool_server_data.Score(old_monitor);
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
