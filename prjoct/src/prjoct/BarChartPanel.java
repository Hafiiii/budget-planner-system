package prjoct;
//package prjoct;
//
//import java.util.ArrayList;
//
//public class teso{
//	public static void main(String args[]) {
//
////		Monthly_Entries month1 = new Monthly_Entries(2023, 5);
////		Monthly_Entries month2 = new Monthly_Entries(2023, 6);
////		
////		System.out.println(month1.getYear());
////		
////		month1.addEntry("Heya", 100.0f, true);
////		month1.addEntry("Hoya", 50.0f, true);
////		month1.addEntry("Hiya", 125.0f, true);
////		month1.calcEntries();
////		month2.addEntry("fish", 25.6f, true);
////		month2.addEntry("bucks", 28.8f, true);
////		month2.calcEntries();
////		System.out.println("total price for month1: " + month1.getIncomeTotal());
////		System.out.println("total price for month2: " + month2.getIncomeTotal());
//		
////		for (budget_entries temp : month1.getIncome()) {
////			System.out.println("name: " + temp.getName());
////			System.out.println("price: " + temp.getAmount());
////		}for (budget_entries temp : month2.getIncome()) {
////			System.out.println("name: " + temp.getName());
////			System.out.println("price: " + temp.getAmount());
////		}
//		Monthly_Planner_Controller plans = new Monthly_Planner_Controller(true);
////		plan.addPlanLog(month1);
////		plan.addPlanLog(month2);
////		plan.writeToFile();
////		plan.readFromFile();
////		plan.writePlanToFile();
////		budget_plan_logger temp1 = plan.readPlanFromFile(2023, 5);
////		System.out.println(plan.getMonth());
////		plan.changeMonth(true);
////		System.out.println(plan.getMonth());
//		ArrayList<Monthly_Entries> temp1 = plans.getPlanLog();
//		temp1.size();
//		for (Monthly_Entries log : temp1) {
//			//if (plan.getMonth() == log.getMonth()) {
//				System.out.println(log.getYear());
//				System.out.println(log.getMonth());
//				log.calcEntries();
//				System.out.println(log.getIncomeTotal() - log.getExpenseTotal());
//				for (budget_entries entries : log.getExpense()) {
//					System.out.println("name: " + entries.getName());
//					System.out.println("price: " + entries.getAmount());
//				}
//			//}
//		}
//		//@SuppressWarnings("unused")
//		//String dir = "C:/Users/Administrator/Desktop/ezgif.com-webp-to-png.png";
//		//String receipt = "records\\2023\\5\\receipt-template-us-band-blue-750px.png";
//		//Photo_Frame photo = new Photo_Frame(dir,"Virtual Insanity",200.0f,30,5,2023);
//		Gallery_Controller gallery = new Gallery_Controller();
//		for (prjoct.Image image : gallery.getAll()) {
//			System.out.println(image.getMonth());
//			System.out.println(image.getDesc());
//			System.out.println(image.getDir());
//		}
//		
//		//frame f = new frame();
//	}
//}
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BarChartPanel extends JPanel {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int[] expenses;
	  private float[] values;
	  private String[] months;
	
	  public BarChartPanel(int[] expenses, float[] values) {
	    this.expenses = expenses;
	    this.values = values;
	    this.months = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	
	    setPreferredSize(new Dimension(400, 300));
	  }
	
	  @Override
	  public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	
	    // Set the background color
	    g.setColor(Color.WHITE);
	    g.fillRect(0, 0, getWidth(), getHeight());
	
	    // Draw the bars
	    int barWidth = getWidth() / months.length;
	    int barHeight = getHeight() - 40;
	    for (int i = 0; i < expenses.length; i++) {
	      int value = expenses[i];
	      int valueY = getHeight() - 20 - (value * barHeight / 1000);
	      g.setColor(Color.BLUE);
	      g.fillRect(i * barWidth, valueY, barWidth - 10, getHeight() - valueY);
	      g.setColor(Color.BLACK);
	      g.drawRect(i * barWidth, valueY, barWidth - 10, getHeight() - valueY);
	    }
	
	    // Add labels for each month
	    for (int i = 0; i < months.length; i++) {
	      int monthX = i * barWidth + barWidth / 2;
	      int monthY = getHeight() - 20;
	      g.drawString(months[i], monthX, monthY);
	    }
	
	    // Add labels for each expense
	    String[] expenseLabels = { "Rent", "Groceries", "Utilities", "Transportation", "Food", "Insurance", "Tax",
	        "Other" };
	    int expenseWidth = barWidth / expenseLabels.length;
	    for (int i = 0; i < expenseLabels.length; i++) {
	      int expenseX = i * expenseWidth + expenseWidth / 2;
	      int expenseY = getHeight() + 20;
	      g.drawString(expenseLabels[i], expenseX, expenseY);
	    }
	  }
	
	  public static void main(String[] argv) {
	    Monthly_Planner_Controller plans = new Monthly_Planner_Controller(true);
	    ArrayList<Monthly_Entries> temp = plans.getPlanLog();
	   // ArrayList<Monthly_Entries> entries = new ArrayList<Monthly_Entries>();
	    int size = plans.getPlanLog().size(), i = 0;
	    int[] months = new int[size];
	    float[] values = new float[size];
	    String[] names = new String[size];
	    
	    Collections.sort(temp, new Comparator<Monthly_Entries>() {
            @Override
            public int compare(Monthly_Entries bp1, Monthly_Entries bp2) {
                // Compare the years
                int yearComparison = Integer.compare(bp1.getYear(), bp2.getYear());
                if (yearComparison != 0) {
                    return yearComparison;
                }

                // If the years are equal, compare the months
                return Integer.compare(bp1.getMonth(), bp2.getMonth());
            }
        });
	    
	    for (Monthly_Entries budgetPlan : temp) {
            System.out.println(budgetPlan.getYear() + ", " + budgetPlan.getMonth());
        }
	    
	    for (Monthly_Entries temp1 : plans.getPlanLog()) {
	    	values[i] = (temp1.getIncomeTotal() - temp1.getExpenseTotal());
	    	months[i] = temp1.getMonth();
	    	i+=1;
	    }

	    JFrame f = new JFrame("Monthly Expenses");
	    f.setSize(400, 300);
	    BarChartPanel panel = new BarChartPanel(months, values);
	    f.getContentPane().add(panel);
	    f.setVisible(true);
	    WindowListener wndCloser = new WindowAdapter() {
	      public void windowClosing(WindowEvent e) {
	        System.exit(0);
	      }
	    };
	    f.addWindowListener(wndCloser);
	    f.setVisible(true);
	  }
}

//for (Object obj : temp) {
//entries.add((Monthly_Entries) obj);
//}
//

//
//for (int i = 0; i < size; i++) {
//Monthly_Entries entry = entries.get(i);
//values[i] = entry.getIncomeTotal() - entry.getExpenseTotal();
//names[i] = "Month " + entry.getMonth() + " " + entry.getYear();
//}
//