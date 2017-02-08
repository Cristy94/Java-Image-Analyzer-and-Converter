package utils;

import gui.GUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/*
 * RadioButtonListener class that extends JFrame and implements ActionListener
 * It is used for the "Save As" option, the frame providing multiple extension possibilities
 * 
 * @author Veronica Dan
 */
public class RadioButtonListener extends JFrame implements ActionListener{

	/*
	 * GUI object
	 */
	public GUI gui;
	
	/*
	 * Constructor
	 */
	public RadioButtonListener(GUI gui){
		 
		this.gui = gui;
		
		/*Frame initialisation*/
		setSize(250,250);
		setTitle("Save As");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*Radio button group initialisation*/
		JRadioButton jpeg = new JRadioButton("jpeg");
        jpeg.setActionCommand("jpeg");
        jpeg.setSelected(true);
        JRadioButton bmp = new JRadioButton("bmp");
        bmp.setActionCommand("bmp");
        bmp.setSelected(false);
        JRadioButton png = new JRadioButton("png");
        png.setActionCommand("png");
        png.setSelected(false);
        JRadioButton tiff = new JRadioButton("tiff");
        tiff.setActionCommand("tiff");
        tiff.setSelected(false);
        JRadioButton yuv = new JRadioButton("yuv");
        yuv.setActionCommand("yuv");
        yuv.setSelected(false);
        
        /*Setting the action listener to this*/
        jpeg.addActionListener(this);
        png.addActionListener(this);
        bmp.addActionListener(this);
        tiff.addActionListener(this);
        yuv.addActionListener(this);
        
        /*Radio button group initialisation*/
        ButtonGroup group = new ButtonGroup();
        group.add(jpeg);
        group.add(png);
        group.add(bmp);
        group.add(tiff);
        group.add(yuv);        
         
        /*JPanel initialisation*/
        JPanel radioPanel = new JPanel(new GridLayout(0, 1));
        radioPanel.add(jpeg);
        radioPanel.add(png);
        radioPanel.add(bmp);
        radioPanel.add(tiff);
        radioPanel.add(yuv);

        /*Add all the elements to the frame*/
        add(radioPanel);
        setVisible(true);
	}
	
	/*
	 * Overriden actionPerformed method
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		/*Save the selection as the desired file extension*/ 
		gui.extensionSave = "." + e.getActionCommand();
		
		/*Open a file chooser to get the path*/
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(gui);
		if (result == JFileChooser.APPROVE_OPTION) {
			try {
				/*Save the image to the path selected and with the selected extension*/
	            File outputfile = new File(fileChooser.getSelectedFile().getAbsolutePath());
                
                ImageIO.write(this.gui.loader.bufferedImage, this.gui.extensionSave, outputfile);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
		}
	}

}
