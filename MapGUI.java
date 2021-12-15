import java.awt.*;
import javax.swing.*;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * this class is responsible for displaying the map.
 * the map is a picture that this class is responsible
 * for accessing and displaying to the player.
 *
 * @author Syraj Alkhalil
 * @version 28.11.2021
 */
public class MapGUI extends JFrame {
    private JFrame frame;
    private JPanel map;

    /**
     * make the map object and set the size of the frame
     */
    public MapGUI(){
        buildFrame();
        this.setMapSize(750, 500);
    }
    
    /**
     * Set a fixed size for the map.
     */
    private void setMapSize(int width, int height)
    {
        map.setPreferredSize(new Dimension(width, height));
        frame.pack();
    }
    
    /**
     * return the map frame
     */
    public JFrame getFrame()
    {
        return frame;
    }

    /**
     * build the map frame.
     */
    private void buildFrame() {
        frame = new JFrame("Map");
        setDefaultCloseOperation(frame.DO_NOTHING_ON_CLOSE); // not needed, but I like to add it for completeness.

        try{
            final BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream("map.png")); // get the file relative to the class, so it works as .jar
            map = new JPanel() 
            {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(image, 0, 0, 750, 500, null);
                }
            };
            
            frame.add(map);
            frame.pack();
            // place the frame at the top right of the screen and show
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setLocation(d.width-750 , 0);
            frame.setVisible(true);

        }catch (IOException ioe){ // we catch the exception
            map = new JPanel();
            System.out.println("the map doesn't exist in the files :( sad.. I guess you have to play the game without a map.");
        }
    }
}