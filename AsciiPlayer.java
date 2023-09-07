import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


/* ----------------------------------------------------------------------------
 *                                  FLAGS
 * -s {Frames per millisecond} (The lower the number the faster the video plays)
 * -wav {wav file} (Wav file that is used for sound)
 * ----------------------------------------------------------------------------
 */
public class AsciiPlayer {
    static Clip clip;
    public static void playSound(String path) {
        try {
            clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(AsciiPlayer.class.getResourceAsStream(path));
            clip.open(inputStream);
            clip.start(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {

        long speedMilli = 25; //Video playback speed (Amount of milliseconds between frames)
        String wavPath = "";


        BufferedReader br = new BufferedReader(new FileReader("AsciiVideo.txt"));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        int width = Integer.parseInt(st.nextToken()), height = Integer.parseInt(st.nextToken()), fps = Integer.parseInt(st.nextToken()); //width and height of frame and how many frames to play per second
        speedMilli = (int)(1000/fps - 0.00009*width*height); //Finds a decent speed for the video (0.00009 is value that seems to match how long it takes in milliseconds for the program to print a character)

        for (int i = 0; i < args.length; i++) { //Read flags
            switch(args[i]) {
                case "-s":
                    try {speedMilli = Long.parseLong(args[i+1]);} catch (Exception e) {
                        System.out.println("Invalid -s param!");
                        System.out.println("-s is reserved for playback speed (Lower number = faster playback)");
                        return;
                    }
                break;
                case "-wav":
                    try {wavPath = args[i+1];} catch (Exception e) {
                        System.out.println("Invalid -wav param!");
                        System.out.println("-wav is reserved for wav files");
                        return;
                    }
                break;
            }
        }

        

        if (wavPath != "") playSound(wavPath); //Play sound if a wavPath was set
        String line = ""; //This stores each line of the frame
        int count = 0; //Counter that increments till we reach the height of the frame to know when we are playing next frame

        while ((line = br.readLine()) != null) {
            System.out.println(line);
            count++;

            if (count == height) {
                count = 0;
                Thread.sleep(speedMilli);
                System.out.print("\033c");
            }
        }
        br.close();
        if (wavPath != "") clip.close();
    }
}