package front_end_server;


import java.util.Comparator;

class ServerComparator implements Comparator{

    public int compare(Object o1, Object o2){
            Client_Info c1 = (Client_Info)o1;
            Client_Info c2 = (Client_Info)o2;

            if (c1.getScore()==c2.getScore())
                return 0;
            else if (c1.getScore() > c2.getScore())
                    return 1;
                else return -1;
    }

}
