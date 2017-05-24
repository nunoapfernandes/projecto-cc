package front_end_server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server_TCP_Listener extends Thread{

    ServerSocket serverSocket;
    List<Monitor_Handler_TCP> webservers;
    Data data;

    public Server_TCP_Listener(Data data) {
        this.data = data;
    }

    @Override
    public void run() {

        try {
            serverSocket = new ServerSocket(80);
            webservers = new ArrayList<>();

            while (!Thread.interrupted()) {

                Socket socket = serverSocket.accept();
                Monitor_Handler_TCP monitor_handler_tcp = new Monitor_Handler_TCP(socket, data);

                webservers.add(monitor_handler_tcp);
                monitor_handler_tcp.start();
            }

            for(Monitor_Handler_TCP monitor: webservers){
                monitor.interrupt();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
