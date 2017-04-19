package front_end_server;


import pdu_data.PDUManager;
import pdu_data.TypeNotFoundException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Queue;

public class Server {


    private Server(){}

    public static void main(String[] args){

        Data backpool_servers_data = new Data();
        Queue<PDUManager> messages = new Queue<PDUManager>();

        /*PDUManager teste = new PDUManager();

        try{
            teste.setType(1);
            teste.setIp_address(InetAddress.getByName("192.168.1.172"));
            teste.setTimestamp(2);

            System.out.println(teste.toString());

            byte[] aux = teste.buildPDU();

            PDUManager res = new PDUManager(aux);

            System.out.println(res.toString());

        }catch (UnknownHostException e){e.printStackTrace();}*/



    }
}
