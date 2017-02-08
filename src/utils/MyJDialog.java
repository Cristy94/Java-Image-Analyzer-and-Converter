package utils;
import gui.GUI;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MyJDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    public static JSlider slider;
    private JButton button;
    private GUI gui;
    
	public MyJDialog(final GUI gui, JFrame parent, String title, ActionListener setEvent) {
	        super(parent, title);
	        
	        this.gui = gui;
	        
	        // set the position of the window
	        Point p = new Point(200, 200);
	        setLocation(p.x, p.y);
	        slider = new JSlider(0, 200, 100);
	        slider.setSize(300, 50);
	        
	        slider.setMajorTickSpacing(10);
	        slider.setPaintTicks(true);
	        
	        // Create the label table
			Hashtable labelTable = new Hashtable();
			labelTable.put( new Integer(0), new JLabel("-100%") );
			labelTable.put( new Integer(100), new JLabel("0%") );
			labelTable.put( new Integer(200), new JLabel("100%") );
			slider.setLabelTable( labelTable );

	        slider.setPaintLabels(true);
	        
	        getContentPane().add(slider);
	        
	        // get content pane, which is usually the
	        // Container of all the dialog's components.
	        // Create a button
	        JPanel buttonPane = new JPanel();
	        button = new JButton("Set");
	        button.setActionCommand("Set");
	        buttonPane.add(button);
	        
	        // set action listener on the button
	        button.addActionListener(setEvent);
	        
	        slider.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					button.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));		
				}
			});
	        
	        getContentPane().add(buttonPane, BorderLayout.PAGE_END);
	        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	        pack();
	        setVisible(true);
	        
	        // Update image if dialog is closed without setting
	        this.addWindowListener(new WindowListener() {
				
				@Override
				public void windowOpened(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowIconified(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowDeiconified(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowDeactivated(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowClosing(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowClosed(WindowEvent e) {
					gui.updateDisplayedImage();				
				}
				
				@Override
				public void windowActivated(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
			});

	    }
}
