package front_end_server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_TCP_Listener extends Thread{

    ServerSocket serverSocket;
    Data data;

    public Server_TCP_Listener(Data data) {
        this.data = data;
    }

    @Override
    public void run() {

        try {

            while (true) {
                Socket socket = serverSocket.accept();
                Thread monitor_handler_tcp = new Monitor_Handler_TCP(socket, data);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
