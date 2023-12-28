package prjoct;
import java.io.Serializable;

public abstract interface Serializable_Monthly_Entries extends Serializable {
	/* Getters */
	public int getYear();
	public int getMonth();
	public float getIncomeTotal(); 
	public float getExpenseTotal();
	/* CEDs */
	public void addEntry(String name, float amount, boolean isIncome);
	public void editEntry(int index, float newAmount, String newName, boolean isIncome);
	public void deleteEntry (int index, boolean isIncome);
	/* misc function(s)*/
	public void calcEntries ();
}
