
package ccclient;

import data.PDUManager;


public class Resposta {
        private int pedido;
        private int campo;
        private String descricao;
        
      
    public Resposta(int pedido, int campo,String descricao){
        this.pedido = pedido;
        this.campo = campo;
        this.descricao = descricao;
    }
    
    public void run(){
            
            PDUManager pdu = new PDUManager();
            System.out.println(pdu.buildPDU());
                switch (pdu.getType()) {
                    case "01":  System.out.println("Send HELLO");
                        break;
                    case "02":
                        (pdu.getData().getNome(),pdu.getData().getAlcunha(),pdu.getData().getPassword());
                        System.out.println("Send REGISTER");
                        break;
                    case "03":
                        System.out.println("Send LOGIN");
                        if(login(pdu.getData(),pdu.getData()))
                            reply(new PDUManager("00","00","00","14","00","00", "data").buildPDU());
                        break;
                    case "04":
                        System.out.println("Send LOGOUT");
                        break;
                    case "05":
                        System.out.println("Send QUIT");
                        break;
                    case "06":
                        System.out.println("Send END");
                        break;
                    case "07":
                        System.out.println("Send LIST_CHALLENGES");
                        break;
                    case "08":
                        System.out.println("Send MAKE_CHALLENGE");
                        break;
                    case "09":
                        System.out.println("Send ACCEPT_CHALLENGE");
                        break;
                    case "10":
                        System.out.println("Send DELETE_CHALLENGE");
                        break;
                    case "11":
                        System.out.println("Send ANSWER");
                        break;
                    case "12":
                        System.out.println("Send RETRANSMIT");
                        break;
                    case "13":
                        System.out.println("Send LIST_RANKING");
                        break;    
                    default:
                        System.out.println(inp + " is an invalid command");
                        break;
                }
            
          }
    
    
    }
     


