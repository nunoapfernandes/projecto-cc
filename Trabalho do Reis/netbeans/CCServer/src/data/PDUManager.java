/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author Miguel
 */
public class PDUManager {
    
    private String version;
    private String security;
    private String label;
    private String type;
    private String fieldNum;
    private String fieldSize;
    private String data; //max 255 bytes

    public PDUManager(){
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFieldNum() {
        return fieldNum;
    }

    public void setFieldNum(String fieldNum) {
        this.fieldNum = fieldNum;
    }

    public String getFieldSize() {
        return fieldSize;
    }

    public void setFieldSize(String fieldSize) {
        this.fieldSize = fieldSize;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    
    public PDUManager(String d){
        decodePDU(d);
    }
    
    public PDUManager(String v, String sec, String lb, String t, String fn, String fs, String d){
        version = v;
        security = sec;
        label = lb;
        type = t;
        fieldNum = fn;
        fieldSize = fs;
        data = d;
    }
    
    public String buildPDU(){
        String pdu = "";
        pdu = "ver="+version+","
             +"seg="+security+","
             +"label="+label+","
             +"tipo="+type+","
             +"num="+fieldNum+","
             +"siz="+fieldSize+","   
             +data+".";
        return pdu;   
    }
    
    public boolean pduType(){
        return type != "01" && type != "04" && type != "05"
                    && type != "06" && type != "07" && type != "13";
    }
    
    public void decodePDU(String pdu){
        if(pdu.length() >= 5 ){
            version = pdu.substring(4, 6);
            security = pdu.substring(11, 13);
            label = pdu.substring(20, 22);
            type = pdu.substring(28, 30);
            fieldNum = pdu.substring(35, 37);
            fieldSize = pdu.substring(44, 46);
            data = pdu.substring(51, pdu.length());
        }
    }
}
