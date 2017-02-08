package gui;

import image.Image;
import image.ImageAnalyzer;
import image.ImageConverter;
import image.ImageFilters;
import image.ImageLoader;
import image.ImageRotate;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import utils.BlurListener;
import utils.ContrastListener;
import utils.ErrorDialog;
import utils.FileUtil;
import utils.FormatExtensions;
import utils.MenuItemListener;
import utils.MyJDialog;

/**
 * GUI class for IAC
 * <p/>
 * This is used to both graphically display the user interface and make
 * the link between the UI actions and class method calls
 *
 * @author Veronica Dan
 */
public class GUI extends JFrame {
    /*
     * JPanel object
     */
    private JPanel panel;
    
    // Stack to store states. !@WARNING: This should have a memory limit.
    Stack<Image> lastStates = null;
    
    /*
     * String useful elements that concern the saving functions
     */
    public String pathOpen, extensionOpen, extensionSave;
    
    /*
     * The image that will be loaded
     */
    public BufferedImage bufferedImage = null;
    
    /*    
     * Menu elements
     */
    private JCheckBox showImage;
    private JMenuBar menuBar;
    private JMenu fileMenu, imageMenu, filterMenu, rotateMenu, undoMenu;
    private JMenuItem open, save, quit;
    private JMenuItem adjustB, adjustC, rotateCW, rotateCCW, flipHor, flipVer;
    private JMenuItem bw, sepia, getHistogram, blur, sharpen;
    private JMenuItem restore;
    //to do for second prototype: open original image;
    
    JLabel imageContainer;
    JLabel originalImageContainer;

    /*
     * Helper classes for manipulating the image
     */
    public ImageLoader loader;
    public ImageFilters filters;
    public ImageRotate rotate;
    
    /*
     * Listeners
     */
    
    private BlurListener blurListener;
    private ContrastListener contrastListener;
    
    /*
     * Constructor
     *
     * @param {String} title - Window title
     * @param {int} width - The width of the window
     * @param {int} height - The height of the window
     */
    public GUI(String title, int width, int height) {
        super(title);

        lastStates = new Stack<Image>();
        // Setting up the frame and the panel
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setResizable(false);
        this.panel = new JPanel();
        setLocationRelativeTo(null);
        // Instantiate helper classes
        loader = new ImageLoader();
        filters = new ImageFilters();
        rotate = new ImageRotate();
        
        // Setting up the menu
        SetUpMenu();

        // Add elements to the JFrame
        panel.add(menuBar);
        
        // Add the place to store the image
        imageContainer = new JLabel();
        originalImageContainer = new JLabel();
        panel.add(imageContainer);

        this.add(panel);
        this.setIconImage(new ImageIcon("Frame_landscape_16.png").getImage());
        setVisible(true);
        
        // Create the listeners
        blurListener = new BlurListener(this);
        contrastListener = new ContrastListener(this);
    }

    // Updates the BufferedImage and ImageIcon container
    public void updateDisplayedImage() {
        loader.updateImage();
        
        imageContainer.setIcon(new ImageIcon(loader.bufferedImage));
        if (showImage.isSelected())
        	originalImageContainer.setIcon(new ImageIcon(loader.originalBufferedImage));
        revalidate();
        repaint();
    }

    // Saves a new stated to the history
    public void pushState() {
    	lastStates.push(loader.currentImage.getCopy());
    }
    
    // Resize window and fit the image
    private void fitWindow(boolean showOriginal){
    	// Window fits image, min size is 400 x 400, max size is screen size
		int width = Math.max(400, loader.currentImage.width + 80);
    	int height = Math.max(400, loader.currentImage.height + 80);
    	
    	if (showOriginal){
    		width += loader.originalImage.width;
    		height += loader.originalImage.height;
    	}
    	
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	double screenWidth = screenSize.getWidth();
    	double screenHeight = screenSize.getHeight();
    	
    	setSize((int)Math.min(width, screenWidth), (int)Math.min(height, screenHeight));
    	menuBar.setPreferredSize(new Dimension(this.getWidth() - 20, 20));
        repaint();
    }

