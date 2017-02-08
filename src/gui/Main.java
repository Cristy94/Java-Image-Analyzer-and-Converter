package gui;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main class for IAC
 * 
 * Main entry point for the IAC (Image Analyzer and Converter) application  
 */
public class Main {

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			@SuppressWarnings("unused")
			GUI gui = new GUI("Image Analyzer and Converter", 800,600);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}