package front_end_server;

import sun.awt.windows.ThemeReader;

import java.util.Map;

public class Server_Monitor_Cleaner extends Thread {

    private Data backpool_server_data;
    private Map<Client_Info,Monitor_Handler_Udp> udp_handlers;

    public Server_Monitor_Cleaner(Data backpool_server_data, Map<Client_Info,Monitor_Handler_Udp> udp_handlers){
        this.backpool_server_data = backpool_server_data;
        this.udp_handlers = udp_handlers;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            udp_handlers.forEach( (k,v) ->
                    checkMonitor(k,v)
            );
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void checkMonitor(Client_Info k, Monitor_Handler_Udp v) {
        if (k.getLastSentCounter() - k.getLastReceivedCounter() > 6) {
            udp_handlers.remove(k);
            backpool_server_data.removeClientInfo(k);
            v.interrupt();
        }
    }
}
