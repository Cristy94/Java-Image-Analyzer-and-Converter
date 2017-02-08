package image;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

/**
 * ImageLoader library class
 * 
 * This class will contain the methods needed to load and display
 * various image file formats
 */
public class ImageLoader {
	
	public Image currentImage = null;
	public Image originalImage = null;
	// Hold a reference to the buffered image from the gui
	public BufferedImage bufferedImage = null;
	public BufferedImage originalBufferedImage = null;

	public ImageLoader() {
		
	}
	
	/**
	 * 
	 * @param {BufferedImage} img - The image data to load
	 */
	public void loadBitmapImage(BufferedImage img) {
		// Convert all loaded images to basic BGR
		img = ImageConverter.convertColorspace(img, BufferedImage.TYPE_3BYTE_BGR);
		byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		currentImage = new Image(pixels, img.getWidth(), img.getHeight());
		bufferedImage = img;
		originalImage = new Image(pixels, img.getWidth(), img.getHeight());
		originalBufferedImage = deepCopy(img);
	}

	static BufferedImage deepCopy(BufferedImage bi) {
		 ColorModel cm = bi.getColorModel();
		 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		 WritableRaster raster = bi.copyData(null);
		 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
		}
	
	public void updateImage() {
		bufferedImage = new BufferedImage(currentImage.width, currentImage.height, BufferedImage.TYPE_3BYTE_BGR);

		// Copy current pixels array to the buffered image
		byte[] destPixels = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
		System.arraycopy(currentImage.pixels, 0, destPixels, 0, destPixels.length);
	}

}
