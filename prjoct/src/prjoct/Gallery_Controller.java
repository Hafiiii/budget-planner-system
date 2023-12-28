package prjoct;
import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Gallery_Controller implements Gallery_Interface{
	/** written by:
	 * 		Nur Hafizah binti Ramli
	 *  	80609
	 *  helped by:
	 *  	Dayang Nur Ezaah Adibah binti Abdul Jalil @ Awang Abdul Jalil
	 *  	77921
	 */
	
	private int current_year;
	private int current_month;
	private ArrayList<Image> images = new ArrayList<Image>();
	
	public Gallery_Controller() {
		readFromFile();
	}
	
    public Gallery_Controller(int year , int month) {
    	current_year = year;
    	current_month = month;
    	readFromFile();//the image data is loaded and available immediately after creating a new Gallery_Controller object.
    }
    /*	getters	*/
    public int getYear() {
		return current_year;
    }
    public int getMonth() {
    	return current_month;
    }
    public ArrayList<Image> getAll(){
    	return images;//returns the entire images list
    }
    public Image getSpecific(int index) {
    	return images.get(index);//returns a specific Image object from the images list based on the provided index.
    }

	//Adds a new entry to the gallery with the provided image file path, description, amount, and date. 
	//It also adds the image to the appropriate directory and updates the ArrayList
    public void addEntry(String filePath, String description, float amount, int date) {
    	try {//This line starts a try block, which is used to handle exceptions that may occur within the code block.
    			filePath = addImageToDirectory(filePath);//This line calls the method addImageToDirectory(moves the image from the original directory to the destination directory) 
														//and passes the filePath as an argument. 
														//The returned value from addImageToDirectory is then assigned back to the filePath variable.
    			System.out.println(filePath);//This line prints the value of filePath to the console. 
											//It is used for debugging purposes to display the path of the added image.
    			if (!filePath.equals(""))//This line checks if the filePath is not an empty string. 
										//It is used to verify that the image was successfully added to the directory.
    			{
    				images.add(new Image(filePath, description, amount, date, current_month, current_year));//This line creates a new Image object and adds it to the images collection. 
																											//The Image object is instantiated with the provided filePath, description, amount, date, current_month, and current_year.
        			System.out.println("image added successfully");//This line prints a success message to the console, indicating that the image was added successfully.
    			} else {//This line is the start of the else block, which is executed if the filePath is an empty string.
    				throw new IOException("add Entry Failed");//This line throws an IOException with a descriptive message indicating that the addition of the image failed.
    			}
    			
    	}catch (Exception e) {//This line starts the catch block and specifies that it catches any exception of type 
								//Exception (including its subclasses) and assigns it to the variable e.
			e.printStackTrace();//This line prints the stack trace of the caught exception, 
								//which provides information about the exception's type, message, and the sequence of method calls that led to the exception.
    	}
    }

	//Edits an existing entry in the gallery at the given index with the provided description, amount, and date.
	//esponsible for modifying an existing image entry in the images collection. 
	//It retrieves the Image object at the specified index, updates its properties (amount, date, and desc) with the provided values, and then replaces the original object in the collection with the modified object.
    public void editEntry(int index, String desc, float amount, int date) {
    	Image temp = images.get(index);//his line retrieves the Image object at the specified index from the images collection and 
									//assigns it to a temporary variable named temp. The get method is used to retrieve the element at the given index.
    	temp.setAmount(amount);//This line calls the setAmount method of the temp object and sets its amount property to the provided amount value.
    	temp.setDate(date);
    	temp.setDesc(desc);
    	images.set(index, temp);//This line updates the Image object at the specified index in the images collection with the modified temp object. 
								//The set method is used to replace the element at the given index.
    }

	//Deletes an entry from the gallery at the given index. 
	//deleting an image entry from the images collection.
	//It also removes the image file from the directory and updates the ArrayList.
	//retrieves the Image object at the specified index, obtains the directory of the associated image file, and 
	//then calls the deleteImageFromDirectory method to delete the image file from the directory.
    /*	wrapper function to delete both the image from the directory and the database*/
    public void deleteEntry(int index) {
    	Image temp = images.get(index);//This line retrieves the Image object at the specified index from the images collection and 
									//assigns it to a temporary variable named temp. The get method is used to retrieve the element at the given index.
    	deleteImageFromDirectory(temp.getDir());//This line calls the deleteImageFromDirectory method and 
												//passes the dir property of the temp object as an argument. 
												//The dir property represents the directory of the image file associated with the Image object.
    }
	//Writes the ArrayList of images to a binary file named "images.bin".
    /*	saves the arraylist of images into the images.bin
     * 	Image class implments the Serializable interface for it to be writable as a binary file.*/
    public int writeToFile() {
    	final String filename = "images";//declares a constant variable filename and assigns it the value "images". 
										//This variable represents the name of the file to which the images collection will be written.
		try {//This line begins a try block to handle any potential exceptions that may occur during the file writing process.
			FileOutputStream fileOut = new FileOutputStream(filename + ".bin");//This line creates a FileOutputStream object named fileOut and associates it with the file specified by filename + ".bin". 
																				//The FileOutputStream is responsible for writing the binary data to the file.
			ObjectOutputStream out = new ObjectOutputStream(fileOut);//This line creates an ObjectOutputStream object named out and associates it with the FileOutputStream object fileOut. 
																	//The ObjectOutputStream is responsible for writing the images collection as an object to the file.
			out.writeObject(images);//This line writes the images collection to the file using the writeObject method of the ObjectOutputStream. 
									//The writeObject method serializes the object and writes its binary representation to the file.
			out.close();//This line closes the ObjectOutputStream to release any system resources associated with it.
			fileOut.close();//This line closes the FileOutputStream to release any system resources associated with it.
			return 1;//This line indicates a successful write operation by returning the value 1.
		} catch (IOException e) {
			e.printStackTrace();//This line prints the stack trace of the exception, which provides information about the exception and its origin.
		}
		return -1;//This line is reached if an exception occurs during the file writing process. It returns the value -1 to indicate a failure in writing the file.
    }

	//Reads the ArrayList of images from the "images.bin" binary file.
    /**
	 * Reads plan logs from a file and populates the images list.
	 * The file name is set as "images.bin".
	 * "why is it surpressed?"
	 * 		answer: casting Object to an arraylist-of-image object isnt exactly safe nor sane.
	 * tell the compiler to ignore a specific warning related to unchecked or unsafe operations. 
	 * In this case, the warning is about casting an Object to an ArrayList<Image> object.
	 * the compiler cannot guarantee this because the exact type of the object is not known at compile-time.
	 * I know there is a risk, but I'm confident that the object being read from the file is actually an ArrayList<Image>
	 * The warning is suppressed to avoid cluttering the code with unnecessary warnings.
	 */
    @SuppressWarnings("unchecked")
    public void readFromFile() {
    	final String filename = "images";//This line declares a constant variable filename and assigns it the value "images". 
										//This variable represents the name of the file from which the images collection will be read.
		try (ObjectInputStream in = new ObjectInputStream( new FileInputStream(filename + ".bin"))) {//This line starts a try-with-resources block. 
																									//It creates an ObjectInputStream named in and associates it with a 
																									//FileInputStream object that reads from the file specified by filename + ".bin". 
																									//The try-with-resources statement ensures that the ObjectInputStream is properly closed at the end of the block.
			images = (ArrayList<Image>) in.readObject();//This line reads the object stored in the file using the readObject method of the ObjectInputStream. 
														//It casts the deserialized object to an ArrayList<Image> and assigns it to the images variable. 
														//The deserialization process reconstructs the ArrayList<Image> object from the binary data stored in the file.
		} catch (EOFException e) {//This line catches an EOFException, which is thrown when the end of the file is reached during the reading process. 
								//It is expected in this case and does not require any specific handling.
		} catch (IOException | ClassNotFoundException e) {//This line catches both IOException and ClassNotFoundException exceptions. 
														//IOException is thrown when there is an error during the reading process, such as a file not found or an I/O error. 
														//ClassNotFoundException is thrown when the class of a serialized object cannot be found during deserialization.
			e.printStackTrace();//This line prints the stack trace of the exception, which provides information about the exception and its origin. 
								//It helps in debugging and understanding the cause of the exception.
		}
    }

	//Moves the image file from the original directory to the appropriate destination directory based on the current year and month. 
	//It returns the new file path.
    /*	subfunction that moves the image from the original directory to the destination directory
     *  the images are organised by year/month/image.jpg.
     *  if successful, returns the destination filepath to be stored in the database.*/
    public String addImageToDirectory(String filePath) {
    	try {//his line starts a try block, which is used to enclose a section of code that may throw exceptions.
    	    File inputFile = new File(filePath);//This line creates a File object inputFile with the provided filePath. 
												//It represents the image file to be processed.

    	    // Open the image and throw an error if fails
    	    BufferedImage image = ImageIO.read(inputFile);//This line uses the ImageIO class to read the image file specified by inputFile and stores it in a BufferedImage object named image. 
														//If the image file cannot be read, an IOException is thrown.
    	    if (image == null) {//This line checks if the image is null, which indicates that the image file failed to open or cannot be read properly.
    	        throw new IOException("Failed to open the image file.");//This line throws an IOException with the message "Failed to open the image file." to indicate that the image file could not be opened.
    	    }

    	    // If it's a PNG, reformat it to JPEG
    	    if (filePath.toLowerCase().endsWith(".png")) {//This line checks if the filePath ends with the extension ".png" by converting it to lowercase and using the endsWith method.
    	        ImageIO.write(image, "jpg", new File(filePath));//This line converts the image from PNG to JPEG format and writes it to a new file with the same filePath but with the ".png" extension replaced by ".jpg".
    	    }

    	    // Create the destination folder
    	    String destinationFolder = "receipts" + File.separator + current_year + File.separator + current_month;//This line creates a String variable destinationFolder that represents the destination folder path where the image will be moved. 
																												//It is constructed using the "receipts" directory, the File.separator constant (platform-specific file separator), the current year, and the current month.
    	    File folder = new File(destinationFolder);//This line creates a File object folder representing the destination folder specified by destinationFolder.
    	    if (!folder.exists()) {//This line checks if the destination folder does not exist.
    	        folder.mkdirs();//This line creates the destination folder and any necessary parent directories using the mkdirs method of the File class.
    	    }

    	    // Move the file to the destination folder
    	    File destinationFile = new File(folder, inputFile.getName());//his line creates a File object destinationFile representing the 
																		//destination file path by combining the folder path and the name of the original inputFile.
    	    inputFile.renameTo(destinationFile);//This line renames/moves the original inputFile to the destinationFile location.
    	    filePath = destinationFile.getAbsolutePath();//This line updates the filePath variable with the absolute path of the destinationFile.
    	} catch (IOException e) {//This line catches an IOException if an error occurs during the image processing or file manipulation.
    	    e.printStackTrace();//This line prints the stack trace of the exception, which provides information about the exception and its origin. 
								//It helps in debugging and understanding the cause of the exception.
    	    filePath ="";//This line sets filePath to an empty string to indicate that an error occurred and the image could not be processed.
    	}
    	 return filePath;//This line returns the updated filePath. It represents the absolute path of the image file after it has
    }

	//Deletes the image file specified by the file directory. 
	//It also removes the corresponding image object from the ArrayList.
    /*	takes the image directory of the file to be deleted
     * 	and... deleted the file
     * 	as well as the object in the database.*/
	//he method finds the image in the images list based on the fileDir, 
	//removes it from the list, and attempts to delete the corresponding image file from t he file system. 
	//The method provides feedback messages indicating whether the deletion was successful or not.
    public void deleteImageFromDirectory(String fileDir) {
    	int index = getImageIndex(fileDir);//his line calls the getImageIndex method to get the index of the image in the images list based on the provided fileDir. 
										//It assigns the index value to the index variable.
    	if (index != -1) {//This line checks if the index is not equal to -1, which indicates that the image was found in the images list.
    		File imageFile = new File(fileDir);//This line creates a File object imageFile representing the image file specified by fileDir.
    		images.remove(index);//This line removes the image at the index from the images list using the remove method.
            if (imageFile.exists()) {//This line checks if the image file exists in the file system.
                if (imageFile.delete()) {//This line attempts to delete the image file using the delete method of the File class.
                    System.out.println("Image deleted successfully: " + fileDir);//This line prints a success message indicating that the image was deleted successfully, 
																				//along with the fileDir representing the path of the deleted image file.
                } else {//This line is the beginning of an else block, indicating that the image file deletion failed.
                    System.out.println("Failed to delete the image: " + fileDir);//This line prints an error message indicating that the image deletion failed,
																				// along with the fileDir representing the path of the image file that could not be deleted.
                }
            } else {//This line is the beginning of an else block, indicating that the image was not found in the images list.
                System.out.println("Image file does not exist: " + fileDir);//prints a message indicating that the image file does not exist, 
																			//along with the fileDir representing the path of the non-existing image file.
            }
    	}
    }

	//Returns an ArrayList of images that have the same month and year as the current controller.
    /* returns a temporary array with Images with the same month as the controller*/
	//checks each image for the specified month and year, and adds the matching images to a temporary list (temp). 
	//Finally, it returns the temporary list containing the images that meet the specified condition.
    public ArrayList<Image> getImagesInMonth(){
    	ArrayList<Image> temp = new ArrayList<Image>();//creates a new ArrayList called temp to store the images that meet the specified condition.
    	for (Image i : images) {//starts a loop that iterates over each Image object i in the images list.
    		// if same date is found and fileName
    		if (i.getMonth() == current_month && i.getYear() == current_year) {//checks if the month of the current Image object i is equal to current_month and the year is equal to current_year.
    			temp.add(i);//adds the current Image object i to the temp list because it meets the specified condition.
    		}
    	}
    	
    	return temp.isEmpty() ? null : temp;//returns the temp list. If the temp list is empty, it returns null, otherwise, 
											//it returns the list containing the images that meet the specified condition.
    }

	//Returns the index of an image in the ArrayList based on its file directory.
    /*	gets the image index using the fileDir. why? because the filedir is unique.
     * 	drawbacks: major performance loss
     * */
	//hecks each image for a match based on the specified month, year, and directory, and 
	//returns the index of the matching image if found. If no match is found, it returns -1.
    public int getImageIndex(String fileDir) {
    	int index = 0;//initializes a variable index with a value of 0. This variable will be used to keep track of the index of the matching image.
    	for (Image i : images) {//starts a loop that iterates over each Image object i in the images list.
    		// if same date is found and fileName
    		if (i.getMonth() == current_month && i.getYear() == current_year && i.getDir() == fileDir) {//checks if the month of the current Image object i is equal to current_month, the year is equal to current_year, and 
																									//the directory (fileDir) of the image matches the provided fileDir.
    			return index;//returns the value of the index variable, indicating the index of the matching image in the images list.
    		}
    		index += 1;//increments the index variable by 1 after each iteration of the loop to move to the next index in the images list.
    	}
    	return -1;//This line is reached if no matching image is found in the loop. It returns -1, 
				//indicating that the image with the provided directory (fileDir) was not found in the images list.
    }
}
