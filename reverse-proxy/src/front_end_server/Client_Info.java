package front_end_server;

import java.net.InetAddress;
import java.util.ArrayList;

public class Client_Info {

    private InetAddress ip_address;

    private long round_trip_time;
    private int tcp_connections;
    private int packet_loss;
    private double score;

    private ArrayList<Long> rtTimes;
    private long lastLog;
    private int burstCounter;
    private int burstSize;


    public Client_Info(){
        this.ip_address = null;
        this.round_trip_time = 50000;
        this.tcp_connections = 0;
        this.packet_loss = 50000;
        this.score = 50000;
        this.rtTimes = new ArrayList<>();
        this.lastLog = 0;
        this.burstCounter = 0;
        this.burstSize = 100;
    }

    public Client_Info (InetAddress ip_address, long lastLog, int burstSize) {
        this.ip_address = ip_address;
        this.round_trip_time = 50000;
        this.tcp_connections = 0;
        this.packet_loss = 50000;
        this.score = 50000;
        this.rtTimes = new ArrayList<>();
        this.lastLog = lastLog;
        this.burstCounter = 0;
        this.burstSize = burstSize;
    }

    public Client_Info(Client_Info client_info){
        this.ip_address = client_info.getIp_address();
        this.round_trip_time = client_info.getRound_trip_time();
        this.tcp_connections = client_info.getTcp_connections();
        this.packet_loss = client_info.getPacket_loss();
        this.score = client_info.getScore();
        this.rtTimes = client_info.getRtTimes();
        this.lastLog = client_info.getLastLog();
        this.burstCounter = client_info.getBurstCounter();
        this.burstSize = client_info.getBurstSize();
    }

    public InetAddress getIp_address() { return this.ip_address; }

    public void setIp_address(InetAddress ip_address) { this.ip_address = ip_address; }

    public long getRound_trip_time() { return this.round_trip_time; }

    public void setRound_trip_time(long round_trip_time) { this.round_trip_time = round_trip_time; }

    public int getTcp_connections() { return this.tcp_connections; }

    public void setTcp_connections(int tcp_connections) { this.tcp_connections = tcp_connections; }

    public int getPacket_loss() { return this.packet_loss; }

    public void setPacket_loss(int packet_loss) { this.packet_loss = packet_loss; }

    public double getScore() { return score; }

    public void setScore(double score) { this.score = score; }

    public ArrayList<Long> getRtTimes() { return rtTimes; }

    public void addRTTime(long rtt) {
        rtTimes.add(rtt);
    }

    public long getLastLog() { return lastLog; }

    public void setLastLog(long lastLog) { this.lastLog = lastLog; }

    public int getBurstCounter() { return burstCounter; }

    public void setBurstCounter(int burstCounter) { this.burstCounter = burstCounter; }

    public void incrementBurstCounter() { this.burstCounter++; }

    public int getBurstSize() { return burstSize; }

    public void setBurstSize(int burstSize) { this.burstSize = burstSize; }

    public void resetRTT() {
        long average = 0;
        for (Long rtt : this.rtTimes) {
            average += rtt;
        }

        round_trip_time = average / this.rtTimes.size();
        if (this.rtTimes.size() != 0) {
            this.packet_loss = this.burstSize - this.rtTimes.size();
        } else {
            this.packet_loss = this.burstSize;
        }
        this.rtTimes = new ArrayList<>();
    }


    @Override
    public String toString(){
        StringBuilder sp = new StringBuilder();

        sp.append("IP Address: " + this.getIp_address() +"\n");
        sp.append("Round Trip Time: " + this.getRound_trip_time() + "\n");
        sp.append("TCP Connections: " + this.getTcp_connections() + "\n");
        sp.append("Packet Loss: " + this.getPacket_loss() + '\n');
        sp.append("Score: " + this.getScore() + '\n');
        sp.append("Round Trip Times: " + this.rtTimes.toString() + '\n');
        sp.append("Burst Counter: " + this.getBurstCounter() + '\n');
        sp.append("Burst Size: " + this.getBurstSize() + '\n');

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
                    (this.getRound_trip_time() == aux.getRound_trip_time()) &&
                    (this.getTcp_connections() == aux.getTcp_connections()) &&
                    (this.getPacket_loss() == aux.getPacket_loss()) &&
                    (this.getScore() == aux.getScore()) &&
                    (this.getBurstCounter() == aux.getBurstCounter())
            );
        }
    }
}
