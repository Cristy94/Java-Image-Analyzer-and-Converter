package image;

/**
 * Image class
 * 
 * Basic class for storing data about an image
 * -> Data - The byte array for this image
 * -> Name
 * -> Size - the approximate size of the image in bytes (W*H*4)
 * -> Width
 * -> Height
 * 
 * This will also include methods for basic alterations such as:
 * -> Crop
 * -> Scale
 */
public class Image {
	public byte[] pixels;
	public int width;
	public int height;
	public final int channels = 3; // This class only works with RGB images
	
	public Image(byte[] pixels, int width, int height) {
		this.pixels = pixels;
		this.height = height;
		this.width = width;
	}
	
	public Image getCopy() {
		byte[] newPixels = new byte[this.pixels.length];
		System.arraycopy( pixels, 0, newPixels, 0, pixels.length );
		return new Image(newPixels, width, height);
	}

}
