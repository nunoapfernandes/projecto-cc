package pdu_data;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class PDUManager {
    private int type; /* 1 - REGISTER; 2 - PROBE ; 3 - ANSWER*/
    private InetAddress ip_address;
    private long timestamp;
    private int counter;

    public PDUManager(){
        this.type = 0;
        this.ip_address = null;
        this.timestamp = 0;
        this.counter = 0;
    }

    public PDUManager(int type, InetAddress ip_address, long timestamp, int counter){
        this.type = type;
        this.ip_address = ip_address;
        this.timestamp = timestamp;
        this.counter = counter;
    }

    public PDUManager(byte[] message){
        try {
            generatePDU(message);
        } catch (TypeNotFoundException e) {
            e.printStackTrace();
        }
    }

    public PDUManager(PDUManager p){
        this.type = p.getType();
        this.ip_address = p.getIp_address();
        this.timestamp = p.getTimestamp();
        this.counter = p.getCounter();
    }

    public int getType(){return this.type;}
    public void setType(int type){this.type=type;}

    public InetAddress getIp_address(){return this.ip_address;}
    public void setIp_address(InetAddress ip_address){this.ip_address=ip_address;}

    public long getTimestamp(){return this.timestamp;}
    public void setTimestamp(long timestamp){this.timestamp = timestamp;}

    public int getCounter() { return counter; }
    public void setCounter(int counter) { this.counter = counter; }

    public byte[] buildPDU(){
        return this.toString().getBytes();
    }

    public void decodePDU(byte[] pdu) throws TypeNotFoundException{
        String[]fields = pdu.toString().split(",");
        String aux;

        aux = fields[0].substring(2);
        if(aux==null || Integer.getInteger(aux)>3 || Integer.getInteger(aux)<1){
            throw new TypeNotFoundException();
        }
        else this.setType(Integer.getInteger(aux));

        aux = fields[1].substring(2);

        if(aux != null){
            try{this.setIp_address(InetAddress.getByName(aux));}
            catch (UnknownHostException e){e.printStackTrace();}
        }

        aux = fields[2].substring(2);

        if(aux!= null){
            this.setTimestamp(Long.getLong(aux));
        }
    }

    public void generatePDU(byte[] pdu) throws TypeNotFoundException{
        String res = new String(pdu);

        String[]fields = res.split(",");

        String type, ip,timestamp;

        type = fields[0].substring(3);
        ip = fields[1].substring(4);
        timestamp = fields[2].substring(3);

        if(Integer.parseInt(type)>3 || Integer.parseInt(type)<1){
            throw new TypeNotFoundException();
        }
        else {
            this.setType(Integer.parseInt(type));
        }

        try{this.setIp_address(InetAddress.getByName(ip));}
        catch (UnknownHostException|NullPointerException e){e.printStackTrace();}

        this.setTimestamp(Long.parseLong(timestamp));


    }

    @Override
    public String toString(){
        StringBuilder sp = new StringBuilder();

        sp.append("ty=" + this.getType() + ",");
        sp.append("ip=" + this.getIp_address()+ ",");
        sp.append("ti=" + this.getTimestamp());

        return sp.toString();
    }

    @Override
    public PDUManager clone(){
        return new PDUManager(this);
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if((o==null) || (this.getClass()!=o.getClass())) return false;

        else{
            PDUManager p = (PDUManager) o;
            return ((this.getType()==p.getType())&&
                    (this.getIp_address().equals(p.getIp_address())) &&
                    (this.getTimestamp()==p.getTimestamp())
            );
        }
    }
}
