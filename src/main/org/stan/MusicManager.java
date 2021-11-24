/*From MakingStan
 Written on 24 november 2021.
 For polymars seajam:  https://itch.io/jam/seajam
*/

package main.org.stan;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicManager {
    public static Clip clip;
    public MusicManager() {
        try {
            AudioInputStream input = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource("music/music.wav"));
            clip = AudioSystem.getClip();
            clip.open(input);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("an audio error occured");
        }

    }
}
