package front_end_server;


import java.net.InetAddress;
import java.util.*;

public class Data {

    PriorityQueue<Client_Info> ranking;
    HashMap<InetAddress,Client_Info> registo;

    public Data() {
       ranking = new PriorityQueue<Client_Info>(100,new ServerComparator());
       registo = new HashMap<InetAddress,Client_Info>();
    }

    public void Score(Client_Info client_info) {

        if (registo.containsKey(client_info.getIp_address())) {

            Client_Info anterior = registo.get(client_info.getIp_address());
            ranking.remove(anterior);
            registo.remove(anterior.getIp_address());
        }


            client_info.setScore(
                    client_info.getPacket_loss() * 0.3 +
                    client_info.getRound_trip_time() * 0.5 +
                    client_info.getTcp_connections() * 0.2);


            ranking.add(client_info);
            registo.put(client_info.getIp_address(),client_info);


    }

    public Client_Info nextserver(){
        return ranking.poll();
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

    public void removeClientInfo(Client_Info ci) {
        ranking.remove(ci);
        registo.remove(ci);
    }

    public Client_Info getClientInfo(InetAddress inetAddress){
        return this.registo.get(inetAddress);
    }
}
