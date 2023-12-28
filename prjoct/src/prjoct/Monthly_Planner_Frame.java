package prjoct;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Monthly_Planner_Frame extends JFrame implements ActionListener{
	/** written by:
	 * 		Imran Harriz bin Madzalan
	 *  	78048
	 * 700 lines of doo dah never make me do this ever again
	 */
	private static final long serialVersionUID = 1L;
	
	static JLayeredPane incomeAndExpensesPane, incomePane, expensePane, lpane;
	static ImageIcon editIcon, deleteIcon, addNewBudgetIcon, addNewEntryIcon;
	static Font arial = new Font("Arial", Font.PLAIN, 20), priceArial = new Font("Arial", Font.PLAIN, 16);
	static Monthly_Planner_Controller plans;
	static Monthly_Entries log;
	static float incomeTotal, expenseTotal, grandTotal;
	static int i = 0;
	static JLabel incomeTotalLabel, expenseTotalLabel, monthLabel, yearLabel;
	static Calendar calendar;
	static JFrame frame;
	static JButton addButton, subButton, viewImagesButton, addNewBudgetPlan;
	static JPanel topBar, expenseBG, incomeBG;
	static JLabel remainingLabel, moneyLabel, expenseLabel, incomeLabel;
	static JOptionPane deletePane;
	
	Monthly_Planner_Frame(Monthly_Planner_Controller inLog){
		plans = inLog;
		log = plans.getLog();
		
		createTopBar();
	}
	
	Monthly_Planner_Frame() {
	    plans = new Monthly_Planner_Controller(true);
	    log = plans.getLog();
	    createTopBar();
	}
	/*	initializes the necessary variables, panels, button UI elements, etc
	 * 	creates the change month buttons, month and year labels.
	 * */
	public static void createTopBar() {
		frame = new JFrame();
	    topBar = new JPanel();
	    addButton = new JButton();
	    subButton = new JButton();
	    lpane = new JLayeredPane();
	    
	    incomeAndExpensesPane = new JLayeredPane();
	    incomeAndExpensesPane.setSize(430, 932);
	    expensePane = new JLayeredPane();
	    expensePane.setSize(430, 932);
	    incomePane = new JLayeredPane();
	    incomePane.setSize(430, 932);
	    //URL url = Monthly_Planner_Frame.class.getResource("/edit.png");
	    editIcon = new ImageIcon(Monthly_Planner_Frame.class.getResource("/edit.png"));
	    deleteIcon = new ImageIcon(Monthly_Planner_Frame.class.getResource("/delete.png"));
	    addNewBudgetIcon = new  ImageIcon (Monthly_Planner_Frame.class.getResource("/newBudgetEntry.png"));
	    addNewEntryIcon= new ImageIcon (Monthly_Planner_Frame.class.getResource("/add.png"));

	    monthLabel = new JLabel();
	    yearLabel = new JLabel();
	    remainingLabel = new JLabel();
	    moneyLabel = new JLabel();
	    viewImagesButton = new JButton();
	    addNewBudgetPlan  = new JButton();
	    expenseLabel = new JLabel();
	    expenseTotalLabel = new JLabel();
	    expenseBG = new JPanel();
	    incomeLabel = new JLabel();
	    incomeTotalLabel = new JLabel();
	    incomeBG = new JPanel();
	    calendar = Calendar.getInstance();
	    
	    deletePane = new JOptionPane();
		
		// top bar shenadigans
		addButton.setBackground(new Color(123,155,239));
		addButton.setText("Prev");
		addButton.setForeground(Color.white);
		addButton.setBounds(25, 49, 129, 49);
		addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	refreshAndChangeTime(false);
            }
        });
		
		subButton.setBackground(new Color(123,155,239));
		subButton.setBounds(266, 49, 129, 49);
		subButton.setForeground(Color.white);
		subButton.setText("Next");
		subButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	refreshAndChangeTime(true);
            }
        });
		
		yearLabel.setOpaque(false);
		yearLabel.setText(String.valueOf(plans.getYear()));
		yearLabel.setFont(priceArial);
		yearLabel.setHorizontalAlignment(JLabel.CENTER);
		yearLabel.setBounds(10,74,397,44);
		
		
		monthLabel.setOpaque(true);
		monthLabel.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
		monthLabel.setFont(arial);
		monthLabel.setForeground(Color.black);
		monthLabel.setHorizontalAlignment(JLabel.CENTER);
		monthLabel.setBackground(new Color(93,172,228));
		monthLabel.setBounds(10,33,397,79);
		
		
		lpane.setBounds(0,0,430,932);
		lpane.add(yearLabel, JLayeredPane.PALETTE_LAYER);
		lpane.add(monthLabel, JLayeredPane.DEFAULT_LAYER);
		lpane.add(addButton, JLayeredPane.PALETTE_LAYER);
		lpane.add(subButton, JLayeredPane.PALETTE_LAYER);
		
		createUI();
		
		
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Plans");
		frame.setResizable(false);
		frame.setLayout(null);
		frame.getContentPane().setBackground(new Color(108,169,165));
		frame.setSize(430, 932);
		frame.setVisible(true);
		frame.add(lpane);
		frame.add(incomeAndExpensesPane);
		
		frame.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent e) {
		        int writeStatus = plans.writeToFile();
		        System.out.println("Closing planner");
		        
		        if (writeStatus == 1)
		        	System.out.println("Write Successful. closing...");
		        else
		        	System.out.println("Write failed! Closing.");
		       // System.exit(0);
		        frame.dispose();
		    }
		});
	}
	/* creates the panel for the expenses
	 * uses a range based for loop using the Monthly_Entries variable
	 * pseudocode: 
	 * 		for every expense in seq of expenses
	 * 			create expensePanel from expense
	 * spacing of the expensePanels/incomePanels are handled using math.
	 * 	specifically the 320 + (i*(6+43)) in the 'y' parameter of setBounds.
	 * */
	public static void createExpensePane() {
		String price;
		final int EXPENSEMAX = 7;
		i = 0;
		for (budget_entries expenses: log.getExpense()) {
			
			// background
			JLabel expensePanel = new JLabel();
			expensePanel.setBounds(10, 320 + (i*(6+43)), 397, 43);
			expensePanel.setBackground(new Color(226,135,135));
			expensePanel.setOpaque(true);
			expensePane.add(expensePanel, JLayeredPane.PALETTE_LAYER);
			JLabel nameEPanel = new JLabel();
			
			//	expense name
			price = String.valueOf(expenses.getAmount());
			nameEPanel.setBounds(10+1, 320 + (i*(6+43)), 230-1, 43);
			nameEPanel.setText(expenses.getName());
			nameEPanel.setFont(arial);
			nameEPanel.setHorizontalAlignment(JLabel.CENTER);
			expensePane.add(nameEPanel, JLayeredPane.MODAL_LAYER);
			
			//	expense price
			JLabel pricePanel = new JLabel();
			pricePanel.setBounds(210, 320 + (i*(6+43)), 85, 43);
			pricePanel.setText("RM"+ price);
			pricePanel.setFont(priceArial);
			expensePane.add(pricePanel, JLayeredPane.MODAL_LAYER);
			
			//	edit button
			JButton editExpense = new JButton();
			editExpense.setBounds(320, 320 + (i*(6+43)), 45, 43);
			editExpense.setIcon(editIcon);
			editExpense.setHorizontalAlignment(JLabel.CENTER);
			editExpense.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					/*	button to delte the expense/income
					 *  if she asks "why do you need to store the the data in the local variables (instead of plugging it in directly)?"
			         * answer: the values are not saved in the actionPerformed function.
			         * 			if we pass a variable int i which is defined outside the lambda function (which is this (actionPerformed) function,
			         * 			into the editExpenseEntry function it will automatically resolve to 0 for some reason.
			         * 			thus editing the firt item instead of the 3th for example.
			         * */
					String name = expenses.getName();
					i = log.getExpenseIndex(name);
					editExpenseEntry(i);
				}
			});
			expensePane.add(editExpense, JLayeredPane.MODAL_LAYER);
			
			// delete button
			JButton deleteExpense = new JButton();
			deleteExpense.setBounds(362, 320 + (i*(6+43)), 45, 43);
			deleteExpense.setIcon(deleteIcon);
			deleteExpense.setHorizontalAlignment(JLabel.CENTER);
			deleteExpense.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	/*	button to delete the expense/income.
	            	 *  if she asks "why do you need to store the the data in the local variables (instead of plugging it in directly)?"
			         * answer: the values are not saved in the actionPerformed function.
			         * 			if we pass a variable int i which is defined outside the lambda function (which is this (actionPerformed) function,
			         * 			into the viewPhoto function it will automatically resolve to 0 for some reason.
			         * 	
			         * upon pressing ok the program will repaint/ "refresh" the frame to show the changes
			         * */
	            	String name = expenses.getName();
	            	i = log.getExpenseIndex(name);
	            	System.out.println("deleting " + name);
	            	String prompt = "Confirm delete " + name + "?";
	            	int answer =  JOptionPane.showConfirmDialog(null, prompt, "Delete?",JOptionPane.YES_NO_OPTION);
	            	
	            	System.out.println(answer);
	            	if (answer == 0) { 
	            		log.deleteEntry(i, true);
	            		System.out.println("Deleted " + name);
	            		repaintFrame();
	            	} else 
	            		System.out.println("Cancelled Deleting " + name);
	            }
	        });
			expensePane.add(deleteExpense, JLayeredPane.MODAL_LAYER);
			i += 1;
		}
		if ( i < EXPENSEMAX) {
			JButton addIncome = new JButton();
			addIncome.setBounds(10, 320 + (i*(6+43)), 397, 43);
			addIncome.setIcon(addNewEntryIcon);
			addIncome.setHorizontalAlignment(JLabel.CENTER);
			addIncome.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	addExpenseEntries();
	            }
	        });
			expensePane.add(addIncome, JLayeredPane.MODAL_LAYER);
		}
		incomeAndExpensesPane.add(expensePane);
		i = 0;
		
	}
	/* creates the panel for the expenses
	 * uses a range based for loop using the Monthly_Entries variable
	 * pseudocode: 
	 * 		for every income in seq of incomes
	 * 			create incomePanel from income
	 * spacing of the expensePanels/incomePanels are handled using math.
	 * 	specifically the 320 + (i*(6+43)) in the 'y' parameter of setBounds.
	 * */
	public static void createIncomePane() {
		String price;
		final int INCOMEMAX = 3;
		i = 0;
		for (budget_entries incomes: log.getIncome()) {
			
			//	background
			JLabel incomePanel = new JLabel();
			incomePanel.setBounds(10, 726 + (i*(6+43)), 397, 43);
			incomePanel.setBackground(new Color(73,212,104));
			incomePanel.setOpaque(true);
			incomePane.add(incomePanel, JLayeredPane.PALETTE_LAYER);
			
			//	expense name
			JLabel nameIPanel = new JLabel();
			nameIPanel.setBounds(10+1, 726 + (i*(6+43)), 230-1, 43);
			nameIPanel.setText(incomes.getName());
			nameIPanel.setFont(arial);
			nameIPanel.setHorizontalAlignment(JLabel.CENTER);
			incomePane.add(nameIPanel, JLayeredPane.MODAL_LAYER);
			
			//	expense price
			price = String.valueOf(incomes.getAmount());
			JLabel pricePanelI = new JLabel();
			pricePanelI.setBounds(210, 726 + (i*(6+43)), 85, 43);
			pricePanelI.setText("RM" + price);
			pricePanelI.setFont(priceArial);
			incomePane.add(pricePanelI, JLayeredPane.MODAL_LAYER);
			
			//	edit button
			JButton editIncome = new JButton();
			editIncome.setBounds(320, 726 + (i*(6+43)), 45, 43);
			editIncome.setIcon(editIcon);
			editIncome.setHorizontalAlignment(JLabel.CENTER);
			editIncome.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					/*	button to delte the expense/income
					 *  if she asks "why do you need to store the the data in the local variables (instead of plugging it in directly)?"
			         * answer: the values are not saved in the actionPerformed function.
			         * 			if we pass a variable int i which is defined outside the lambda function (which is this (actionPerformed) function,
			         * 			into the editExpenseEntry function it will automatically resolve to 0 for some reason.
			         * 			thus editing the firt item instead of the 3th for example.
			         * */
					String name = incomes.getName();
					i = log.getIncomeIndex(name);
					editIncomeEntry(i);
				}
			});
			incomePane.add(editIncome, JLayeredPane.MODAL_LAYER);
			
			//	delete button
			JButton deleteIncome = new JButton();
			deleteIncome.setBounds(362, 726 + (i*(6+43)), 45, 43);
			deleteIncome.setIcon(deleteIcon);
			deleteIncome.setHorizontalAlignment(JLabel.CENTER);
			deleteIncome.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	/*	button to delete the expense/income.
	            	 *  if she asks "why do you need to store the the data in the local variables (instead of plugging it in directly)?"
			         * answer: the values are not saved in the actionPerformed function.
			         * 			if we pass a variable int i which is defined outside the lambda function (which is this (actionPerformed) function,
			         * 			into the viewPhoto function it will automatically resolve to 0 for some reason.
			         * 	
			         * upon pressing ok the program will repaint/ "refresh" the frame to show the changes
			         * */
	            	String name = incomes.getName();
	            	i = log.getIncomeIndex(name);
	            	System.out.println("deleting " + name);
	            	String prompt = "Confirm delete " + name + "?";
	            	int answer =  JOptionPane.showConfirmDialog(null, prompt, "Delete?",JOptionPane.YES_NO_OPTION);
	            	System.out.println(answer);
	            	
	            	if (answer == 0) { 
	            		log.deleteEntry(i, false);
	            		System.out.println("Deleted" + name);
	            		repaintFrame();
	            	} else 
	            		System.out.println("Cancelled Deleting " + name);
	            }
	        });
			incomePane.add(deleteIncome, JLayeredPane.MODAL_LAYER);
			i += 1;
		}
		//	add button
		if ( i < INCOMEMAX) {
			JButton addIncome = new JButton();
			addIncome.setBounds(10, 726 + (i*(6+43)), 397, 43);
			addIncome.setIcon(addNewEntryIcon);
			addIncome.setHorizontalAlignment(JLabel.CENTER);
			addIncome.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	addIncomeEntry();
	            }
	        });
			incomePane.add(addIncome, JLayeredPane.MODAL_LAYER);
		}
		
		incomeAndExpensesPane.add(incomePane);
		i = 0;
	}
	
	/* removes and the gallery panels in the frame, and adds creates new ones to represent the changes,
	 * updates the Monthly_Entry log by using log = plans.getLog();
	 * changes the moneyLabel's color, text / change the year/month*/
	public static void repaintFrame() {
    	log = plans.getLog();
		incomeAndExpensesPane.removeAll();
		incomeAndExpensesPane.revalidate();
		incomeAndExpensesPane.repaint();
    	if (log != null) {
    		
    		expensePane.removeAll();
        	incomePane.removeAll();
        	expensePane.revalidate();
        	expensePane.repaint();
        	incomePane.revalidate();
        	incomePane.repaint();
    		incomeTotal = log.getIncomeTotal();
        	expenseTotal = log.getExpenseTotal();
        	expenseTotalLabel.setText("RM" + expenseTotal);
    		incomeTotalLabel.setText("RM" + incomeTotal);
    		
        	grandTotal = incomeTotal - expenseTotal;
    		moneyLabel.setText("RM" + grandTotal);
    		moneyLabel.setHorizontalAlignment(JLabel.CENTER);
    		if (grandTotal < 0) {
    			moneyLabel.setBackground(new Color(226,135,135));
    		}else 
    			moneyLabel.setBackground(new Color(73,212,104));
    	}
    	createUI();
    	calendar.set(Calendar.MONTH, plans.getMonth());
    	yearLabel.setText(String.valueOf(plans.getYear()));
    	monthLabel.setText(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
	}
	
	/*	called when pressing either next or previous month button.*/
	public static void refreshAndChangeTime(boolean isAddition) {
		plans.changeMonth(isAddition);
		repaintFrame();
	}
	
	/*	creates an the static UI components such as the remaining money, the bar above the income and expense
	 *  and the view gallery button*/
	public static void createUI() {
		
		if (log != null) {
			
			log.calcEntries();
		    incomeTotal = log.getIncomeTotal();
		    expenseTotal = log.getExpenseTotal();
		    grandTotal = incomeTotal - expenseTotal;
		    
			//remaiing money
			remainingLabel.setBounds(83, 123, 264, 41);
			remainingLabel.setText("Remaining This Month:");
			remainingLabel.setFont(arial);
			remainingLabel.setHorizontalAlignment(JLabel.CENTER);
			remainingLabel.setBackground(new Color(217,217,217));
			remainingLabel.setForeground(Color.black);
			remainingLabel.setOpaque(true);
			
			
			moneyLabel.setBounds(83, 170, 264, 41);
			moneyLabel.setFont(new Font("Arial", Font.PLAIN, 22));
			moneyLabel.setText("RM" + grandTotal);
			moneyLabel.setHorizontalAlignment(JLabel.CENTER);
			if (grandTotal < 0) {
				moneyLabel.setBackground(new Color(226,135,135));
			}else 
				moneyLabel.setBackground(new Color(73,212,104));
			moneyLabel.setForeground(Color.black);
			moneyLabel.setOpaque(true);
			
			// vie imago
			ActionListener[] listeners = viewImagesButton.getActionListeners();
			for (ActionListener listener : listeners) {
				viewImagesButton.removeActionListener(listener);
			}
			
			viewImagesButton.setBounds(10, 223, 397, 36);
			viewImagesButton.setText("View Image Receipts");
			viewImagesButton.setFont(arial);
			viewImagesButton.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent  e) {
			    	/*	creates a gallery frame by passing the current month and year*/
			    	Gallery_Frame gallery = new Gallery_Frame(plans.getMonth(), plans.getYear());
			        //Photo_Frame photo = new Photo_Frame("records\\2023\\5\\receipt-template-us-band-blue-750px.png","Virtual Insanity",200.0f,30,5,2023);
			    }
			});
			viewImagesButton.setHorizontalAlignment(JButton.CENTER);
			viewImagesButton.setForeground(new Color(217,217,217));
			viewImagesButton.setForeground(Color.black);
			viewImagesButton.setOpaque(true);
			
			expenseLabel.setBounds(10, 276, 397, 27);
			expenseLabel.setText("Expenses");
			expenseLabel.setFont(arial);
			expenseLabel.setHorizontalAlignment(JLabel.LEFT);
			expenseLabel.setBackground(new Color(217,217,217));
			expenseLabel.setForeground(Color.black);
			expenseLabel.setOpaque(true);
			
			expenseTotalLabel.setBounds(280, 277, 125, 27);
			expenseTotalLabel.setText("RM" + expenseTotal);
			expenseTotalLabel.setFont(arial);
			
			
			expenseBG.setBounds(10 ,313, 397, 348);
			expenseBG.setBackground(new Color(217,217,217));
			expenseBG.setOpaque(true);
			
			if (log != null) {
				createExpensePane();
				createIncomePane();
			}
			
			incomeLabel.setBounds(10, 683, 397, 27);
			incomeLabel.setText("Incomes");
			incomeLabel.setFont(arial);
			incomeLabel.setHorizontalAlignment(JLabel.LEFT);
			incomeLabel.setBackground(new Color(217,217,217));
			incomeLabel.setForeground(Color.black);
			incomeLabel.setOpaque(true);
			
			incomeTotalLabel.setBounds(280, 684, 125, 27);		
			incomeTotalLabel.setText("RM" + incomeTotal);
			incomeTotalLabel.setFont(arial);
			
			incomeBG.setBounds(10 ,721, 397, 151);
			incomeBG.setBackground(new Color(217,217,217));
			incomeBG.setOpaque(true);	
			
			/* wher you actually make it work*/
			incomeAndExpensesPane.add(remainingLabel, JLayeredPane.DEFAULT_LAYER);
			incomeAndExpensesPane.add(moneyLabel,JLayeredPane.DEFAULT_LAYER);
			incomeAndExpensesPane.add(viewImagesButton,JLayeredPane.DEFAULT_LAYER);
			incomeAndExpensesPane.add(expenseLabel, JLayeredPane.DEFAULT_LAYER);
			incomeAndExpensesPane.add(expenseTotalLabel,JLayeredPane.PALETTE_LAYER);
			incomeAndExpensesPane.add(incomeLabel, JLayeredPane.DEFAULT_LAYER);
			incomeAndExpensesPane.add(incomeTotalLabel,JLayeredPane.PALETTE_LAYER);
			
		} else {
			
			ActionListener[] listeners = addNewBudgetPlan.getActionListeners();
			for (ActionListener listener : listeners) {
				addNewBudgetPlan.removeActionListener(listener);
			}
			addNewBudgetPlan.setBounds(83, 123, 264, 93);
			addNewBudgetPlan.setText("Add new Monthly Budget Plan");
			addNewBudgetPlan.setFont(priceArial);
			addNewBudgetPlan.setIcon(addNewBudgetIcon);
			addNewBudgetPlan.setHorizontalTextPosition(JButton.CENTER);
			addNewBudgetPlan.setVerticalTextPosition(JButton.BOTTOM);
			
			/*
			 * for some reason this specific button doesnt get removed from the incomeAndExpensesPane
			 * which results the button being pressed multiple times
			 * not sure why that happens
			 * FIXED
			 * */
			
			addNewBudgetPlan.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (e.getSource() == addNewBudgetPlan) {
						
						addNewMonthlyBudgetPlanner();
					}
				}
			});
			addNewBudgetPlan.setForeground(Color.black);
			addNewBudgetPlan.setOpaque(true);
			incomeAndExpensesPane.add(addNewBudgetPlan, JLayeredPane.DEFAULT_LAYER);
		}
	}
	
	/* called when pressing the add new budget planner button*/
	public static void addNewMonthlyBudgetPlanner() {
		plans.addPlanLog();
		log = plans.getLog();
		repaintFrame();
	}
	
	/*
	 * there has to be a better way to do this
	 * */
	
	/* is called when the + button under the expense is pressed.
	 * opens an JOptionPane with a 3 by 2 grid layout asking for name and amount
	 * if other is chosen in the combobox, the custom name field can b used.
     * */
	public static void addExpenseEntries() {
		String[] options = {"Food and Drink", "Transportation", "Rent","Personal Care", "Health Care", "Subscriptions", "Other"};
	    JComboBox<String> comboBox = new JComboBox<>(options);
	    JTextField amountField = new JTextField(10);
	    JTextField optionalField = new JTextField(10);
	    optionalField.setEnabled(false);
	    
	    
	    comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//sets the optional field to be usable
                String selectedOption = (String) comboBox.getSelectedItem();
                optionalField.setEnabled(selectedOption.equals("Other"));
            }
        });
	    
	    
	    JPanel addPanel = new JPanel();
	    addPanel.setLayout(new GridLayout(3, 2));
	    addPanel.add(new JLabel("Select option:"));
	    addPanel.add(comboBox);
	    addPanel.add(new JLabel("custom name:"));
	    addPanel.add(optionalField);
	    addPanel.add(new JLabel("Enter amount:"));
	    addPanel.add(amountField);
	    
	    
	    int result = JOptionPane.showConfirmDialog(null, addPanel, "Add Expense", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
	    	String selectedOption = (String) comboBox.getSelectedItem();
	    	String optionalText = optionalField.isEnabled() ? optionalField.getText() : "";
            float amount = Float.parseFloat(amountField.getText());

            System.out.println("Selected option: " + selectedOption);
            System.out.println("Amount entered: " + amount);
            System.out.println("Optional Text: " + optionalText);
            
            //adds the new expense into the database, and calls the repaintFrame to show the changes.
            log.addEntry(optionalField.isEnabled() ? optionalText : selectedOption , amount, true);
            repaintFrame();
	    }
	}
	/* is called when the + button under the income is pressed.
	 * opens an JOptionPane with a 3 by 2 grid layout asking for name and amount
	 * if other is chosen in the combobox, the custom name field can b used.
     * */
	public static void addIncomeEntry() {
		String[] options = {"Salary", "Loan/Borrows", "Investment","Other"};
	    JComboBox<String> comboBox = new JComboBox<>(options);
	    JTextField amountField = new JTextField(10);
	    JTextField optionalField = new JTextField(10);
	    optionalField.setEnabled(false);
	    
	    
	    comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//sets the optional field to be usable
                String selectedOption = (String) comboBox.getSelectedItem();
                optionalField.setEnabled(selectedOption.equals("Other"));
            }
        });
	    
	    
	    JPanel addPanel = new JPanel();
	    addPanel.setLayout(new GridLayout(3, 2));
	    addPanel.add(new JLabel("Select option:"));
	    addPanel.add(comboBox);
	    addPanel.add(new JLabel("Custom name:"));
	    addPanel.add(optionalField);
	    addPanel.add(new JLabel("Enter amount:"));
	    addPanel.add(amountField);
	    
	    int result = JOptionPane.showConfirmDialog(null, addPanel, "Add Income", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
	    	String selectedOption = (String) comboBox.getSelectedItem();
	    	String optionalText = optionalField.isEnabled() ? optionalField.getText() : "";
            float amount = Float.parseFloat(amountField.getText());

            System.out.println("Selected option: " + selectedOption);
            System.out.println("Amount entered: " + amount);
            System.out.println("Optional Text: " + optionalText);
            
          //adds the new income into the database, and calls the repaintFrame to show the changes.
            log.addEntry(optionalField.isEnabled() ? optionalText : selectedOption, amount, false);
            repaintFrame();
	    }
	}
	/* called when the pencil button next to the amount of expense is pressed.
	 * opens an JOptionPane with a 3 by 2 grid layout asking for name and amount
	 * if other is chosen in the combobox, the custom name field can b used.
     * */
	public static void editExpenseEntry(int index) {
		budget_entries temp = log.getSpecificExpense(index);
		
		String[] options = {"Food and Drink", "Transportation", "Rent","Personal Care", "Health Care", "Subscriptions", "Other"};
	    JComboBox<String> comboBox = new JComboBox<>(options);
	    JTextField amountField = new JTextField(10);
	    JTextField optionalField = new JTextField(10);
	    amountField.setText(String.valueOf(temp.getAmount()));
	    optionalField.setEnabled(false);
	    optionalField.setText(temp.getName());
	    
	    comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//sets the optional field to be usable
                String selectedOption = (String) comboBox.getSelectedItem();
                optionalField.setEnabled(selectedOption.equals("Other"));
            }
        });
	    
	    
	    JPanel addPanel = new JPanel();
	    addPanel.setLayout(new GridLayout(3, 2));
	    addPanel.add(new JLabel("Select option:"));
	    addPanel.add(comboBox);
	    addPanel.add(new JLabel("Custom name:"));
	    addPanel.add(optionalField);
	    addPanel.add(new JLabel("Enter new amount:"));
	    addPanel.add(amountField);
	    
	    
	    int result = JOptionPane.showConfirmDialog(null, addPanel, "Edit Expense", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
	    	String selectedOption = (String) comboBox.getSelectedItem();
	    	String optionalText = optionalField.isEnabled() ? optionalField.getText() : "";
            float amount = Float.parseFloat(amountField.getText());

            System.out.println("Selected option: " + selectedOption);
            System.out.println("Amount entered: " + amount);
            System.out.println("Optional Text: " + optionalText);
            
          //save the changes into the database, and calls the repaintFrame to show the changes.
            log.editEntry(index, amount, optionalField.isEnabled() ? optionalText : selectedOption , true);
            repaintFrame();
	    }
	}
	/* called when the pencil button next to the amount of income is pressed.
	 * opens an JOptionPane with a 3 by 2 grid layout asking for name and amount
	 * if other is chosen in the combobox, the custom name field can b used.
     * */
	public static void editIncomeEntry(int index) {
		budget_entries temp = log.getSpecificIncome(index);
		
		String[] options = {"Salary", "Loan/Borrows", "Investment","Other"};
	    JComboBox<String> comboBox = new JComboBox<>(options);
	    JTextField amountField = new JTextField(10);
	    JTextField optionalField = new JTextField(10);
	    amountField.setText(String.valueOf(temp.getAmount()));
	    optionalField.setEnabled(false);
	    optionalField.setText(temp.getName());
	    
	    
	    comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	//sets the optional field to be usable
                String selectedOption = (String) comboBox.getSelectedItem();
                optionalField.setEnabled(selectedOption.equals("Other"));
            }
        });
	    
	    
	    JPanel addPanel = new JPanel();
	    addPanel.setLayout(new GridLayout(3, 2));
	    addPanel.add(new JLabel("Select option:"));
	    addPanel.add(comboBox);
	    addPanel.add(new JLabel("Custom name:"));
	    addPanel.add(optionalField);
	    addPanel.add(new JLabel("Enter new amount:"));
	    addPanel.add(amountField);
	    
	    int result = JOptionPane.showConfirmDialog(null, addPanel, "Edit Income", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
	    	String selectedOption = (String) comboBox.getSelectedItem();
	    	String optionalText = optionalField.isEnabled() ? optionalField.getText() : "";
            float amount = Float.parseFloat(amountField.getText());

            System.out.println("Selected option: " + selectedOption);
            System.out.println("Amount entered: " + amount);
            System.out.println("Optional Text: " + optionalText);
            
          //save the changes into the database, and calls the repaintFrame to show the changes.
            log.editEntry(index, amount, optionalField.isEnabled() ? optionalText : selectedOption , false);
            repaintFrame();
	    }
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
