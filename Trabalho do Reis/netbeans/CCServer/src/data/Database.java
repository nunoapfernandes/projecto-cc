package data;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel
 */
public class Database {
    
    private ArrayList<Account> users;
    private ArrayList<Challenge> challs;
    private Set<Question> questions;
    
    public Database(){
        users = new ArrayList();
        challs = new ArrayList();
    }
    
    public synchronized ArrayList<Account> getUsers() {
        return users;
    }
    
    public synchronized ArrayList<Challenge> getChalls() {
        return challs;
    }
    
    public synchronized boolean existsUser( String username ){
        for (Account user : users) {
            if (user.getUsername().compareTo(username) == 0) {
                return true;
            }
        }
        return false;
    }
    
    public synchronized Account findUser( String username ){
        for (Account user : users) {
            if (user.getUsername().compareTo(username) == 0) {
                return user;
            }
        }
        return null;
    }
    
    public synchronized void saveUsers(){
        String filename = "database/users.sd";
        ObjectOutputStream out = null;       
        try { try {
            out = new ObjectOutputStream(new
            BufferedOutputStream(new FileOutputStream(filename)));
            out.writeObject(users);
            } finally {out.close();}
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void save(){
        try {
            checkfolder();
            saveUsers();
        } catch (IOException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public void checkfolder() throws IOException{
    String path = new java.io.File(".").getCanonicalPath();
    Path p1 = Paths.get(path+"/database");
    File dir;
    if( !Files.exists(p1) ){
        dir = new File(p1.toString());
        dir.mkdir();
    }
}
    
    public ArrayList<Account> loadUsers(){
        ArrayList<Account> usr = new ArrayList<>();
        String filename = "database/users.sd";
        ObjectInputStream in = null;
        File f = new File(filename);
        try {
                if(f.exists()){ 
                    try {          
                            in = new ObjectInputStream(new
                            BufferedInputStream(new FileInputStream(filename)));
                            usr = (ArrayList<Account>) in.readObject();
                    } finally {
                        if( in != null ) in.close();
                    }
                }
        } catch (Exception ex) {Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);}
        return usr;
    }
    
    public synchronized void loadAll(){
        users = loadUsers();
    }
}
