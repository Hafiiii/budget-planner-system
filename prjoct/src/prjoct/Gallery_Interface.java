package prjoct;

import java.util.ArrayList;

public abstract interface Gallery_Interface{
	//public Gallery_Controller();
	/*	getters*/
    public abstract int getYear();
    public abstract int getMonth();
    /*	CRUD operations*/
    	// adds to the ArrayList<Image>.
    public abstract void addEntry(String directory ,String description, float amount, int date);
    
    public abstract void editEntry(int index, String desc, float amount, int date);
    
    public abstract void deleteEntry(int index);
    
    public abstract int writeToFile();
    
    public abstract void readFromFile();
    
    public abstract String addImageToDirectory(String filePath);
    
    public abstract void deleteImageFromDirectory(String fileDir);
    
    public abstract int getImageIndex(String fileDir);
    public ArrayList<Image> getImagesInMonth();
}
