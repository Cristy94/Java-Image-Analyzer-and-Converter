package image;

/**
 * ImageRotate library class
 * 
 * This class will contain the methods needed to load rotate
 * and flip images
 */
public class ImageRotate {

	public void rotateCW(Image currentImage) {
		
		// Rotates the sprite 90 degrees clockwise.
		byte[] pixels = new byte[currentImage.pixels.length];
		int width = currentImage.width;
		int height = currentImage.height;
		int channels = currentImage.channels;
		
		// Store the index in the new array
		int newOffset = 0;
		
		for(int col = 0; col < width; ++col) 
			for(int row = height - 1; row >=0; --row){
			
				// Count the pixels rotate 90deg
				int srcOffset = (row * width + col) * channels;
				
				// Move all channels of the pixel at once
				for(int band = 0; band < channels; ++band) {
					pixels[newOffset + band]= currentImage.pixels[srcOffset + band];
				}
				
				// Increment the new index
				newOffset+= channels;
		}
		
		int w = width;
		width = height;
		height = w;
		
		currentImage.width = width;
		currentImage.height = height;
		
		//Replace current pixels with the new ones
		System.arraycopy(pixels, 0, currentImage.pixels, 0, pixels.length);
	}
	
	public void rotateCCW(Image currentImage) {
		
		for(int i=1; i<=3; i++){
			rotateCW(currentImage);
		}
	}

	public void flipHorizontal(Image currentImage) {
		
		byte[] pixels = new byte[currentImage.pixels.length];
		int width = currentImage.width;
		int height = currentImage.height;
		int channels = currentImage.channels;
				
		for(int row = 0; row < height; ++row)
			for(int col = 0; col < width; ++col) {
				int offset = (row * width + col) * channels;
				int newOffset = (row * width + (width - 1 - col)) * channels;
							
				for(int band = 0; band < channels; ++band) {
					pixels[newOffset + band] =  currentImage.pixels[offset + band];
				}
			}
		
		// Replace current pixels with the new ones
		System.arraycopy(pixels, 0, currentImage.pixels, 0, pixels.length);
	}

	public void flipVertical(Image currentImage) {
		for(int i=1; i<=2; i++){
			rotateCW(currentImage);
		}
		
		flipHorizontal(currentImage);
	}
}
