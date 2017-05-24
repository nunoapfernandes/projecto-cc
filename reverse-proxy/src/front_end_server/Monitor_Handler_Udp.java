package front_end_server;


import pdu_data.PDUManager;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;


/* Class responsável por mandar os pedidos de probing */

public class Monitor_Handler_Udp extends Thread{

    private Client_Info client_info;
    private PDUManager pdu;
    private Data backpool_server_data;
    private int burstSize = 100;
    private long timeout;


    public Monitor_Handler_Udp(Client_Info client_info, Data backpool_server_data){
        this.client_info = client_info;
        this.backpool_server_data = backpool_server_data;
        this.pdu = new PDUManager();
        this.burstSize = backpool_server_data.getBurstSize();
        this.timeout = backpool_server_data.getTimeout();
    }

    /** Por cada pedido de monitoramento, uma thread é arrancada com o objectivo de mandar probes a cada 5 segundos */
    @Override
    public void run(){
        /** O tipo de cada probe no PDU genérico é 2 */
        this.pdu.setType(2);
        this.pdu.setIp_address(client_info.getIp_address());
        //System.out.println("Iniciando processo de envio");

        try{
            /** Criação do socket para envio dos PDUS */
            DatagramSocket client = new DatagramSocket();
            while(!Thread.interrupted()){
                /** Verificaçao do ultimo registo do monitor */
                if (System.currentTimeMillis() - client_info.getLastLog() > this.timeout) {
                    System.out.println("Monitor " + client_info.getIp_address() + " foi interrompido");
                    backpool_server_data.removeClient(client_info);
                    Thread.currentThread().interrupt();
                }
                this.pdu.incrementCounter();
                /** Preparação e envio de burst de pacotes a enviar */
                for (int i = 0; i < this.burstSize; i++) {
                    /** Preparação e envio do PDU para o servidor da back pool, tratado como cliente */
                    this.pdu.setTimestamp(System.currentTimeMillis());
                    byte[] data = pdu.buildPDU();
                    DatagramPacket send_packet = new DatagramPacket(data,data.length,client_info.getIp_address(),5555);
                    client.send(send_packet);
                }
                //System.out.println("Burst de " + this.burstSize + " pacotes enviado");
                /** Timer para execução do ciclo a cada 5 segundos*/
                Thread.sleep(5*1000);
            }
        }catch(InterruptedException e){
        }
        catch (SocketException e){
            System.out.println("Socket Exception");
            e.printStackTrace();
        }catch (IOException e){
            System.out.println("IO Exception");
            e.printStackTrace();
        }
    }

}
