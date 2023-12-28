package prjoct;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Gallery_Frame {
	/** written by:
	 * 		Dayang Nur Ezaah Adibah binti Abdul Jalil @ Awang Abdul Jalil
	 *  	77921
	 *  helped by:
	 *  	Nur Hafizah binti Ramli
	 *  	80609
	 */
	
	private final static int WIDTH = 430;
	private final static int HEIGHT = 932;
	
	private static int current_month, current_year;
	private static ArrayList<prjoct.Image> imagesInThisMonth;
	static Gallery_Controller gallery;
	
	static Font arial = new Font("Arial", Font.PLAIN, 20);
	
	static JFrame frame;
	static JPanel backPanel;
	static JButton backButton;
	static JLayeredPane buttonLayers, galleryLayer;
	 
	static ImageIcon backIcon, addImage;
	static Calendar calendar;
	static boolean testing = false;
	
	/*constructor.
	 * 		takes the month and year from the Monthly_Planner_Controller
	 *		to be used in adding, deleting editing images
	 * */
	public Gallery_Frame(int inMonth, int inYear) {
		current_month = inMonth;
		current_year = inYear;
		gallery = new Gallery_Controller(inYear, inMonth);
		imagesInThisMonth = gallery.getImagesInMonth();
		calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, current_month);
		
		createUI();
		
		frame = new JFrame();
		frame.setSize(WIDTH, HEIGHT);
		frame.setTitle("Gallery");
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setVisible(true);
		
		frame.add(galleryLayer);
		frame.add(buttonLayers);
		
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		    	System.out.println("Closing Photo Frame...");
		        int writeStatus = gallery.writeToFile();
		        if (writeStatus == 1)
		        	System.out.println("Image Write Successful. closing...");
		        else
		        	System.out.println("Image Write failed! Closing.");
		        frame.dispose();
		        
		    }
		});
    }
	
	/*	initializes the necessary variables, panels, button UI elements, etc
	 * 	creates the back button and adds an action when pressed.
	 * */
	public static void createUI() {
		buttonLayers = new JLayeredPane();
		buttonLayers.setBounds(0, 0, WIDTH, HEIGHT);
		
		backPanel = new JPanel();
		backPanel.setBounds(10, 10, 58, 58);
		backPanel.setBackground(new Color(217,217,217));
		backPanel.setVisible(true);
		
		backIcon = new ImageIcon(Gallery_Frame.class.getResource("/back.png"));
		addImage = new ImageIcon(Gallery_Frame.class.getResource("/addImage.png"));
		
		backButton = new JButton();
		backButton.setBounds(10, 10, 58, 58);
		backButton.setVisible(true);
		backButton.setOpaque(false);
		backButton.setContentAreaFilled(false);
		backButton.setBorderPainted(false);
		backButton.setIcon(backIcon);
		backButton.setFocusable(false);
		backButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent  e) {
		    	JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(backButton);
		    	System.out.println("Closing Photo Frame...");
		        int writeStatus = gallery.writeToFile();
		        if (writeStatus == 1)
		        	System.out.println("Image Write Successful. closing...");
		        else
		        	System.out.println("Image Write failed! Closing.");
		        currentFrame.dispose();
		    }
		});
		
		buttonLayers.add(backPanel, JLayeredPane.DEFAULT_LAYER);
		buttonLayers.add(backButton, JLayeredPane.PALETTE_LAYER);

		galleryLayer = new JLayeredPane();
		galleryLayer.setBounds(0, 0, WIDTH, HEIGHT);
		createGalleryPanels();
		
	}
	
	static int i = 0;
	
	/* creates the panel for the gallery
	 * uses a range based for loop using the imagesInThisMonth variable
	 * pseudocode: 
	 * 		for every Image in seq of images
	 * 			create galleryPanel from image
	 * 
	 * */
	public static void createGalleryPanels() {
		String price, dateAdded = "heart on wave doo doo doo";
		i = 0;
			
		if(imagesInThisMonth != null)
		{
			for (prjoct.Image image : imagesInThisMonth) {
				JPanel galleryPanel = new JPanel();
				galleryPanel.setBounds(0, 76 + (i * 204), WIDTH, 204);
				galleryPanel.setBackground(new Color(217,217,217));
				galleryPanel.setOpaque(true);
				galleryLayer.add(galleryPanel,JLayeredPane.PALETTE_LAYER);
				
				JPanel imageBGPanel = new JPanel();
				imageBGPanel.setBounds(19, 87 + (i * 204), 188, 188);
				imageBGPanel.setBackground(Color.BLACK);
				imageBGPanel.setOpaque(true);
				galleryLayer.add(imageBGPanel,JLayeredPane.MODAL_LAYER);
				
				openAndHandleImage(image.getDir(), i);
				
				JButton viewImageButton = new JButton();
				viewImageButton.setBounds(19, 87 + (i * 204), 188, 188);
				viewImageButton.setOpaque(false);
				viewImageButton.setContentAreaFilled(false);
				viewImageButton.setBorderPainted(false);
				viewImageButton.addActionListener(new ActionListener() {
				    @Override
				    public void actionPerformed(ActionEvent  e) {
				        /* if she asks "why do you need to store the the data in the local variables (instead of plugging it in directly)?"
				         * answer: the values are not saved in the actionPerformed function.
				         * 			if we pass a variable int i which is defined outside the lambda function (which is this (actionPerformed) function,
				         * 			into the viewPhoto function it will automatically resolve to 0 for some reason.
				         * */
				        String dir = image.getDir(), desc = image.getDesc();
				        float amount = image.getAmount();
				        int date = image.getDate();
				        viewPhoto(dir, desc, amount , date , current_month, current_year);
				        //Photo_Frame photo = new Photo_Frame("records\\2023\\5\\receipt-template-us-band-blue-750px.png","Virtual Insanity",200.0f,30,5,2023);
				    }
				});
				galleryLayer.add(viewImageButton,JLayeredPane.DRAG_LAYER);
				
				dateAdded = "Date Added: "+ calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " + image.getDate();
				JLabel dateAddedLabel = new JLabel();
				dateAddedLabel.setBounds(215, 87 + (i * 204), 191, 45);
				dateAddedLabel.setText(dateAdded);
				dateAddedLabel.setHorizontalAlignment(JLabel.CENTER);
				dateAddedLabel.setVerticalAlignment(JLabel.CENTER);
				dateAddedLabel.setFont(arial);
				galleryLayer.add(dateAddedLabel,JLayeredPane.MODAL_LAYER);
				
				JLabel descriptionLabel = new JLabel();
				descriptionLabel.setBounds(215, 135 + (i * 204), 191, 45);
				descriptionLabel.setText(image.getDesc());
				//descriptionLabel.setText("Car tune");
				descriptionLabel.setHorizontalAlignment(JLabel.CENTER);
				descriptionLabel.setVerticalAlignment(JLabel.CENTER);
				descriptionLabel.setFont(arial);
				galleryLayer.add(descriptionLabel,JLayeredPane.MODAL_LAYER);
				
				price = "RM" + image.getAmount();
				JLabel amountLabel = new JLabel();
				amountLabel.setBounds(215, 180 + (i * 204), 191, 45);
				amountLabel.setText(price);
				amountLabel.setHorizontalAlignment(JLabel.CENTER);
				amountLabel.setVerticalAlignment(JLabel.CENTER);
				amountLabel.setFont(arial);
				galleryLayer.add(amountLabel,JLayeredPane.MODAL_LAYER);
				
				JButton editButton = new JButton("edit");
				editButton.setBounds(215, 225 + (i * 204), 91, 45);
				editButton.setHorizontalAlignment(JLabel.CENTER);
				editButton.addActionListener(new ActionListener() {
				    @Override
				    public void actionPerformed(ActionEvent  e) {
				    	 /* if she asks "why do you need to store the the data in the local variables (instead of plugging it in directly)?"
				         * answer: the values are not saved in the actionPerformed function.
				         * 			if we pass a variable int i which is defined outside the lambda function (which is this (actionPerformed) function,
				         * 			into the editPhoto function it will automatically resolve to 0, and edit the firstmost element in the Images array
				         * what does repaint screen do?
				         * answer: removes the gallery panels (the rows that you see) and creates new panels to represent the new updates.
				         * */
				        String dir = image.getDir();
				        i = gallery.getImageIndex(dir);
				        editPhoto(i);
				        repaintScreen();
				      //  viewPhoto(dir, desc, amount , date , current_month, current_year);
				        //Photo_Frame photo = new Photo_Frame("records\\2023\\5\\receipt-template-us-band-blue-750px.png","Virtual Insanity",200.0f,30,5,2023);
				    }
				});
				galleryLayer.add(editButton,JLayeredPane.MODAL_LAYER);
				
				JButton deleteButton = new JButton("delete");
				deleteButton.setBounds(315, 225 + (i * 204), 91, 45);
				deleteButton.setHorizontalAlignment(JLabel.CENTER);
				deleteButton.addActionListener(new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e) {
		            	/* a simple confirm delete button.
		            	 *  if she asks "why do you need to store the the data in the local variables (instead of plugging it in directly)?"
				         * answer: the values are not saved in the actionPerformed function.
				         * 			if we pass a variable int i which is defined outside the lambda function (which is this (actionPerformed) function,
				         * 			into the viewPhoto function it will automatically resolve to 0 for some reason.
				         * */
		            	
		            	String directory = image.getDir();
		            	String desc = image.getDesc();
		            	i = gallery.getImageIndex(directory);
		            	System.out.println("deleting " + desc);
		            	String prompt = "Confirm delete " + desc + "?";
		            	int answer =  JOptionPane.showConfirmDialog(null, prompt, "Delete?",JOptionPane.YES_NO_OPTION);
		            	
		            	System.out.println(answer);
		            	if (answer == 0) { 
		            		gallery.deleteEntry(i);
		            		System.out.println("Deleted " + desc);
		            		//repaintFrame();
		            	} else 
		            		System.out.println("Cancelled Deleting " + desc);
		            	repaintScreen();
		            }
		        });
				galleryLayer.add(deleteButton,JLayeredPane.MODAL_LAYER);
				
				i +=1 ;
			}
		}
		if (i < 4) {
			JPanel addBGPanel = new JPanel();
			addBGPanel.setBounds(19, 87 + (i * 204), 188, 188);
			addBGPanel.setBackground(new Color(253,253,253));
			addBGPanel.setOpaque(true);
			galleryLayer.add(addBGPanel,JLayeredPane.PALETTE_LAYER);
			
			JButton addImageButton = new JButton();
			addImageButton.setBounds(current_month, current_year, 188, 188);
			addImageButton.setBounds(19, 87 + (i * 204), 188, 188);
			addImageButton.setOpaque(false);
			addImageButton.setContentAreaFilled(false);
			addImageButton.setBorderPainted(false);
			addImageButton.setIcon(addImage);
			addImageButton.setFocusable(false);
			addImageButton.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent  e) {
			    	addPhoto();
			    	
			        //Photo_Frame photo = new Photo_Frame("records\\2023\\5\\receipt-template-us-band-blue-750px.png","Virtual Insanity",200.0f,30,5,2023);
			    }
			});
			
			galleryLayer.add(addImageButton,JLayeredPane.MODAL_LAYER);
			i = 0;
		}
	}
    
	/* removes and the gallery panels in the frame, and adds creates new ones to represent the changes.*/
    public static void repaintScreen() {
    	// similar to frame.java's repaintFrame.
    	imagesInThisMonth = gallery.getImagesInMonth();
    	galleryLayer.removeAll();
    	galleryLayer.revalidate();
    	galleryLayer.repaint();
    	createGalleryPanels();
    	
    }
    
    public static void viewPhoto(String dir, String desc, float amount, int date,  int month, int year) {
    	System.out.println("Opening Photo Frame");
    	Photo_Frame photo = new Photo_Frame(dir, desc, amount, date, month, year);
    	// a function used when clicking on an image.
    	// instantiates Photo_Frame(String dir, float amount, int date, int month)
    	// which opens a new frame showing the image in full hd
    }
    
    /* offers the user the capability change the date, description and amount.
     * possible question: "why cannot change the image?"
     * 		"programming oversight" or
     * 		 "just like how you accidentally send an image in the group chat, instinctively youll delete it instead of editing it."
     * */
    public static void editPhoto(int index) {
    	
    	prjoct.Image temp = gallery.getSpecific(index);
    	
	    JTextField dateField = new JTextField(10);
	    dateField.setText(String.valueOf(temp.getDate()));
	    JTextField descField = new JTextField(10);
	    descField.setText(temp.getDesc());
    	JTextField amountField = new JTextField(10);
    	amountField.setText(String.valueOf(temp.getAmount()));
	    
	    JPanel editPanel = new JPanel();
	    
	    // gridlayout creates a grid layout given the rows and collumns
	    editPanel.setLayout(new GridLayout(3,2));
	    editPanel.add(new JLabel("Enter new Date"));
	    editPanel.add(dateField);
	    editPanel.add(new JLabel("Enter new description:"));
	    editPanel.add(descField);
	    editPanel.add(new JLabel("Enter amount:"));
	    editPanel.add(amountField);
	    
	    int result = JOptionPane.showConfirmDialog(null, editPanel, "Add Image", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
	    	int date =  Integer.parseInt(dateField.getText());
	    	String description = (String) descField.getText();
            float amount = Float.parseFloat(amountField.getText());

            System.out.println("new date: " + date);
            System.out.println("new description " + description);
            System.out.println("Amount entered: " + amount);
            
            gallery.editEntry(index, description, amount, date);
            
            repaintScreen();
	    }
    }
    
    static String directory;
    
    /* function to add a photo into the database.
     * creates a JOptionPane asking the user to choose the image, the description date and amount.
     * */
    public static void addPhoto() {
    	JTextField dateField = new JTextField(10);
	    JTextField descField = new JTextField(10);
    	JTextField amountField = new JTextField(10);
    	JButton openFileChooser = new JButton("open file");
    	
    	//int returnValue = 0;
    	
    	dateField.setEnabled(false);
    	descField.setEnabled(false);
    	amountField.setEnabled(false);

    	// this one here makes sure that that the user has already chosen an actual image.
    	openFileChooser.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png"));
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                // User selected a file
                // Handle the selected file here
            	directory = fileChooser.getSelectedFile().getPath();
                System.out.println("Selected file: " + directory);
                dateField.setEnabled(true);
            	descField.setEnabled(true);
            	amountField.setEnabled(true);
            }
        });
    	
    	JPanel addPanel = new JPanel();
    	addPanel.setLayout(new GridLayout(4,2));
    	addPanel.add(new JLabel("Choose image"));
    	addPanel.add(openFileChooser);
    	addPanel.add(new JLabel("Enter Date"));
    	addPanel.add(dateField);
    	addPanel.add(new JLabel("Enter description:"));
	    addPanel.add(descField);
	    addPanel.add(new JLabel("Enter amount:"));
	    addPanel.add(amountField);
	    
	    int result = JOptionPane.showConfirmDialog(null, addPanel, "Add Expense", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
	    	int date =  Integer.parseInt(dateField.getText());
	    	String description = (String) descField.getText();
            float amount = Float.parseFloat(amountField.getText());

            //System.out.println("Directory: " + directory);
            System.out.println("Selected file: " + directory);
            System.out.println("new date: " + date);
            System.out.println("new description " + description);
            System.out.println("Amount entered: " + amount);
            
            // this one here adds to the database.
            gallery.addEntry(directory, description, amount, date);
            
            repaintScreen();
            //in reaint frame make sure to redo the imagesInThisMonth = gallery.getImagesInMonth();
            // coz it wont show things
	    }
    }
    
    /*	these two function makes the small "preview images" in the gallery panel.
     * 	if the image size is greater than the size of the preview image, then call the handleImageRatio function to find the scale factor
     * 	then using graphics2d and bufferedImage, rescale the image.
     * */
    // Helper functions
    public static void openAndHandleImage(String imageDir, int offset) {
    	
    	ImageIcon icon = new ImageIcon(imageDir);
		Image originalImage = icon.getImage();
		
		int imageWidth = originalImage.getWidth(null);
		int imageHeight = originalImage.getHeight(null);
		
		if (imageWidth > 188 || imageHeight > 188) {
            double scaleFactor = handleImageRatio(imageWidth,imageHeight);
            imageWidth = (int) (imageWidth * scaleFactor);
            imageHeight = (int) (imageHeight * scaleFactor);
		}
		BufferedImage scaledImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = scaledImage.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(originalImage, 0, 0, imageWidth, imageHeight, null);
        g2d.dispose();
		
        ImageIcon scaled = new ImageIcon(scaledImage);
        
        JLabel imageLabel = new JLabel(scaled, JLabel.CENTER);
        if (imageHeight > imageWidth)
        	imageLabel.setBounds(19+94 - (imageWidth/2), 87 + (i * 204), imageWidth , imageHeight);
        else
        	imageLabel.setBounds(19+94 - (imageWidth/2), 87 + (i * 204) + 94 - (imageHeight / 2 ), imageWidth , imageHeight);
        imageLabel.setVisible(true);
        galleryLayer.add(imageLabel, JLayeredPane.POPUP_LAYER);
	}
    
	public static double handleImageRatio(int imageWidth, int imageHeight) {
		double widthRatio = (double) 188 / imageWidth;
        double heightRatio = (double) 188 / imageHeight;
        return Math.min(widthRatio, heightRatio);
		// if the image size is greater than the frame size (i.e. image size is 900 x 3000).
	}
    
}
