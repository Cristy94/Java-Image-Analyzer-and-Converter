package utils;

import gui.GUI;
import image.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContrastListener implements ActionListener{
	/*
	 * GUI object 
	 */
	protected GUI gui;
	
	/*
	 * Constructor
	 * 
	 * @param {GUI} gui - a reference to the existing GUI object
	 */
	public ContrastListener (GUI gui){
		this.gui = gui;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		 if(e != null && e.getActionCommand() != null && e.getActionCommand().equals("Set")) {
			 gui.pushState();
			 
			 int intensity = MyJDialog.slider.getValue() - 100;
			 
		     gui.filters.contrast(gui.loader.currentImage, intensity);
	         gui.updateDisplayedImage();
		 } else {
			 // Value is changed from slider
			 Image oldImage = gui.loader.currentImage.getCopy();
			 
			 int intensity = MyJDialog.slider.getValue() - 100;
			 
		     gui.filters.contrast(gui.loader.currentImage, intensity);
	         gui.updateDisplayedImage();
	     	 gui.loader.currentImage = oldImage;
		 }
	}

}
