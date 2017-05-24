package front_end_server;


import pdu_data.PDUManager;
import pdu_data.TypeNotFoundException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Server {


    private Server(){}
    private static Menu mainMenu;

    public static void main(String[] args){

        Data backpool_servers_data = new Data();

        Server_UDP_Listener listener_udp = new Server_UDP_Listener(backpool_servers_data);
        Server_TCP_Listener listener_tcp = new Server_TCP_Listener(backpool_servers_data);


        listener_udp.start();
        listener_tcp.start();


        loadMenu();

        do{
            mainMenu.executeMenu();
            switch (mainMenu.getOption()){
                case 1: System.out.println("Number of servers being handled:" + backpool_servers_data.getNumberOfRegisteredMonitors());
                        break;
                case 2: System.out.println("Ips monitored");
                        List<InetAddress> ips = backpool_servers_data.getIpAdresses();
                        for(InetAddress ip : ips){
                            System.out.println(ip.toString());
                        }
                        break;
            }
        } while (mainMenu.getOption()!=0);

        listener_udp.interrupt();
        listener_tcp.interrupt();
        System.out.println("Bye!");

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


    public static void loadMenu(){
        String[] main_menu = {
                "Show nr of servers",
                "List ip of monitored servers",
        };

        mainMenu = new Menu(main_menu);
    }
}
