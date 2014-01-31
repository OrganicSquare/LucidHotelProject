package communication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import communication.Util;
public class FileConnector {
	private static String ProgramFilesLocation = null; 
	public static void main(String args[]){
	
	
	}
	public static boolean loadGameFiles(){
		ProgramFilesLocation = documentLocation();
		
		boolean findLocation = createFolder("GameJJVassd");
		if(findLocation){
			boolean findFile = createFile("userDetails.txt");
			if(findFile){
				
				
			}
		}
		
		
		return false;
	}
	private static boolean writeDataToFile(String content, String fileName){
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedWriter bw = new BufferedWriter(fw);
		try {
			bw.write(content);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return false;
	}
	private static boolean createFile(String fileName){
		Boolean FileCreationSuccess = true;

		File f = new File(ProgramFilesLocation + File.separator + fileName);
		if (!f.exists()) {
			f.mkdirs(); 
			try {
				f.createNewFile();
			} catch (IOException e) {
				Util.logC("Error Creating new file");
				FileCreationSuccess = false;
			}
		} else {
			FileCreationSuccess = false;
		}
		
		return FileCreationSuccess;
		
	}
	private static boolean createFolder(String folderName){
		Boolean FileCreationSuccess = true;
		File theDir = new File(ProgramFilesLocation + folderName);
		ProgramFilesLocation += ProgramFilesLocation + File.separator;
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
