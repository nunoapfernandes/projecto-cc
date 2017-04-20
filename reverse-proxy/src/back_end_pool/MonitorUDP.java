package back_end_pool;


import pdu_data.PDUManager;
import pdu_data.TypeNotFoundException;

import java.io.IOException;
import java.net.*;

public class MonitorUDP {



    public static void main(String[] args)  {

        MonitorUDP_Register register = new MonitorUDP_Register();
        MonitorUDP_Answer answer = new MonitorUDP_Answer();

        register.start();
        answer.start();
    }
}
