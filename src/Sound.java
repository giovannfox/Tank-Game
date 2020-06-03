package src;

import javax.sound.sampled.Clip;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;

public class Sound {
    private AudioInputStream soundStream;
    private int type;
    private Clip clip;

    public Sound(int type, String soundFile){
        this.type = type;
        try{
            soundStream = AudioSystem.getAudioInputStream(this.getClass().getClassLoader().getResource(soundFile));
            clip = AudioSystem.getClip();
            clip.open(soundStream);
        }
        catch(Exception e){
            System.out.println(e.getMessage() + "Audio fail");
        }
        if(this.type == 1){
            Runnable myRunnable = new Runnable(){
                @Override
                public void run(){
                    while(true){
                        clip.start();
                        clip.loop(clip.LOOP_CONTINUOUSLY);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException exception) {
                            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, exception);
                        }
                    }
                }
            };
            Thread thread = new Thread(myRunnable);
            thread.start();
        }
    }
    public Clip getClip() {
        return this.clip;
    }
    public void play(){
        clip.start();
    }

}
