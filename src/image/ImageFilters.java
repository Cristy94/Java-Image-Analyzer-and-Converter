package image;




/**
 * ImageFilters library class
 * 
 * This class will contain methos to apply various filters on images.
 * Supported filters:
 * 	-> grayscale
 *  -> blur
 *  -> sharpen
 *  -> sepia
 *  -> invert
 *  -> brightness
 *  -> contrast
 *  
 *  Filters would most likely be implemented using kernel/convultion matrix
 */
public class ImageFilters {
	
	/**
	 * Auxiliarry class to get the unsigned value of a byte
	 * @param {byte} b
	 * @return {int}
	 */
	public static int toUnsigned(byte b) {
	    return (int) (b >= 0 ? b : (256 + b));
	}
	
	/**
	 * Desaturate
	 * 
	 * @param {Image} currentImage
	 */
	public void desaturate(Image currentImage) {
			byte[] pixels = currentImage.pixels;
			int bands = currentImage.channels;
			int hasAlpha = 0;
			
			// If it has 4 bands, we assume it has alpha
			if(bands > 3) {
				hasAlpha = 1;
			}
				
			for(int i = 0; i < pixels.length; i+=bands) {
				
				// If it has alpha RGB elements are displaced by one in ARGB format
				int r = toUnsigned(pixels[i + 2 + hasAlpha]);
				int g = toUnsigned(pixels[i + 1 + hasAlpha]);
				int b = toUnsigned(pixels[i + hasAlpha]);
				int gray = (int)(r * 0.299 + g  * 0.587 + b * 0.114);
				
				// Apply average
				r = gray;
				g = gray;
				b = gray;
												
				pixels[i + 2 + hasAlpha] = (byte)r;
				pixels[i + 1 + hasAlpha] = (byte)g;
				pixels[i + hasAlpha] = (byte)b;						
			}
	}
	
	/**
	 * Sepia
	 * 
	 * @param {Image} currentImage
	 */
	public void sepia(Image currentImage) {

		byte[] pixels = currentImage.pixels;

		for(int i = 0; i < pixels.length; i+=3) {
			
			// If it has alpha RGB elements are displaced by one in ARGB format
			int r = toUnsigned(pixels[i + 2]);
			int g = toUnsigned(pixels[i + 1 ]);
			int b = toUnsigned(pixels[i]);
											
			pixels[i + 2] = (byte)Math.min((.393 * r) + (.769 * g) + (.189 * b), 255.0); // red
			pixels[i + 1] = (byte)Math.min((.349 * r) + (.686 * g) + (.168 * b), 255.0); // green
			pixels[i] = (byte)Math.min((.272 * r) + (.534 * g) + (.131 * b), 255.0); // blue					
		}

	}
	
	/**
	 * Increase/decrease image brightness
	 * 
	 * @param {Image} currentImage
	 * @param {int} intensity [0-255]
	 */
	public void brigthness(Image currentImage, int intensity) {

		byte[] pixels = currentImage.pixels;
		
		for(int i = 0; i < pixels.length; i++) {
			
			int val = toUnsigned(pixels[i]);
			
			pixels[i] = (byte)Math.min(255, Math.max(0, val + intensity)); 
		}
	}
	
	
	/**
	 * Increase/decrease image contrast
	 * 
	 * @param {Image} currentImage
	 * @param {int} intensity [-100, 100]
	 */
	public void contrast(Image currentImage, int intensity) {

		byte[] pixels = currentImage.pixels;
		
		double c =  (100 + intensity) / 100.0;
		c = c * c;
		
		for(int i = 0; i < pixels.length; i++) {
			
			int val = toUnsigned(pixels[i]);
			
			double newVal = ((val / 255.0 - 0.5) * c + 0.5) * 255;
			
			pixels[i] = (byte)Math.min(255, Math.max(0, newVal)); 
		}
	}
	
	
	/**
	 * Blur using convolution matrix
	 * 
	 * @param {Image} currentImage
	 */
	public void blur(Image currnetImage) {
		double [][] blurKernel = { 
			{1.0/16,  2.0/16, 1.0/16},
			{2.0/16,  4.0/16, 2.0/16},
			{1.0/16,  2.0/16, 1.0/16},			
		};
		
		applyKernel(currnetImage, blurKernel);		
	}

	/**
	 * Sharpen using convolution matrix
	 * 
	 * @param {Image} currentImage
	 */
	public void sharpen(Image currnetImage) {
		double [][] sharpenKernel = { 
			{0, -1, 0},
			{-1, 5, -1},
			{0, -1, 0}		
		};
		
		applyKernel(currnetImage, sharpenKernel);		
	}
	
	/**
	 * Applies a kernel on an image. Automatically updates the referenced image pixels.
	 * 
	 * @param currentImage - The image to apply the convolution filter on
	 * @param kernel - A matrix of dimensions NxN, where N is odd
	 */
	public void applyKernel(Image currentImage, double [][] kernel) {
		int kernelHeight = kernel.length;
		int kernelWidth = kernel[0].length;
		int kernelHfHeight = kernelHeight / 2;
		int kernelHfWidth = kernelWidth / 2;
		
		int width = currentImage.width;
		int height = currentImage.height;
				
		// Create a pixel array to store the updated image
		byte[] pixels = new byte[currentImage.pixels.length];
		
		for(int row = 0; row < height; ++row)
			for(int col = 0; col < width; ++col) {
				// We are currently changing the pixel at position [row][col]
				
				double sumR = 0;
				double sumG = 0;
				double sumB = 0;
												
				for(int kr = 0; kr < kernelHeight; ++kr) 
					for(int kc = 0; kc < kernelWidth; ++kc) {
						// Find the offset of the current pixel from the center of the kernel matrix
						int offsetRow = kr - kernelHfHeight;
						int offsetCol = kc - kernelHfWidth;
						
						// So, when we want to change X,Y pixel
						// We first apply kernel[0][0], which means that we have to take in account
						// The pixel at X + xOffset, y + yOffset 
						
						// Find pixel from image that overlaps the kernel pixel position
						int sourcePixelRow = row + offsetRow;
						int sourcePixelCol = col + offsetCol;
						
						// Keep pixel position in range
						sourcePixelRow = Math.min(height - 1, Math.max(0, sourcePixelRow));
						sourcePixelCol = Math.min(width - 1, Math.max(0, sourcePixelCol));
					
						// Get the pixel position in the 1D array
						int pixelOffset = (sourcePixelRow * width + sourcePixelCol) * 3;
						
						// Add pixel value to the computed sum
						int r = toUnsigned(currentImage.pixels[pixelOffset + 2]);
						int g = toUnsigned(currentImage.pixels[pixelOffset + 1]);
						int b = toUnsigned(currentImage.pixels[pixelOffset]);
						
						sumR += r * kernel[kr][kc];
						sumG += g * kernel[kr][kc];
						sumB += b * kernel[kr][kc];
						
					}
				

				// Get the position in the 1D byte array
				int offset = (row * width + col) * 3;
				
				// Keep colors in [0-255]
				sumR = Math.min(255, Math.max(0, sumR));
				sumG = Math.min(255, Math.max(0, sumG));
				sumB = Math.min(255, Math.max(0, sumB));
				
				// Update the pixel
				pixels[offset + 2] = (byte)sumR; 
				pixels[offset + 1] = (byte)sumG;
				pixels[offset] = (byte)sumB; 
			}
		
		// Replace current pixels with the new ones
		System.arraycopy(pixels, 0, currentImage.pixels, 0, pixels.length);
	}
}
