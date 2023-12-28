package prjoct;

public class Image implements Serializable_Image{
	/** written by:
	 * 		Ferdinand Morientes Anak Rantai
	 *  	77988
	 */
	private transient static final long serialVersionUID = 1L;
	private String description, directory;
    private int date , month, year;
    private float amount;
    /**
    Constructs an Image object with the specified directory, description, amount, date, month, and year.
     dir The directory of the image file.
     desc The description of the image.
     amount The amount associated with the image.
     date The date of the image.
     month The month of the image.
     year The year of the image.
     
     	whats a transient?: an expression that tells the program not to write the attribute into the database.
    */
    public Image(String dir, String desc, float amount, int date, int month, int year) {
    	
        this.description = desc;
        this.directory = dir;
        this.amount = amount;
        this.date = date;
        this.month = month;
        this.year = year;
    }
    
    public String getDesc() {
        return description;
    }
    
    public String getDir() {
        return directory;
    }
    
    public int getDate() {
        return date;
    }
    
    public int getMonth() {
    	return month;
    }
    public int getYear() {
    	return year;
    }
    public float getAmount() {
        return amount;
    }
    public void setDesc(String newDesc) {
        this.description = newDesc;
    }
    public void setAmount(float newAmount) {
        this.amount = newAmount;
    }
    public void setDate(int newDate) {
        this.date = newDate;
    }
}