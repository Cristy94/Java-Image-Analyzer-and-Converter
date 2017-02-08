package utils;
import java.awt.Point;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ErrorDialog extends JDialog {

    private static final long serialVersionUID = 1L;
	public ErrorDialog(JFrame parent, String errorName) {
	        super(parent, "Error!");
	        // set the position of the window
	        Point p = new Point(200, 200);
	        setLocation(p.x, p.y);
	        // Create a message
	        JPanel panel = new JPanel();
	        panel.add(new JLabel(errorName));
        	getContentPane().add(panel);
	        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	        pack();
	        setVisible(true);
        	setSize(400, 70);
	    }
}

