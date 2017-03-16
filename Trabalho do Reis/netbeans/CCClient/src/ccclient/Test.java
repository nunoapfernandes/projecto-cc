/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccclient;

//import data.PDUManager;
import gui.Jogo;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;



public class Test {
    
    private static final int port = 26500;
    
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException, LineUnavailableException, UnsupportedAudioFileException{
        InetAddress address = InetAddress.getByName(null);
    
        DatagramSocket socket = new DatagramSocket();
        //PDUManager pdu = new PDUManager("00","00","00","01","00","00", "data");
       // byte[] buf = pdu.buildPDU().getBytes();
    
        //DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        //socket.send(packet);
        //System.out.println("Sent Packet " + new String(buf));
        
        ImageIcon i = new ImageIcon("C:\\Users\\nmore_000\\Desktop\\ao0znRA_460s.jpg");
        //ImageIcon i = new ImageIcon("C:\\Users\\Miguel\\Dropbox\\Aulas\\CC\\Praticas\\Kit TP2-LEI-CC\\Kit TP2-LEI-CC\\imagens\\000001.jpg");
        ArrayList<String> a = new ArrayList();
        a.add("resposta1");
        a.add("resposta2");
        a.add("resposta3");
        a.add("resposta4");     
        String s = "D:\\08. I Am a River.wav";
        Pergunta p = new Pergunta(i, s, a);
        Jogo jogo = new Jogo();
        jogo.mudaPergunta(p);
        jogo.play();
    }
    
    
}
