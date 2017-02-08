package image;

import histogram.GetHistogram;
import histogram.PaintPicture;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

/**
 * ImageAnalyzer library class
 * 
 * This will be used to display various info about an image, such as:
 * -> RGB histogram
 */
@SuppressWarnings("serial")
public class ImageAnalyzer extends JFrame{

    public ImageAnalyzer()
    {
        super("Generating Histogram");
        
        //setting from the Frame
        setLayout(new GridLayout(2,2));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800,800);
        setVisible(true);     
    }
    
    public void generateHistogram(BufferedImage bufferedImage, Image image)
    {
    	//adding the picture
        Dimension d= getSize();
        d.height = d.height/2;
        d.width = d.width/2;
        PaintPicture myPic = new PaintPicture(d, bufferedImage);
             
        //PaintPicture myPic2 = new PaintPicture(d);
        GetHistogram myPic2 = new GetHistogram(d, "red", image);
        GetHistogram myPic3 = new GetHistogram(d, "green", image);
        GetHistogram myPic4 = new GetHistogram(d, "blue", image);
        
        add(myPic);
        
        add(myPic2);
        
        add(myPic3);
        
        add(myPic4);             
    }
}
