package communication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import communication.Util;
public class FileConnector {
	private static String ProgramFilesLocation = null; 
	private static String ProjectName = "LucidHotel";
	public static void main(String args[]){

		// Create data to write to file
			String s = "hi";
			Object[] fileData = new Object[]{s};
		// Write to a file
		writeGameFiles("userDetails.dat",fileData);
		
		// Read a file
		Object[] fileReadData = readGameFiles("userDetails.dat");
			// Access using fileReadData[KEY]
		
		
	}
	public static Object[] readGameFiles(String Location){
		Object[] fileResponse = new Object[100];
		FileInputStream fin;
		ObjectInputStream ois;
		try {
			fin = new FileInputStream(ProgramFilesLocation + ProjectName +File.separator +Location);
			ois = new ObjectInputStream(fin);
			int counter = 0;
			while (fin.available() > 0){
				fileResponse[counter] = (Object)ois.readObject();
				counter += 1;
			}
			ois.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return fileResponse;
	}
	public static boolean writeGameFiles(String Location, Object[] object){
		boolean fileSuccess = false;
		ProgramFilesLocation = documentLocation();
		
		boolean findLocation = createFolder(ProjectName);
		if(findLocation){
			boolean findFile = createFile(ProjectName +File.separator +Location);
			if(findFile){
				writeDataToFile(ProjectName +File.separator +Location,object);
				fileSuccess = true;
			}
		}
		
		
		return fileSuccess;
	}
	private static boolean writeDataToFile(String fileName, Object[] inputObject){
		boolean fileSuccess = false;
		FileOutputStream fileStream = null;
		ObjectOutputStream outputStream = null;
		try {
			fileStream = new FileOutputStream(ProgramFilesLocation+ fileName);
			outputStream = new ObjectOutputStream(fileStream);
			for(int i = 0; i< inputObject.length; i++){
				outputStream.writeObject(inputObject[i]);
			}
			outputStream.close();
			fileSuccess = true;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileSuccess;
	}
	private static boolean createFile(String fileName){
		Boolean FileCreationSuccess = true;

		File f = new File(ProgramFilesLocation + fileName);
		if (!f.exists()) {
			
			try {
				f.createNewFile();
			} catch (IOException e) {
				Util.logC("Error Creating new file");
				FileCreationSuccess = false;
			}
		}
		
		return FileCreationSuccess;
		
	}
	private static boolean createFolder(String folderName){
		Boolean FileCreationSuccess = true;
		File theDir = new File(ProgramFilesLocation + folderName);
		  if (!theDir.exists()) {
		    System.out.println("creating directory: " + ProgramFilesLocation + folderName);
		    FileCreationSuccess = theDir.mkdir();  

		  }
		  return FileCreationSuccess;
	}
	public static String documentLocation(){
		String userDocuments = null;
		String OSSystem = System.getProperty("os.name");
		if(OSSystem.startsWith("Windows")) {
			try {
			    Process p =  Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
			    p.waitFor();
	
			    InputStream in = p.getInputStream();
			    byte[] b = new byte[in.available()];
			    in.read(b);
			    in.close();
	
			    userDocuments = new String(b);
			    userDocuments = userDocuments.split("\\s\\s+")[4];
			    userDocuments += "\\";
			} catch(Throwable t) {
			    t.printStackTrace();
			}
		} else if(OSSystem.startsWith("Mac")) {
			userDocuments = System.getProperty("user.home")+File.separator+"Documents" +File.separator;
		}
		else {
			
			Util.logC("System Not supported yet");
		}
		
		return userDocuments;
	}
	
}
