import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/* ---------------------------------------------------------
 *                       FLAGS
 * -w {width} (Width of image - will maintain aspect ratio)
 * -p {1-225} (Precision of color) 
 * -i (Invert color)
 * -fps {fps used in ffmpeg when making frames} (This allows our player to use the fps to figure out how fast to play the video)
 * ---------------------------------------------------------
 */
public class AsciiGenerator {
    public static void main(String[] args) throws IOException {
        int fileCount = new File("frames").listFiles().length; //Find out the amount of files in the folder

        String colorsStr = "\u00D1@#W$9876543210?!abc;:+=-,._ ";
        String inputImagePath = "frames/image-1.png"; //Default Image String

        boolean invertFrames = false; //By default the lighter a pixel is closer it is to the 0th index in colorStr
        int width = 50; // Set the desired new width for the resized image
        int fpsFFMPEG = 20;
        

        for (int i = 0; i < args.length; i++) { //Read flags
            switch(args[i]) {
                case "-w":
                    try {width = Integer.parseInt(args[i+1]);} catch (Exception e) {
                        System.out.println("Invalid -w param!");
                        System.out.println("-w is reserved for frame width.");
                        return;
                    }
                break;
                case "-p":
                    try {
                        int percision = Integer.parseInt(args[i+1]);
                        if (percision > 225 || percision < 1) throw new Exception();
                        for (int j = 0; j < percision; j++) colorsStr += " ";
                    } catch (Exception e) {
                        System.out.println("Invalid -p param!");
                        System.out.println("-p is reserved for color percision {1-225}.");
                        return;
                    }
                break;
                case "-i": invertFrames = !invertFrames;
                break;
                case "-fps":
                    try {fpsFFMPEG = Integer.parseInt(args[i+1]);} catch (Exception e) {
                        System.out.println("Invalid -fps param!");
                        System.out.println("-fps is reserved for the fps you used when creating the frames.");
                        return;
                    }
                break;
            }
        }

        char[] colors = colorsStr.toCharArray(); //Convert the colors string into a char array
        BufferedImage originalImage = ImageIO.read(new File("frames").listFiles()[0]); //Get and read the first file in frames

        // Calculate the new height to maintain the original aspect ratio (we can do this since all frames are from the same video, so all frames have the same aspect ratio)
        double aspectRatio = (double) originalImage.getWidth() / originalImage.getHeight();
        int height = (int) (width / aspectRatio);

        
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("AsciiVideo.txt"))); //Clear the current file
        pw.print("");
        pw.close();
        pw = new PrintWriter(new BufferedWriter(new FileWriter("AsciiVideo.txt", true)));
        pw.println(width+" "+height+" "+fpsFFMPEG); //Print width and height to beginning of the file

        for (int i = 1; i <= fileCount; i++) {
            inputImagePath = "frames/image-"+i+".png";
            // Read the input image
            originalImage = ImageIO.read(new File(inputImagePath));
            // Create a new BufferedImage with the new dimensions
            BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());

            // Perform the resizing using Graphics2d
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(originalImage, 0, 0, width, height, null);
            g2d.dispose();
                

            //int col = (int)(Math.random()*8+30 + (Math.random() > 0.5 ? 60 : 0));
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = resizedImage.getRGB(x, y);

                    int red = (pixel >> 16) & 0xFF;
                    int green = (pixel >> 8) & 0xFF;
                    int blue = pixel & 0xFF;
                    int gs = (red+green+blue)/3;
                        
                    //Colors length
                    int cLen = colors.length-1;
                    int pickAsciiChar = invertFrames ? (int)(gs/(255.0/cLen)) : cLen - (int)(gs/(255.0/cLen));
                    pw.print(colors[pickAsciiChar]);
                    pw.print(colors[pickAsciiChar]);
                }
                pw.println();
            }
            System.out.printf("Frame %d written\n", i);
        }
        System.out.println("Wrote to AsciiVideo.txt");
        pw.close();
    }
}