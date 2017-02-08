package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum which holds various image formats.
 *
 */
public enum FormatExtensions {
 TIFF, YUV, PNG, BMP, JPEG, GIF;
 

 /**
  * Map which alows the search of an enum type based on its extension.
  */
  private static Map<String,FormatExtensions> formatMap = new HashMap<String,FormatExtensions>();
  static {
	  formatMap.put( "tiff", TIFF);
	  formatMap.put( "yuv", YUV);
	  formatMap.put( "png", PNG);
	  formatMap.put( "bmp", BMP);
	  formatMap.put( "jpg", JPEG);
	  formatMap.put( "gif", GIF);

  }
    
  /**
   * Method which fetches the enum type based on the given extension.
   * @param extension The extension for the format.
   * @return A {@link FormatExtentsion} which corresponds to the given extension. 
   */
    public static FormatExtensions getFormatOnXtension(String extension)
    {
        return formatMap.get(extension);
    }
}
