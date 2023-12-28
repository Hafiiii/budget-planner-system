package prjoct;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class Photo_Frame {
	/** written by:
	 * 		Dayang Nur Ezaah Adibah binti Abdul Jalil @ Awang Abdul Jalil
	 *  	77921
	 *  helped by:
	 *  	Nur Hafizah binti Ramli
	 *  	80609
	 */
	
	private final static int WIDTH = 430;
	private final static int HEIGHT = 932; 
	
	private static String imageDirectory, description;
	private static float amount;
	private static int date, month, year;
	
	static ImageIcon backIcon;
	static JFrame frame;
	static JLayeredPane layeredPane;
	
	static JPanel backPanel;
	static JButton backButton;
	static JLayeredPane buttonLayers;
	static JLabel dateAddedlabel, amountLabel, descLabel;
	
	static Calendar calendar;
	/*constructor.
	 * 		takes the directory, description, amount from the Image
	 * 		and the month and year from the gallery controller
	 * 		makes the image using the openImage function
	 * */
	Photo_Frame(String dir, String desc,  float inAmount, int inDate, int inMonth, int inYear){
		description = desc;
		imageDirectory = dir;
		amount = inAmount;
		date = inDate;
		month = inMonth;
		year = inYear;
		
		layeredPane= new JLayeredPane();
		layeredPane.setBounds(0, 0, WIDTH, HEIGHT);
		calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month);
		
		frame = new JFrame();
        frame.setSize(WIDTH, HEIGHT);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setTitle("Photo Viewer");
        frame.setVisible(true);
        frame.add(layeredPane);
        
		createUI();
        openImage();
        
        frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		        System.out.println("Closing Photo Frame");
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
		
		backIcon = new ImageIcon(Photo_Frame.class.getResource("/back.png"));
		
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
		        System.out.println("Closing Photo Frame");
		        frame.dispose();
		    }
		});
		
		buttonLayers.add(backPanel, JLayeredPane.DEFAULT_LAYER);
		buttonLayers.add(backButton, JLayeredPane.PALETTE_LAYER);
		
		String dateAdded = "Date added: " + date + " " + calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " "  + year;
		
		dateAddedlabel = new JLabel(dateAdded);
		dateAddedlabel.setBounds(68,12,316,28);
		dateAddedlabel.setFont(new Font("Arial", Font.PLAIN, 20));
		dateAddedlabel.setForeground(new Color(140,140,140));
		dateAddedlabel.setHorizontalAlignment(JLabel.CENTER);
		dateAddedlabel.setVisible(true);
		
		amountLabel = new JLabel("Amount: RM" + amount);
		amountLabel.setBounds(68,40,316,28);
		amountLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		amountLabel.setForeground(new Color(140,140,140));
		amountLabel.setHorizontalAlignment(JLabel.CENTER);
		
		amountLabel.setVisible(true);
		
		descLabel = new JLabel(description);
		descLabel.setBounds(0, 800, WIDTH, 52);
		descLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		descLabel.setForeground(new Color(140,140,140));
		descLabel.setHorizontalAlignment(JLabel.CENTER);
		descLabel.setVerticalAlignment(JLabel.CENTER);
		
		layeredPane.add(descLabel,JLayeredPane.PALETTE_LAYER);
		layeredPane.add(buttonLayers, JLayeredPane.PALETTE_LAYER);
		layeredPane.add(dateAddedlabel, JLayeredPane.PALETTE_LAYER);
		layeredPane.add(amountLabel, JLayeredPane.PALETTE_LAYER);
		
	}
	/*	these two function rescales the image to fit in the frame.
     * 	if the image size is greater than the size of the frame, then call the handleImageRatio function to find the scale factor
     * 	then using graphics2d and bufferedImage, rescale the image.
     * */
	public static void openImage() {
		ImageIcon icon = new ImageIcon(imageDirectory);
		Image originalImage = icon.getImage();
		
		int imageWidth = originalImage.getWidth(null);
		int imageHeight = originalImage.getHeight(null);
		
		if (imageWidth > WIDTH || imageHeight > HEIGHT) {
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
        if ( imageHeight > imageWidth)
        	imageLabel.setBounds(0, (HEIGHT / 2) - (imageHeight / 2), imageWidth , imageHeight);
        else
        	imageLabel.setBounds((WIDTH/2) - (imageWidth/2) - 8, (HEIGHT / 2) - (imageHeight / 2), imageWidth , imageHeight);
        imageLabel.setVisible(true);
        layeredPane.add(imageLabel, JLayeredPane.DEFAULT_LAYER);
	}
	public static double handleImageRatio(int imageWidth, int imageHeight) {
		double widthRatio = (double) (WIDTH- 16) / imageWidth;
        double heightRatio = (double) HEIGHT / imageHeight;
        return Math.min(widthRatio, heightRatio);
		// if the image size is greater than the frame size (i.e. image size is 900 x 3000).
	}
}
