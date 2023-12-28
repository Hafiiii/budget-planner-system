package prjoct;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class Monthly_Planner_Controller{
	/** written by:
	 * 		Imran Harriz bin Madzalan
	 *  	78048
	 */
	private ArrayList<Monthly_Entries> plan_logs = new ArrayList<Monthly_Entries>();
	private int current_year;
	private int current_month;
	
	Monthly_Planner_Controller() {}
	/**
	 * Constructs a Monthly_Planner_Controller object and initializes the current year and month.
	 * 	readUponConstruct Specifies whether to read data from a file upon construction.
	 */
	Monthly_Planner_Controller (boolean readUponConstruct){
		Calendar calendar = Calendar.getInstance();
		this.current_year = calendar.get(Calendar.YEAR);
		this.current_month = calendar.get(Calendar.MONTH);
		// debug feature.should be true during release. false is during debug.
		if (readUponConstruct)
			readFromFile();
		calculateAll();
	}
	/*  revised, arrayList version*/
	/*	getters*/
	public ArrayList<Monthly_Entries> getPlanLog(){
		return plan_logs;
	}
	public int getYear() {
		return current_year;
	}
	public int getMonth() {
		return current_month;
	}
	/*	returns the balance calculated from the current month's incometotal and expense total.
	 * */
	public float getCurrentBalance() {
		int index = getPlanIndex(current_year, current_month);
		if (index != -1){
			Monthly_Entries temp = plan_logs.get(index);
			float balance = temp.getIncomeTotal() - temp.getExpenseTotal();
			return balance;
		}
		return 0.0f;
	}
	/* gets the selected Monthly_Entries according the current month in the controller.
	 * returns the Monthly_Entries associated with that month and year,
	 * 	if none are associated, then return a null.*/
	public Monthly_Entries getLog() {
		int index = getPlanIndex(current_year, current_month);
		if (index == -1)
			return null;
		else 
			return plan_logs.get(index);
	}
	/**	Changes the current month based on the specified flag.
	 *  addition Flag indicating whether to increment or decrement the current month.
	 */
	public void changeMonth (boolean addition) {
		current_month = addition ? current_month + 1 : current_month - 1;
		if (current_month  < 0) {
			current_year -= 1;
			current_month = 11;
		}
		else if (current_month > 11) {
			current_year += 1;
			current_month = 0;
		}
	}
	/*	when running this thing, do call the write to file function*/
	public void addPlanLog() {
		boolean added = false;
		for (Monthly_Entries logs : plan_logs) {
			if (logs.isAssociated(current_year, current_month)) {
				added = true;
				break;
			}
		}
		if (!added) {
			System.out.println("creating new monthly budget planner for month " + getMonth());
			Monthly_Entries inLog = new Monthly_Entries(current_year, current_month);
			plan_logs.add(inLog);
		} else {
			System.out.println("already added for month "+ getMonth());
		}
		
	}
	/*	deletes the expense by removing it by the index given
	 * 	if isExpense == true, then remove from the Monthly_Entries arraylist.*/
	public void removePlanLog(int index) {
		plan_logs.remove(index);
	}
	/*	Edits the plan log for the specified year and month with the provided Monthly_Entries object.*/
	public void editPlanLog(int year, int month, Monthly_Entries inLog) {
		try {
			int index = getPlanIndex(year,month);
			if (index == -1) 
				throw new IllegalArgumentException("No plan found for the specified year and month");
			else 
				plan_logs.set(index, inLog);
			}catch (Exception e) {
				e.printStackTrace();
		}
	}
	/**
	 * Reads plan logs from a file and populates the plan_logs list.
	 * The file name is set as "plans.bin".
	 */
	@SuppressWarnings("unchecked")
	public void readFromFile() {
		final String filename = "plans";
		try (ObjectInputStream in = new ObjectInputStream( new FileInputStream(filename + ".bin"))) {
			plan_logs = (ArrayList<Monthly_Entries>) in.readObject();
		} catch (EOFException e) {
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Writes the plan logs to a file.
	 * The file name is set as "plans.bin".
	 * return 1 if the write operation was successful, -1 otherwise.
	 */
	public int writeToFile() {
		final String filename = "plans";
		try {
			FileOutputStream fileOut = new FileOutputStream(filename + ".bin");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(plan_logs);
			out.close();
			fileOut.close();
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	/*	misc functions  */
	/**
	 * Returns the index of the plan log associated with the specified year and month.
	 * param year The year of the plan log.
	 * param month The month of the plan log.
	 * return The index of the plan log, or -1 if not found.
	 */
	public int getPlanIndex (int year , int month) {
		int index = 0;
		for (Monthly_Entries log : plan_logs) {
			if (log.isAssociated(year, month))
				return index;
			index += 1;
		}
		return -1;
	}
	/**
	 * Calculates the entries for all plan logs.
	 * Call this function to calculate the entries during class initialization.
	 */
	public void calculateAll() {
		for (Monthly_Entries logs :plan_logs) {
			logs.calcEntries();
		}
	}
}
