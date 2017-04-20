package front_end_server;


import pdu_data.PDUManager;

import java.io.IOException;
import java.net.*;


/* Class responsável por mandar os pedidos de probing */

public class Monitor_Handler_Udp extends Thread{

    private Client_Info client_info;
    private PDUManager pdu;


    public Monitor_Handler_Udp(Client_Info client_info){
        this.client_info = client_info;
        this.pdu = new PDUManager();
    }

    /** Por cada pedido de monitoramento, uma thread é arrancada com o objectivo de mandar probes a cada 5 segundos */
    @Override
    public void run(){
        /** O tipo de cada probe no PDU genérico é 2 */
        this.pdu.setType(2);
        /** É necessário mandar um timestamp para cálculo do Round Trip Time */
        this.pdu.setTimestamp(System.currentTimeMillis());
        this.pdu.setCounter(1);
        try{
            this.pdu.setIp_address(InetAddress.getByName("192.168.1.172"));
        }catch (UnknownHostException e){
            System.out.println("Couldn't obtain server ip address");
            e.printStackTrace();
        }

        System.out.println("Iniciando processo de envio");

        try{
            /** Criação do socket para envio dos PDUS */
            DatagramSocket client = new DatagramSocket();

            /** Transformação dos PDUs para array de bytes para envio*/
            byte[] data = pdu.buildPDU();

            while(!Thread.interrupted()){
                /** Preparação e envio do PDU para o servidor da back pool, tratado como cliente */
                DatagramPacket send_packet = new DatagramPacket(data,data.length,client_info.getIp_address(),5555);
                client.send(send_packet);
                this.client_info.incrementSentCounter();
                System.out.println(client_info.getIp_address());
                System.out.println("Enviado");
                /** Timer para execução do ciclo a cada 5 segundos*/
                Thread.sleep(5*1000);

                
                this.pdu.setTimestamp(System.currentTimeMillis());
                this.pdu.incrementCounter();
                System.out.println(this.pdu.getCounter());
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