    /*
     * Method that creates the menu bar and every element in it
     */
    public void SetUpMenu() {
        
    	/*Create the menuBar*/
        menuBar = new JMenuBar();

        menuBar.setPreferredSize(new Dimension(this.getWidth() - 20, 20));
        
        // Add resize event
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
            	JFrame frame = (JFrame)e.getSource();
            	menuBar.setPreferredSize(new Dimension(frame.getWidth() - 20, 20));
            }
        });
        
        // Also add move event because of JDK7 bug
        this.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent e) {
            	JFrame frame = (JFrame)e.getSource();
            	menuBar.setPreferredSize(new Dimension(frame.getWidth() - 20, 20));
            }
        });

	     /*Create the menus*/
        fileMenu = new JMenu("File");
        fileMenu.setPreferredSize(new Dimension(70, fileMenu.getPreferredSize().height));
        imageMenu = new JMenu("Image");
        imageMenu.setPreferredSize(new Dimension(70, imageMenu.getPreferredSize().height));
        filterMenu = new JMenu("Filter");
        filterMenu.setPreferredSize(new Dimension(70, filterMenu.getPreferredSize().height));
        rotateMenu = new JMenu("Rotate");
        rotateMenu.setPreferredSize(new Dimension(70, rotateMenu.getPreferredSize().height));
        undoMenu = new JMenu("Undo");
        undoMenu.setPreferredSize(new Dimension(70, undoMenu.getPreferredSize().height));
        
        /*Create checkbox for original image*/
        showImage = new JCheckBox("Show original image");
        
        /*Create the menu items and assign the its customised MenuItemListener*/
        fileMenu();
        imageMenu();
        rotateMenu();
        filtersMenu();
        undoMenu();
        initCheckBox();
        
        /*Add key binds*/
        KeyStroke ctrlO = KeyStroke.getKeyStroke("control O");
        open.setAccelerator(ctrlO);
        KeyStroke ctrlS = KeyStroke.getKeyStroke("control S");
        save.setAccelerator(ctrlS);
        KeyStroke ctrlQ = KeyStroke.getKeyStroke("control Q");
        quit.setAccelerator(ctrlQ);
        KeyStroke ctrlZ = KeyStroke.getKeyStroke("control Z");
        restore.setAccelerator(ctrlZ);
               
        /*Add all the appropriate elements to fileMenu*/
        fileMenu.add(open);
        fileMenu.add(save);
        fileMenu.add(quit);
	      
	    /*Add all the appropriate elements to imageMenu*/
	    imageMenu.add(getHistogram);
        
        /*Add all the appropriate elements to filterMenu*/
        filterMenu.add(adjustB);
        filterMenu.add(adjustC);
        filterMenu.add(bw);
        filterMenu.add(sepia);
        filterMenu.add(blur);
        filterMenu.add(sharpen);
        
        /*Add all the appropriate elements to rotateMenu*/
        rotateMenu.add(rotateCW);
        rotateMenu.add(rotateCCW);
        rotateMenu.add(flipHor);
        rotateMenu.add(flipVer);
        
        /*Add all the  appropriate elements to undoMenu.*/
        undoMenu.add(restore);
        
        
	    /*Add all the menus to the menuBar*/
        menuBar.add(fileMenu);
        menuBar.add(imageMenu);
        menuBar.add(filterMenu);
        menuBar.add(rotateMenu);
        menuBar.add(undoMenu); 
        menuBar.add(showImage);
    }
    
    private void initCheckBox(){
    	showImage.setEnabled(false);
    	showImage.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED){
					try{
						panel.add(originalImageContainer);
						originalImageContainer.setIcon(new ImageIcon(loader.originalBufferedImage));
					}catch (Exception ex){
						ErrorDialog errorDialog = new ErrorDialog(new JFrame(), "Error while adding the image!");
					}
				}else{
					try{
						panel.remove(originalImageContainer);
						originalImageContainer.setIcon(null);
					}catch (Exception ex){
						ErrorDialog errorDialog = new ErrorDialog(new JFrame(), "Error while removing the image!");
					}
				}
				revalidate();
				fitWindow(showImage.isSelected());
			}
        });
    }
    
    private void fileMenu(){
    	
    	open = new JMenuItem("Open");
        open.setIcon(new ImageIcon("Open_Archive_16.png"));
        open.addActionListener(new MenuItemListener(this) {

            public void actionPerformed(ActionEvent e) {
		    	
            	/*Open a file chooser to get the desired image's path*/
                JFileChooser fileChooser = new JFileChooser();
                configurFileChooser(fileChooser);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(gui);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    
		       		/*Save the path of the file to be opened to the gui object*/
                    this.gui.pathOpen = selectedFile.getAbsolutePath();
                }

                try {
                    // Read the image from the selected path
                    this.gui.bufferedImage = ImageIO.read(new File(this.gui.pathOpen));

                    // Convert the buffered image to bitmap data
                    this.gui.loader.loadBitmapImage(this.gui.bufferedImage);
                    
                    // Enable the radio button to show the original image
                    showImage.setEnabled(true);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(gui, "File not found.");
                }
		     
		        // Add the image to a JLabel and then to the panel
                updateDisplayedImage();
		    	    
                // Set the image's extension to the gui object
                this.gui.extensionOpen = FileUtil.getExtension(pathOpen);

                fitWindow(showImage.isSelected());
            }
        });

        save = new JMenuItem("Save");
        save.setIcon(new ImageIcon("Save_Disk_16.png"));
        save.addActionListener(new MenuItemListener(this) {

            public void actionPerformed(ActionEvent e) {
	    		 /*Open a file chooser to get the path*/
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                fileChooser.setAcceptAllFileFilterUsed(false);

        		fileChooser.setFileFilter(new FileNameExtensionFilter("YUV", "yuv"));
                configurFileChooser(fileChooser);

                int result = fileChooser.showSaveDialog(gui);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                    	
		    			/*Save the image to the path selected and with the same extension it had before*/
                        FileNameExtensionFilter filter = (FileNameExtensionFilter) fileChooser.getFileFilter();

                        String fileName = fileChooser.getSelectedFile().getAbsolutePath() + '.' + filter.getExtensions()[0];
                        File newName = new File(fileName);
                        
                        loader.updateImage();
                        switch(FormatExtensions.getFormatOnXtension(filter.getExtensions()[0])) {
                        case TIFF:
                        	ImageConverter.saveTiff(fileName, loader.bufferedImage);
                        	break;
                        case YUV:
                        	  int w = loader.bufferedImage.getWidth();
                              int h = loader.bufferedImage.getHeight();
                              int[] rgb = loader.bufferedImage.getRGB(0, 0, w, h, null, 0, w);
                             byte[] yuv =  ImageConverter.colorconvertRGBtoYUV(rgb, w, h);
                             
                             FileOutputStream fos = new FileOutputStream(fileName);
                             fos.write(yuv);
                             fos.close();
                             
                        	break;
                        default:
                        	 ImageIO.write(loader.bufferedImage, filter.getExtensions()[0], newName);
                        	break;
                        }
                        
                       
                    } catch (Exception ex) {
                        ErrorDialog errorDialog = new ErrorDialog(new JFrame(), "Error!");
                    }
                }

            }

        });

      
        quit = new JMenuItem("Quit");
        quit.setIcon(new ImageIcon("Emergency_exit_16.png"));
        quit.addActionListener(new MenuItemListener(this) {

            public void actionPerformed(ActionEvent e) {
	            	 /*Dispose the active frame*/
                this.gui.dispose();
            }

        });

    }
    private void imageMenu(){
       
    	getHistogram = new JMenuItem("Image histogram");
    	getHistogram.setIcon(new ImageIcon("Bar_graph_16.png"));
        getHistogram.addActionListener(new MenuItemListener(this) {

            public void actionPerformed(ActionEvent e) {
                try {
                    ImageAnalyzer histogram = new ImageAnalyzer();
                    histogram.generateHistogram(this.gui.bufferedImage, loader.currentImage);
                } catch (Exception ex) {
                    ErrorDialog errorDialog = new ErrorDialog(new JFrame(), "Error!");
                }
            }
        });	
    
        
    }
    private void filtersMenu(){
    	
    	final GUI self = this;
    	
 		adjustB = new JMenuItem("Adjust Brightenss");
 		adjustB.setIcon(new ImageIcon("Bright_lightbulb_16.png"));
 		adjustB.addActionListener(new MenuItemListener(this) {
 		
 			public void actionPerformed(ActionEvent e) {
 					MyJDialog dialog = new MyJDialog(self, new JFrame(), "Adjust Brightenss", blurListener);
 					dialog.setSize(300, 150);
 				}
 			});

		adjustC = new JMenuItem("Adjust Contrast");
 		adjustC.setIcon(new ImageIcon("Contrast_icon_16.png"));
		adjustC.addActionListener(new MenuItemListener(this) {
		
			public void actionPerformed(ActionEvent e) {
					MyJDialog dialog = new MyJDialog(self, new JFrame(), "Adjust Contrast", contrastListener);
					dialog.setSize(300, 150);
				}
			});
	
    	bw = new JMenuItem("Black and White");
 		bw.setIcon(new ImageIcon("Paint_can_16.png"));
        bw.addActionListener(new MenuItemListener(this) {
            
        	public void actionPerformed(ActionEvent e) {
                try {
                	pushState();
                    filters.desaturate(loader.currentImage);                    
                    updateDisplayedImage();
                } catch (Exception ex) {
                    ErrorDialog errorDialog = new ErrorDialog(new JFrame(), "Error!");
                }
            }

        });
       
        sepia = new JMenuItem("Sepia");
 		sepia.setIcon(new ImageIcon("Paint_can_16.png"));
        sepia.addActionListener(new MenuItemListener(this) {
            
        	public void actionPerformed(ActionEvent e) {
                try {
                	pushState();
                    filters.sepia(loader.currentImage);
                    updateDisplayedImage();
                } catch (Exception ex) {
                    ErrorDialog errorDialog = new ErrorDialog(new JFrame(), "Error!");
                }
            }

        });
    
        blur = new JMenuItem("Blur");
 		blur.setIcon(new ImageIcon("Drop_silhouette_16.png"));
        blur.addActionListener(new MenuItemListener(this) {
            
        	public void actionPerformed(ActionEvent e) {
                try {
                	pushState();
                    filters.blur(loader.currentImage);
                    updateDisplayedImage();
                } catch (Exception ex) {
                    ErrorDialog errorDialog = new ErrorDialog(new JFrame(), "Error!");
                }
            }

        });
        
        sharpen = new JMenuItem("Sharpen");
        sharpen.setIcon(new ImageIcon("Pencils_Sharpener_16.png"));
        sharpen.addActionListener(new MenuItemListener(this) {
            
        	public void actionPerformed(ActionEvent e) {
                try {
                	pushState();
                    filters.sharpen(loader.currentImage);
                    updateDisplayedImage();
                } catch (Exception ex) {
                    ErrorDialog errorDialog = new ErrorDialog(new JFrame(), "Error!");
                }
            }

        });
       
    }
    private void rotateMenu(){
    	
    	rotateCW = new JMenuItem("Rotate Clock-Wise");
    	rotateCW.setIcon(new ImageIcon("Update_arrow_16.png"));
        rotateCW.addActionListener(new MenuItemListener(this) {

            public void actionPerformed(ActionEvent e) {
                try {
                	pushState();
                	rotate.rotateCW(loader.currentImage);
                    updateDisplayedImage();
                    fitWindow(showImage.isSelected());
                } catch (Exception ex) {
                    ErrorDialog errorDialog = new ErrorDialog(new JFrame(), "Error!");
                }
            }

        });

        rotateCCW = new JMenuItem("Rotate Counter-Clock-Wise");
    	rotateCCW.setIcon(new ImageIcon("Update_arrow2_16.png"));
        rotateCCW.addActionListener(new MenuItemListener(this) {

            public void actionPerformed(ActionEvent e) {
                try {
                	pushState();
                	rotate.rotateCCW(loader.currentImage);
                    updateDisplayedImage();
                    fitWindow(showImage.isSelected());
                } catch (Exception ex) {
                    ErrorDialog errorDialog = new ErrorDialog(new JFrame(), "Error!");
                }
            }

        });

        flipHor = new JMenuItem("Flip horizontally");
    	flipHor.setIcon(new ImageIcon("Flip_object_16.png"));
        flipHor.addActionListener(new MenuItemListener(this) {

            public void actionPerformed(ActionEvent e) {
                try {
                	pushState();
                	rotate.flipHorizontal(loader.currentImage);
                    updateDisplayedImage();
                } catch (Exception ex) {
                    ErrorDialog errorDialog = new ErrorDialog(new JFrame(), "Error!");
                }

            }

        });
       
        flipVer = new JMenuItem("Flip vertically");
    	flipVer.setIcon(new ImageIcon("Flip_object2_16.png"));
        flipVer.addActionListener(new MenuItemListener(this) {

            public void actionPerformed(ActionEvent e) {
                try {
                	pushState();
                	rotate.flipVertical(loader.currentImage);
                    updateDisplayedImage();
                } catch (Exception ex) {
                    ErrorDialog errorDialog = new ErrorDialog(new JFrame(), "Error!");
                }

            }

        });

    }
    private void undoMenu() {
        
    	restore = new JMenuItem("Undo");
    	restore.setIcon(new ImageIcon("Back_arrow_16.png"));
        restore.addActionListener(new MenuItemListener(this) {

            public void actionPerformed(ActionEvent e) {
                try {
                	
                	if(!lastStates.empty()) {
                		loader.currentImage = lastStates.pop();
                		updateDisplayedImage();
                		fitWindow(showImage.isSelected());
                	}
                }
                catch(Exception esss){
                	ErrorDialog errorDialog = new ErrorDialog(new JFrame(), "Undo error!");
                }
            }

        });
    }
    
    /**
     * Method used to configure the give file chooser with different image filters.
     * @param fileChooser The file chooser that will be configured.
     */
	private void configurFileChooser(JFileChooser fileChooser) {
		fileChooser.setFileFilter(new FileNameExtensionFilter("PNG", "png"));

		fileChooser.setFileFilter(new FileNameExtensionFilter("BMP ", "bmp"));
		
		fileChooser.setFileFilter(new FileNameExtensionFilter("TIFF", "tiff"));

		fileChooser.setFileFilter(new FileNameExtensionFilter("JPEG", "jpg"));
				
		fileChooser.setFileFilter(new FileNameExtensionFilter("GIF", "gif"));
		
	}
	
}
