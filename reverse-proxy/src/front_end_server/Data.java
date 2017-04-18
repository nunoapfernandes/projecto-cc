package front_end_server;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;

public class Data {

    ArrayList<Client_Info> Ranking;
    HashMap<InetAddress,Client_Info> Registo;

    public Data() {
       Ranking = new ArrayList<Client_Info>();
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

            Collections.sort(Ranking, new ServerComparator());

    }
}
