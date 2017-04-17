package ccclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel
 */
public class CCClient {

    private static final int port = 26500;
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private boolean loggedIn = false;
    
    public CCClient(){
        try{
            socket = new Socket( InetAddress.getByName(null), port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
             System.out.println("Connection refused");
         }
    }
    
    public void shutdown(){
        try {
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(CCClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean login(String username, String password){
        try {
            out.println("login "+username+" "+ password);
            out.flush();
            System.out.println("Sent command: login "+username+" "+ password);
            String inp = in.readLine();
            if(inp != null)
                if( inp.compareTo("success") == 0 ){
                    loggedIn = true;
                    return true;
                }
            return false;
        } catch (IOException ex) {
            Logger.getLogger(CCClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
