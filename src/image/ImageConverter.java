package image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.github.jaiimageio.plugins.tiff.TIFFImageWriteParam;

/**
 * ImageConverter library class
 * <p/>
 * This class will contain the methods needed to convert a file's format.
 */
public class ImageConverter {
  
	/**
	 * Method used to save the given image in TIFF format.
	 * @param filename The name of the file with which the image is going to be saved.
	 * @param image The image that will be saved to the given file.
	 */
    final public static void saveTiff(String filename, BufferedImage image) {
		
		File tiffFile = new File(filename);
		ImageOutputStream ios = null;
		ImageWriter writer = null;
		
		try {
			
			Iterator<?> it = ImageIO.getImageWritersByFormatName("TIF");
			if (it.hasNext()) {
				writer = (ImageWriter)it.next();	
			}
			
			ios = ImageIO.createImageOutputStream(tiffFile);
			writer.setOutput(ios);
			TIFFImageWriteParam writeParam = new TIFFImageWriteParam(Locale.ENGLISH);
			writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			writeParam.setCompressionType("PackBits"); 
			
			IIOImage iioImage = new IIOImage(image, null, null);
			writer.write(null, iioImage, writeParam);
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
	
    /**
     * Method used to convert the given RGB to YUV.
     * @param aRGB The RGB that will be converted.
     * @param width The width of the picture.
     * @param height The height of the picture.
     * @return An array of bytes in YUV color space.
     */
    final public static byte[] colorconvertRGBtoYUV(int[] aRGB, int width, int height) {
        final int frameSize = width * height;
        final int chromasize = frameSize / 4;
       
        int yIndex = 0;
        int uIndex = frameSize;
        int vIndex = frameSize + chromasize;
        byte [] yuv = new byte[width*height*3/2];
       
        int a, R, G, B, Y, U, V;
        int index = 0;
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {

                R = (aRGB[index] & 0xff0000) >> 16;
                G = (aRGB[index] & 0xff00) >> 8;
                B = (aRGB[index] & 0xff) >> 0;

                Y = ((66 * R + 129 * G +  25 * B + 128) >> 8) +  16;
                U = (( -38 * R -  74 * G + 112 * B + 128) >> 8) + 128;
                V = (( 112 * R -  94 * G -  18 * B + 128) >> 8) + 128;

                yuv[yIndex++] = (byte) ((Y < 0) ? 0 : ((Y > 255) ? 255 : Y));
               
                if (j % 2 == 0 && index % 2 == 0)
                {
                    yuv[uIndex++] = (byte)((U < 0) ? 0 : ((U > 255) ? 255 : U));
                    yuv[vIndex++] = (byte)((V < 0) ? 0 : ((V > 255) ? 255 : V));
                }

                index ++;
            }
        }       
        return yuv;
    } 
   
    /**
     * convert a BufferedImage to RGB colourspace
     */
    final public static BufferedImage convertColorspace(BufferedImage src, int newType) {		
    	try {
    		BufferedImage img = new BufferedImage(src.getWidth(), src.getHeight(), newType);
    		Graphics g = img.getGraphics();
    		g.drawImage(src, 0, 0, null);
    		g.dispose();
    		return img;
    	} catch (Exception e) {
    		System.out.println("Exception " + e + " converting image");

    	}

    	return src;
    }
}
