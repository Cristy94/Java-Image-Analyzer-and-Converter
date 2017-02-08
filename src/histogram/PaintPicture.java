package histogram;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PaintPicture extends JPanel {
    
    Dimension dimFrame;
    BufferedImage picture;
    
    public PaintPicture(Dimension d, BufferedImage pic)
    {
        dimFrame = d;
        picture = pic;
    }
       
    public void paintComponent(Graphics g)
    {

        super.paintComponent(g);
        g.drawImage(picture, 0, 0,dimFrame.height, dimFrame.width, this);
                              
    }
    
}
