package communication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import communication.Util;
public class FileConnector {
	private static String ProgramFilesLocation = null; 
	public static void main(String args[]){
	
		loadGameFiles();
	}
	public static boolean loadGameFiles(){
		boolean fileSuccess = false;
		ProgramFilesLocation = documentLocation();
		
		boolean findLocation = createFolder("LucidHotel");
		if(findLocation){
			boolean findFile = createFile("LucidHotel" +File.separator +"userDetails.txt");
			if(findFile){
				String s = "hi";
				Object[] fileData = new Object[]{s};
				writeDataToFile("LucidHotel" +File.separator +"userDetails.txt",fileData);
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
			System.out.println("Writing data to " + ProgramFilesLocation + fileName);
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
		System.out.println("Created New file: " + ProgramFilesLocation + fileName);
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
