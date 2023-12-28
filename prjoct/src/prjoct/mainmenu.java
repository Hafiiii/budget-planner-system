package prjoct;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class mainmenu extends JFrame {
    /** written by:
     * 		Dayang Nur Ezaah Adibah binti Abdul Jalil @ Awang Abdul Jalil
	 * 		77921
	 *  helped by:
	 *  	Ferdinand Morientes Anak Rantai
	 *  	77988
	 */
	private static final long serialVersionUID = 1L;
	private final static int WIDTH = 430;
	private final static int HEIGHT = 932;
	
	
	private JPanel cards;
    private final String MAIN_VIEW = "Main View";
    private final String EXPENSES_VIEW = "Expenses View";
    private static Monthly_Planner_Frame expensesFrame;
    static Monthly_Planner_Controller plans;
    static float balance;
    //private ArrayList<budget_plan_logger> plan_logs;

    public mainmenu() {
    	plans = new Monthly_Planner_Controller(true);
        initComponents();
    }

    private void initComponents() {
        // Set frame properties
        setTitle("UNI Budget Planner");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new CardLayout());
        getContentPane().setBackground(new Color(101, 153, 149));

        cards = new JPanel(new CardLayout());
        getContentPane().add(cards);

        // Main view
        JPanel mainView = new JPanel();
        mainView.setLayout(null);
        mainView.setBackground(new Color(101, 153, 149));

        // Create and set properties for the welcome label
        RoundedLabel welcomeLabel = new RoundedLabel(20);
        welcomeLabel.setText("Welcome Back!");
        welcomeLabel.setBounds(140, 30, 148, 45);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBackground(new Color(0xFFFFFF));
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        welcomeLabel.setBorder(new RoundedBorder(20, new Color(101, 153, 149)));

        // Create and set properties for the total savings label
        JLabel totalSavingsLabel = new JLabel("Total savings");
        totalSavingsLabel.setBounds(165, 88, 103, 30);
        totalSavingsLabel.setForeground(Color.WHITE);
        totalSavingsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        //plans.changeMonth(false);
        balance = plans.getCurrentBalance();
        //belum ubah ke moneyLabel
        JLabel totalSavingsAmount = new JLabel("RM" + balance);
        totalSavingsAmount.setBounds(0, 115, WIDTH - 16, 60);
        totalSavingsAmount.setForeground(Color.WHITE);
        totalSavingsAmount.setHorizontalAlignment(SwingConstants.CENTER);
        totalSavingsAmount.setFont(new Font("Arial", Font.BOLD, 40));

        // Create and set properties for the panel
        RoundedPanel panel = new RoundedPanel(30);
        panel.setBounds(1, 219, 412, 645);
        panel.setBackground(Color.WHITE);
        panel.setBorder(new RoundedBorder(30, new Color(101, 153, 149)));
        panel.setLayout(null);

        // Create and set properties for the view/edit expenses button
        RoundedButton viewEditExpensesButton = new RoundedButton(30);
        viewEditExpensesButton.setText("View/Edit Expenses");
        viewEditExpensesButton.setBounds(90, 380, 240, 48);
        viewEditExpensesButton.setBackground(new Color(101, 153, 149));
        viewEditExpensesButton.setForeground(Color.WHITE);
        viewEditExpensesButton.setFont(new Font("Arial", Font.BOLD, 16));
        viewEditExpensesButton.setBorder(new RoundedBorder(30, new Color(101, 153, 149)));
        viewEditExpensesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	viewEditExpensesButton.setEnabled(false);
                if (expensesFrame == null) {
                    expensesFrame = new Monthly_Planner_Frame(plans);

                } else {
                	expensesFrame = new Monthly_Planner_Frame(plans);
                	System.out.println("Expenses pane still running!");
                }
                expensesFrame = null;
                viewEditExpensesButton.setEnabled(true);
            }
        });
        
        RoundedButton viewGraph = new RoundedButton(30);
        viewGraph.setText("View graph");
        viewGraph.setBounds(90, 430, 240, 48);
        viewGraph.setBackground(new Color(101, 153, 149));
        viewGraph.setForeground(Color.WHITE);
        viewGraph.setFont(new Font("Arial", Font.BOLD, 16));
        viewGraph.setBorder(new RoundedBorder(30, new Color(101, 153, 149)));
        viewGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	ArrayList<Monthly_Entries> temp = plans.getPlanLog();
          	   // ArrayList<Monthly_Entries> entries = new ArrayList<Monthly_Entries>();
          	    int size = plans.getPlanLog().size(), i = 0;
          	    int[] months = new int[size];
          	    float[] values = new float[size];
          	    
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
          	    
          	    for (Monthly_Entries temp1 : plans.getPlanLog()) {
          	    	values[i] = (temp1.getIncomeTotal() - temp1.getExpenseTotal());
          	    	months[i] = temp1.getMonth();
          	    	i+=1;
          	    }
          	    barchart chart = new barchart(values, months, "graph of Savings/Month");
            }
        });
        panel.add(viewGraph);
        panel.add(viewEditExpensesButton);
        // Expenses view
        JPanel expensesView = new JPanel();
        // Add components and set layout for the expenses view panel

        mainView.add(welcomeLabel);
        mainView.add(totalSavingsLabel);
        mainView.add(totalSavingsAmount);
        mainView.add(panel);
        cards.add(mainView, MAIN_VIEW);
        cards.add(expensesView, EXPENSES_VIEW);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            mainmenu mainmenu = new mainmenu();
            mainmenu.setVisible(true);
            //mainmenu.setIconImage(new ImageIcon(mainmenu.class.getResource("/addImage.png")));
        });
    }
}