package prjoct;
import java.io.Serializable;

public abstract interface Serializable_Image extends Serializable{
	
	public abstract String getDesc();
	public abstract String getDir();
    public abstract int getDate();
    public abstract int getMonth();
    public abstract int getYear();
    public abstract float getAmount();
    public abstract void setAmount(float newAmount);
    public abstract void setDate(int newDate);
}
