package front_end_server;

import java.net.InetAddress;

public class Client_Info {

    private InetAddress ip_address;
    private double round_trip_time;
    private int tcp_connections;
    private int packet_loss;
    private double score;
    private int lastSentCounter;
    private int lastReceivedCounter;

    public Client_Info(){
        this.ip_address = null;
        this.round_trip_time = 50000;
        this.tcp_connections = 0;
        this.packet_loss = 50000;
        this.setScore(50000);
        this.lastReceivedCounter = 0;
        this.lastSentCounter = 0;
    }

    public Client_Info(InetAddress ip_address, double round_trip_time, int tcp_connections, int packet_loss, int score){
        this.ip_address = ip_address;
        this.round_trip_time = round_trip_time;
        this.tcp_connections = tcp_connections;
        this.packet_loss = packet_loss;
        this.setScore(score);
        this.lastReceivedCounter = 0;
        this.lastSentCounter = 0;
    }

    public Client_Info(Client_Info client_info){
        this.ip_address = client_info.getIp_address();
        this.round_trip_time = client_info.getRound_trip_time();
        this.tcp_connections = client_info.getTcp_connections();
        this.packet_loss = client_info.getPacket_loss();
        this.setScore(client_info.getScore());
        this.lastReceivedCounter = client_info.getLastReceivedCounter();
        this.lastSentCounter = client_info.getLastSentCounter();
    }

    public InetAddress getIp_address() { return this.ip_address; }

    public void setIp_address(InetAddress ip_address) { this.ip_address = ip_address; }

    public double getRound_trip_time() { return this.round_trip_time; }

    public void setRound_trip_time(double round_trip_time) { this.round_trip_time = round_trip_time; }

    public int getTcp_connections() { return this.tcp_connections; }

    public void setTcp_connections(int tcp_connections) { this.tcp_connections = tcp_connections; }

    public int getPacket_loss() { return this.packet_loss; }

    public void setPacket_loss(int packet_loss) { this.packet_loss = packet_loss; }

    public double getScore() { return score; }

    public void setScore(double score) { this.score = score; }

    public int getLastSentCounter() { return lastSentCounter; }

    public void setLastSentCounter(int lastSentCounter) { this.lastSentCounter = lastSentCounter; }

    public int getLastReceivedCounter() { return lastReceivedCounter; }

    public void setLastReceivedCounter(int lastReceivedCounter) { this.lastReceivedCounter = lastReceivedCounter; }

    public void incrementsReceivedCounter() { this.lastReceivedCounter++; }

    public void incrementSentCounter() { this.lastSentCounter++;}


    @Override
    public String toString(){
        StringBuilder sp = new StringBuilder();

        sp.append("IP Address:" + this.getIp_address() +"\n");
        sp.append("Round Trip Time:" + this.getRound_trip_time() + "\n");
        sp.append("TCP Connections:" + this.getTcp_connections() + "\n");
        sp.append("Packet Loss:" + this.getPacket_loss());

        return sp.toString();
    }

    @Override
    public Client_Info clone(){
        return new Client_Info(this);
    }

    @Override
    public boolean equals(Object c){
        if(this == c) return true;
        if((c==null)|| (this.getClass()!=c.getClass())) return false;
        else {
            Client_Info aux = (Client_Info) c;

            return ((this.getIp_address().equals(aux.getIp_address())) &&
                    (this.getRound_trip_time()==aux.getRound_trip_time()) &&
                    (this.getTcp_connections()==aux.getTcp_connections()) &&
                    (this.getPacket_loss()==aux.getPacket_loss())
            );
        }
    }
}
