package histogram;

import image.Image;
import image.ImageFilters;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GetHistogram extends JPanel {
    
    Dimension dimPic;
    Image picture;
    String colorReceived;
    int [] samples;
    
    public GetHistogram(Dimension d, String color, Image image) {
        //to dimension the picture
        dimPic = d;
        colorReceived = color;
        picture = image;

        getHistogram();       
    }
       
    public void getHistogram() {
        samples = new int[256];
        int maxNumSamples = 0;
        
        int hasAlpha = picture.channels == 4 ? 1 : 0;
        
        for(int i = 0; i < picture.pixels.length; i+= picture.channels) {
                
				int ired = (int)ImageFilters.toUnsigned(picture.pixels[i + 2 + hasAlpha]);
				int igreen = (int)ImageFilters.toUnsigned(picture.pixels[i + 1 + hasAlpha]);
				int iblue = (int)ImageFilters.toUnsigned(picture.pixels[i + hasAlpha]);
                
                if(colorReceived.equals("red"))
                {
                   samples[ired]++; 
                   if(samples[ired] > maxNumSamples)
                   {
                       maxNumSamples = samples[ired];
                   }
                   
                }
                else if(colorReceived.equals("green"))
                {
                   samples[igreen]++; 
                   if(samples[igreen] > maxNumSamples)
                   {
                       maxNumSamples = samples[igreen];
                   }
                }
                else if(colorReceived.equals("blue"))
                {
                    samples[iblue]++; 
                   if(samples[iblue] > maxNumSamples)
                   {
                       maxNumSamples = samples[iblue];
                   }
                }            
            } 

        // Normalizing                   
        for(int i=0; i < 255; i++) {
            samples[i] = (int)((samples[i]*200)/(maxNumSamples));
        }      
    }
    
    public void paintComponent(Graphics g)
    {
        if(colorReceived.equals("red"))
        {
            g.setColor(Color.RED);
        }
        else if (colorReceived.equals("green"))
        {
            g.setColor(Color.GREEN);
        }
        else if (colorReceived.equals("blue"))
        {
            g.setColor(Color.BLUE);
        }
        
        for(int i=0; i < 255; i++)
        {
            g.drawLine(i, 0,  i, samples[i]);
        }    
    }
}
