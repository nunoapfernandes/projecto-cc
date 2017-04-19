package front_end_server;


import pdu_data.PDUManager;
import pdu_data.TypeNotFoundException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class Server {


    private Server(){}

    private static Menu mainMenu;

    public static void main(String[] args){

        Data backpool_servers_data = new Data();
        Map<Client_Info,Monitor_Handler_Udp> udp_handlers = new HashMap<>();

        Server_UDP_Listener listener = new Server_UDP_Listener(backpool_servers_data,udp_handlers);

        listener.start();



        loadMenu();

        do{
            mainMenu.executeMenu();
            switch (mainMenu.getOption()){
                case 1: System.out.println("Number of servers being handled:" + udp_handlers.size());
                        break;
                case 2: break;
            }
        } while (mainMenu.getOption()!=0);

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
                "List ip of monitored servers"
        };

        mainMenu = new Menu(main_menu);
    }
}
