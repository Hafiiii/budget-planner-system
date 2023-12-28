package prjoct;
import java.io.Serializable;

public class budget_entries implements Serializable{
	/** written by Ferdinand Morientes Anak Rantai
	 *  77988
	 */
	
	 private transient static final long serialVersionUID = 1L;
	 private int type;
	 private String name;
	 private float amount;
	 
	 public budget_entries() {}
	 
	 public budget_entries( int type, String name, float amount) {
		 this.type = type;
		 this.name = name;
		 this.amount = amount;
	 }
	 
	 public int getType() {
		 return type;
	 }
	 public String getName() {
		 return name;
	 }
	 public float getAmount() {
		 return amount;
	 }
	 public void setName (String newName) {
		 this.name = newName;
	 }
	 public void setAmount(float newAmount) {
		 this.amount = newAmount;
	 }
}