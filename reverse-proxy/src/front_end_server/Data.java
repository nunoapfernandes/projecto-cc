package front_end_server;


import java.awt.*;
import java.net.InetAddress;
import java.util.*;

public class Data {

    PriorityQueue<Client_Info> Ranking;
    HashMap<InetAddress,Client_Info> Registo;

    public Data() {
       Ranking = new PriorityQueue<Client_Info>(100,new ServerComparator());
       Registo = new HashMap<InetAddress,Client_Info>();
    }

    public void Score(Client_Info client_info) {

        if (Registo.containsKey(client_info.getIp_address())) {

            Client_Info anterior = Registo.get(client_info.getIp_address());
            Ranking.remove(anterior);
            Registo.remove(anterior.getIp_address());
        }


            client_info.setScore(
                    client_info.getPacket_loss() * 0.3 +
                    client_info.getRound_trip_time() * 0.5 +
                    client_info.getTcp_connections() * 0.2);


            Ranking.add(client_info);
            Registo.put(client_info.getIp_address(),client_info);


    }

    public Client_Info nextserver(){
        return Ranking.poll();
    }

    public void lostTCP(Client_Info client_info){
        client_info.setTcp_connections(client_info.getTcp_connections()-1);
        Score(client_info);
    }

    public void newTCP(Client_Info client_info){
        client_info.setTcp_connections(client_info.getTcp_connections()+1);
        Score(client_info);
    }

    public void update(Client_Info client_info, double rtt, int pl){

        client_info.setPacket_loss(pl);
        client_info.setRound_trip_time(rtt);
        Score(client_info);
    }

    public Client_Info getClientInfo(InetAddress inetAddress){
        return this.Registo.get(inetAddress);
    }
}
