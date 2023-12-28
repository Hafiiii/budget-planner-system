package prjoct;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.text.DateFormatSymbols;

public class barchart extends JPanel {
	/** written by:
	 * 		Ferdinand Morientes Anak Rantai
	 *  	77988
	 */
	private static final long serialVersionUID = 1L;
	private float[] values;
	private int[] months;
	private String title;

  public barchart(float[] v, int[] months, String t) {
	  values = v;
	  this.months = months;
	  title = t;
	  
	  JFrame frame = new JFrame(); // Create a new JFrame
	    frame.setSize(430, 930); // Set the size of the frame
	    frame.getContentPane().add(this); // Add the barchart panel to the frame's content pane
	    
	    WindowListener wndCloser = new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	            System.out.println("closing chart");
	        }
	    };
	    frame.addWindowListener(wndCloser);
	    frame.setVisible(true);
  }

  public void paintComponent(Graphics g) {
	  setOpaque(false);
	  setBounds(10, 313, 394, 328);
	  String[] shortMonths = new DateFormatSymbols().getShortMonths();
	  
    super.paintComponent(g);
    if (values == null || values.length == 0)
      return;
    double minValue = 0;
    double maxValue = 0;
    for (int i = 0; i < values.length; i++) {
      if (minValue > values[i])
        minValue = values[i];
      if (maxValue < values[i])
        maxValue = values[i];
    }


    int clientWidth = 394;
    int clientHeight = 328;
    double barWidthScale = 1.0 / values.length; // Calculate the scale dynamically

    Font titleFont = new Font("SansSerif", Font.BOLD, 16);
    FontMetrics titleFontMetrics = g.getFontMetrics(titleFont);
    Font labelFont = new Font("SansSerif", Font.PLAIN, 10);
    FontMetrics labelFontMetrics = g.getFontMetrics(labelFont);

    int titleWidth = titleFontMetrics.stringWidth(title);
    int y = titleFontMetrics.getAscent();
    int x = (clientWidth - titleWidth) / 2;
    g.setFont(titleFont);
    g.drawString(title, x, y);

    int top = titleFontMetrics.getHeight();
    int bottom = labelFontMetrics.getHeight();
    if (maxValue == minValue)
        return;
    double scale = (clientHeight - top - bottom) / (maxValue - minValue);
    y = clientHeight - labelFontMetrics.getDescent();
    g.setFont(labelFont);

    for (int i = 0; i < values.length; i++) {
        int valueX = (int) (i * clientWidth * barWidthScale);
        int valueY = top;
        int height = (int) (values[i] * scale);
        if (values[i] >= 0)
            valueY += (int) ((maxValue - values[i]) * scale);
        else {
            valueY += (int) (maxValue * scale);
            height = -height;
        }

        // draw x axis label
        g.drawString("Month", clientWidth / 2, clientHeight - 10);

        // draw y axis label
        Graphics2D g2d = (Graphics2D) g;
        g2d.rotate(-Math.PI / 2);
        g2d.drawString("Savings", -clientHeight / 2, 10);
        g2d.rotate(Math.PI / 2);

        g.setColor(Color.blue);
        g.fillRect(valueX, valueY + 20, (int) (clientWidth * barWidthScale) - 10, height - 20);
        g.setColor(Color.black);
        g.drawRect(valueX, valueY + 20, (int) (clientWidth * barWidthScale) - 10, height - 20);
        int labelWidth = labelFontMetrics.stringWidth(shortMonths[months[i]]);
        x = (int) (i * clientWidth * barWidthScale + (clientWidth * barWidthScale - labelWidth) / 2);
        g.drawString(shortMonths[months[i]], x, y);
    }
}

//  public static void main(String[] argv) {
//    JFrame f = new JFrame();
//    f.setSize(430, 930);
//    float[] values = new float[7];
//    int[] names = new int[7];
//    values[0] = -11;
//    names[0] = 0;
//
//    values[1] = 2;
//    names[1] = 1;
//
//    values[2] = 4;
//    names[2] = 2;
//    
//    values[3] = 3;
//    names[3] = 3;
//    
//    values[4] = 6;
//    names[4] = 4;
//    
//    values[5] = 6;
//    names[5] = 4;
//    
//    values[6] = 6;
//    names[6] = 4;
//    barchart chart = new barchart(values, names, "title");
//    f.getContentPane().add(chart);
//
//    WindowListener wndCloser = new WindowAdapter() {
//      public void windowClosing(WindowEvent e) {
//        System.exit(0);
//      }
//    };
//    f.addWindowListener(wndCloser);
//    f.setVisible(true);
//  }
}