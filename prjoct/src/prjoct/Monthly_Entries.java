package prjoct;
import java.util.ArrayList;

public class Monthly_Entries implements Serializable_Monthly_Entries{
	/** written by:
	 * 		Imran Harriz bin Madzalan
	 *  	78048
	 */
	private transient static final long serialVersionUID = 1L;
	private int assoc_month;
	private int assoc_year;
	private transient float total_income;
	private transient float total_expense;
	//	whats a transient?: an expression that tells the program not to write the attribute into the database.
	private ArrayList<budget_entries> income = new ArrayList<budget_entries>();
	private ArrayList<budget_entries> expense = new ArrayList<budget_entries>();
	
	// default constructor
	public Monthly_Entries () {}

	public Monthly_Entries (int year, int month) {
		this.assoc_year = year;
		this.assoc_month = month;
	}
	/* getters */
	public int getYear() {
		return assoc_year;
	}
	public int getMonth() {
		return assoc_month;
	}
	public boolean isAssociated(int year, int month) {
		if (this.assoc_year == year && this.assoc_month == month)
			return true;
		return false;
	}
	public float getIncomeTotal() {
		return total_income;
	}
	public float getExpenseTotal() {
		return total_expense;
	}
	public ArrayList<budget_entries> getIncome() {
		return income;
	}
	public ArrayList<budget_entries> getExpense() {
		return expense;
	}
	public budget_entries getSpecificIncome(int index) {
		return income.get(index);
	}
	public budget_entries getSpecificExpense(int index) {
		return expense.get(index);
	}
	/* CEDs */
	
	// why does the isExpense boolean exist? because... i(mran) wanted to reduce lines
	//	by sacrificing readability. i apologize.
	
	/* takes the name, and amount from the user
	 * 	creates a temporary budget entry and depending on the isExpense boolean,
	 * 	adds it to the expense arraylist, else to the income arrayList*/
	public void addEntry(String name, float amount, boolean isExpense) {
		final int type = isExpense ? 1 : 2;
		budget_entries entry = new budget_entries(type,name,amount);
		if (isExpense)
			expense.add(entry);
		else
			income.add(entry);
	}
	/*	edits the amount and name by the index.
	 * 	if isExpense == true, then edit from the expense arraylist
	 * 	else edit it from the income arraylist 
	 * 	uses the arrayList's set() method*/
	public void editEntry(int index, float newAmount, String newName, boolean isExpense) {
		budget_entries temp = isExpense ? expense.get(index) : income.get(index); 
		temp.setAmount(newAmount);
		temp.setName(newName);
		if (isExpense)
			expense.set(index, temp);
		else
			income.set(index, temp);
	}
	/*	deletes the expense by removing it by the index given
	 * 	if isExpense == true, then remove from the expense arraylist
	 * 	else remove it from the income arraylist 
	 * 	uses the arraylist's remove() method.*/
	public void deleteEntry (int index, boolean isExpense) {
		if (isExpense)
			expense.remove(index);
		else
			income.remove(index);
	}
	/* misc functons*/
	/*	calculates all of the total income and total expense by using a range based for loop
	 * 	and save the amount into the total_income and total_expense attribute*/
	public void calcEntries () {
		total_income = 0;
		total_expense = 0;
		for (budget_entries entry : this.income) {
			this.total_income += entry.getAmount();
		}
		for (budget_entries entry : this.expense) {
			total_expense += entry.getAmount();
		}
	}
	/*	gets the index of the income in the expense arrayList by comparing the name*/
	public int getExpenseIndex (String name) {
		int index = 0;
		for (budget_entries expenses : expense) {
			if (name.equals(expenses.getName()))
				return index;
			index += 1;
		}
		return -1;
	}
	/*	gets the index of the income in the income arrayList by comparing the name*/
	public int getIncomeIndex (String name) {
		int index = 0;
		for (budget_entries incomes : income) {
			if (name.equals(incomes.getName()))
				return index;
			index += 1;
		}
		return -1;
	}
}
