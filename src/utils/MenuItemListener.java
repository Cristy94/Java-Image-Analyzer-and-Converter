package utils;

import gui.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * MenuItemListener class that implements ActionListener
 * It is used for every menu item and has it's actionPerformed method overriden for each of them
 * 
 * @author Veronica Dan
 */
public class MenuItemListener implements ActionListener{
	/*
	 * GUI object 
	 */
	protected GUI gui;
	
	/*
	 * Constructor
	 * 
	 * @param {GUI} gui - a reference to the existing GUI object
	 */
	public MenuItemListener(GUI gui){
		this.gui = gui;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}

}
