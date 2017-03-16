package data;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel
 */
public class Account implements Serializable{
    private String hash;
    private String salt;
    private String username;
    
    public Account( String username, String salt, String hash){
        this.username = username;
        this.salt = salt;
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public String getSalt() {
        return salt;
    }

    public String getUsername() {
        return username;
    }
    
    public static String genHash(String pw, String salt){
        String hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(pw.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            hash = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {}
        return hash;
    }
    
    public boolean validate(String pw){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(this.salt.getBytes());
            byte[] bytes = md.digest(pw.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            if(this.hash.compareTo(sb.toString()) == 0)
                return true;
            else
                return false;
        }
        catch (NoSuchAlgorithmException e) {}
        return false;
    } 

    public static String genSalt(){
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] salt = new byte[16];
        if(sr != null) sr.nextBytes(salt);
        return Arrays.toString(salt);
    }
}
