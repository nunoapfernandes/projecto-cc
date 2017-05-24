package front_end_server;


import sun.net.util.IPAddressUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Monitor_Handler_TCP extends Thread {

        int port = 80;
        Socket s;
        private Data backpool_servers_data;

    public Monitor_Handler_TCP(Socket s, Data backpool_servers_data) {
        this.s =s;
        this.backpool_servers_data = backpool_servers_data;
    }



    @Override
    public void run(){
        try {
            Client_Info serv = backpool_servers_data.nextserver();
            InetAddress ipudp = serv.getIp_address();
            Socket web_server = new Socket(ipudp.toString(),80);


            InputStream clientIn = s.getInputStream();
            OutputStream clientOut = s.getOutputStream();
            InputStream webserverIn = web_server.getInputStream();
            OutputStream webserverOut = web_server.getOutputStream();

            byte[] request = new byte[1024];

            /**LÊ PEDIDO DO CLIENTE E REENCAMINHA PARA O WEBSERVER*/
            clientIn.read(request);
            webserverOut.write(request);
            webserverOut.flush();

            /**LÊ RESPOSTA DO WEBSERVER E DEVOLVE PARA O CLIENTE*/
            webserverIn.read(request);
            clientOut.write(request);
            clientOut.flush();




        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



}
