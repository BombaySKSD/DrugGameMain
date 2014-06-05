package object;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    private Clip clip;
    public Sound(String fileName) {
        try {
            File file = new File(fileName);
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);
        }catch(Exception e){}
    }
    final public void play(){
        clip.setFramePosition(0);
        clip.start();
    }
    final public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    final public void stop(){
        clip.stop();
    }
    final public boolean playing(){
    	return clip.isRunning();
    }
}