package front_end_server;


import java.util.Comparator;

public class ServerComparator implements Comparator<Client_Info>{

    public int compare(Client_Info c1, Client_Info c2){
            if (c1.getScore()==c2.getScore())
                return 0;
            else if (c1.getScore() > c2.getScore())
                    return 1;
                else return -1;
    }

}
