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
        /*
        try{
            this.pdu.setIp_address(InetAddress.getByName("192.168.1.172"));
        }catch (UnknownHostException e){
            System.out.println("Couldn't obtain server ip address");
            e.printStackTrace();
        }
        */
        this.pdu.setIp_address(client_info.getIp_address());
        System.out.println("Iniciando processo de envio");

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
                    System.out.println(client_info.getIp_address());
                }
                System.out.println("Burst de " + this.burstSize + " pacotes enviado");
                /** Timer para execução do ciclo a cada 5 segundos*/
                Thread.sleep(5*1000);
                System.out.println(this.pdu.getCounter());
            }
        }catch (SocketException|InterruptedException e){
            System.out.println("Socket Exception");
            e.printStackTrace();
        }catch (IOException e){
            System.out.println("IO Exception");
            e.printStackTrace();
        }
    }

    private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // Iterate all NICs (network interface cards)...
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // Iterate all IP addresses assigned to each card...
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {

                        if (inetAddr.isSiteLocalAddress()) {
                            // Found non-loopback site-local address. Return it immediately...
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // Found non-loopback address, but not necessarily site-local.
                            // Store it as a candidate to be returned if site-local address is not subsequently found...
                            candidateAddress = inetAddr;
                            // Note that we don't repeatedly assign non-loopback non-site-local addresses as candidates,
                            // only the first. For subsequent iterations, candidate will be non-null.
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                // We did not find a site-local address, but we found some other non-loopback address.
                // Server might have a non-site-local address assigned to its NIC (or it might be running
                // IPv6 which deprecates the "site-local" concept).
                // Return this non-loopback candidate address...
                return candidateAddress;
            }
            // At this point, we did not find a non-loopback address.
            // Fall back to returning whatever InetAddress.getLocalHost() returns...
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }

}
