package utils;

import gui.GUI;
import image.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BlurListener implements ActionListener{
	/*
	 * GUI object 
	 */
	protected GUI gui;
	
	/*
	 * Constructor
	 * 
	 * @param {GUI} gui - a reference to the existing GUI object
	 */
	public BlurListener (GUI gui){
		this.gui = gui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		 if(e != null && e.getActionCommand() != null && e.getActionCommand().equals("Set")) {
			gui.pushState();
		 
		 	int percValue = MyJDialog.slider.getValue() - 100;
         	// Convert from -100%-100% to -255->255
		 	int intensity = (255 * percValue) / 100; 
		 
	     	gui.filters.brigthness(gui.loader.currentImage, intensity);
	     	gui.updateDisplayedImage();
		 } else {		 
			// Value is changed from slider
			Image oldImage = gui.loader.currentImage.getCopy();
			 
		 	int percValue = MyJDialog.slider.getValue() - 100;
         	// Convert from -100%-100% to -255->255
		 	int intensity = (255 * percValue) / 100; 
		 
	     	gui.filters.brigthness(gui.loader.currentImage, intensity);
	     	gui.updateDisplayedImage();
	     	
	     	gui.loader.currentImage = oldImage;
		 }
	}

}
