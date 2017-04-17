package Som;

import java.awt.Component;
import java.io.File;
import static java.lang.System.in;
import javax.swing.JFileChooser;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


    /*
import java.io.*;
import javax.sound.sampled.*;

public class Som{

    public static void main(){

try {
    File yourFile = new File("C:\\Users\\nmore_000\\Downloads\\loop1.wav");
    AudioInputStream stream;
    AudioFormat format;
    DataLine.Info info;
    Clip clip;

    stream = AudioSystem.getAudioInputStream(yourFile);
    format = stream.getFormat();
    info = new DataLine.Info(Clip.class, format);
    clip = (Clip) AudioSystem.getLine(info);
    clip.open(stream);
    clip.start();
}
catch (Exception e) {
    //whatevers
     }
    }
}

import java.io.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
 

public class Som{
       
    
    private Clip clip;    
    private AudioInputStream audioIn;   
    
    public Som(){
        this.clip = null;
        this.audioIn = null;
    }
    
     public void playWav(File wav) {    
        try {               
            audioIn = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(wav)));                
            clip = AudioSystem.getClip();                
            clip.open(audioIn);    
            clip.start();    
            clip.loop(Clip.LOOP_CONTINUOUSLY);//comente essa linha caso não deseje um loop    
        } catch (Exception ex) {    
            ex.printStackTrace();    
        }    
    }    
    
    public void stopWav() {    
        try {    
            clip.stop();    
            audioIn.close();    
        } catch (Exception ex) {    
            ex.printStackTrace();    
        }    
    }

public static void main(String args[]){    
    Som s = new Som();
    s.playWav(new File("C:\\Users\\nmore_000\\Downloads\\loop1.wav"));  
    }

}    
/**
 * A simple Java sound file example (i.e., Java code to play a sound file).
 * AudioStream and AudioPlayer code comes from a javaworld.com example.
 * @author alvin alexander, devdaily.com.
 */
/* */
public class Som{

public Som() {   
        try{
          JFileChooser  openf = new JFileChooser();
          Component j = null;
          openf.showOpenDialog(j);
          File fl = openf.getSelectedFile();
          String st = fl.getAbsolutePath();
          AudioStream au = new AudioStream(in);
          AudioPlayer.player.start(au);
                    }
    catch(Exception e){}
}
    
    public static void main(String[] args){
        Som s = new Som();
    } 
      
}  /*Clip sound = (Clip) AudioSystem.getLine(new Line.Info(Clip.class));
      sound.open(AudioSystem.getAudioInputStream("D:\\08. I Am a River.wav));

    // specify the sound to play
    // (assuming the sound can be played by the audio system)
    File soundFile = new File("D:\\08. I Am a River.wav");
    AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);

    // load the sound into memory (a Clip)
    DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
    Clip clip = (Clip) AudioSystem.getLine(info);
    clip.open(sound);

    // due to bug in Java Sound, explicitly exit the VM when
    // the sound has stopped.
    clip.addLineListener(new LineListener() {
      public void update(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP) {
          event.getLine().close();
          System.exit(0);
        }
      }
    });

    // play the sound clip
    clip.start();*/

