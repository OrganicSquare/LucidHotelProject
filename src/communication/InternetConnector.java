package communication;

import game.Main;

import java.io.BufferedReader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.fluent.Async;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;

import communication.Util;
import java.util.Timer;
import java.util.TimerTask;
// FROM MODELLOADER
public class InternetConnector {
	private final static String WEB_SERVER = "http://benallen.info/projects/GameJJB/bin/dataHandler.php";
	private static Integer WEB_STATUS = 0; 
	private final static int ITERATION_NUMBER = 1000;
    public static String JSONRawMain;
	 static Timer timer;
    
	public static void main(String args[])throws IOException{
		// Initial Vars
		 // Do you want the system to log data?
		WEB_STATUS = 1; // This allows reasoning for failure following code below:
		/*
		 * WEB_STATUS:
		 * 0 - Undefined
		 * 1 - All OK
		 * 2 - Database Error
		 * 3 - HTTP Error
		 * 4 - Algorithmic Error
		 * 5 - Conversion Error
		 * 6 - IO Exception
		 */
		
		// Available Functions
		
		// Will Add User with specified details
		//boolean addUserConfirmation = addUser("jamie","12345");
			// If false then the user must exist already
		
		// Will confirm login process
		//boolean loginConfirmation = isLoginOK("ben","1234");
			// If false then the user has incorrect details
		
		// Will download user information
		//Map<String, Object> downloadedUserInformation = downloadUserInformation("ben");
			// How to access the returns:
			// int id = (int) downloadedUserInformation.get("uId");
			// String skin = (String) downloadedUserInformation.get("uSkin");
		
		// Will upload user Position
		//boolean sendUserPositionConfirmation = sendUserPosition(3,new float[]{1.0f,2.0f,3.0f,5.0f,0});
			// If false the connection could not be made
		
		// Will Download another user position
		//Map<String, Object> downloadUserPositionConfirmation = downloadUserPosition(6, false);
			// How to access the returns:
		//	 Float xPos = (Float) downloadUserPositionConfirmation.get("xPos");
			// Boolean isMoving = (Boolean) downloadUserPositionConfirmation.get("isMoving");
		//Util.logC(xPos.toString());
		
		
		
		
		// Will log a user out - should be called when player closes program
		// boolean logUserOutOK = logoutUser(3);
		
		
		// Lists usernames, ids and skins of all players that are online OTHER THAN THE USERS ID
		//Map<Integer, Map<String, Object>> playersOnline = findOnlinePlayers(uId);
			// You must make a Map to access a single players details
			// Like so: Map<String, Object> playersDetails = playersOnline.get(NUMBER_HERE);
			//	- where NUMBER_HERE = the player number i.e 0, 1 etc 
			// Then access the individual players details like so:
			// String userName = playersDetails.get("uName").toString();
			// uId, uName and uSkin are available
		
	}
	public static void downloadAllUserPositions(int uId){
		String[] urlName, urlAttribute = null;
		String JSONRaw = null;		
		urlName = new String[]{"target", "uId"};
		urlAttribute = new String[]{"findOnlinePlayers",Integer.toString(uId)};		
		excutePostAsync(urlName,urlAttribute, false);
	}
	public static Map<Integer, Map<String, Object>> decodeUserPositions() {
		Map<Integer, Map<String, Object>> ComputationResponse = new HashMap<Integer, Map<String, Object>>();
		String JSONRaw = JSONRawMain;
		if(JSONRaw.trim().equals("false")){
			System.out.println("Warning: There are no users currently online");
			
			return ComputationResponse;
		} else {
			if(!JSONRaw.equals("") && !JSONRaw.equals("true")){
			Object obj=JSONValue.parse(JSONRaw);
			JSONArray array=(JSONArray)obj;
				for (int i = 0; i< array.size(); i++){
					Map<String, Object> UserDetails = new HashMap<String, Object>();
					JSONObject playerDetails=(JSONObject)array.get(i);			
					try {
						UserDetails.put("uId", Integer.parseInt(playerDetails.get("id").toString()));
						UserDetails.put("uName", playerDetails.get("userName").toString());
						UserDetails.put("uSkin", playerDetails.get("skin").toString());
						UserDetails.put("yPos", Float.parseFloat(playerDetails.get("yPos").toString()));
						UserDetails.put("xPos", Float.parseFloat(playerDetails.get("xPos").toString()));
						UserDetails.put("zPos", Float.parseFloat(playerDetails.get("zPos").toString()));
						UserDetails.put("rotY", Float.parseFloat(playerDetails.get("rotY").toString()));
						UserDetails.put("isMoving", Boolean.parseBoolean(playerDetails.get("yPos").toString()));
					} catch (Exception ex) {
						System.out.println("Error converting the user positions w/ data : " + JSONRawMain);				
					}
					ComputationResponse.put(i, UserDetails);
				}	
			}
		}
		
		return ComputationResponse;
	}
	public static boolean logoutUser(Integer userId){
		String ComputationResponse = null;
		Boolean ReturnResponse = true;
		String[] urlName, urlAttribute = null;
		
		urlName = new String[]{"target","userId"};
		urlAttribute = new String[]{"logUserOut",String.valueOf(userId)};
		
		ComputationResponse = excutePost(urlName,urlAttribute);
				
		if(!ComputationResponse.equals("true")){
			ReturnResponse = false;
		} 
		return ReturnResponse;
	}
	public static void downloadUserInformation(String userName){	
		JSONRawMain = "";
		excutePostAsync(new String[]{"target","userName"}, new String[]{"userInformation",userName}, false);		
	}	
	public static Map<String, Object> decodeUserInformation(){
		Map<String, Object> ComputationResponse = new HashMap<String, Object>();
		String JSONRaw = JSONRawMain;
		Object obj=JSONValue.parse(JSONRaw);
		JSONRawMain = "";
		JSONArray array=(JSONArray)obj;
		JSONObject playerDetails=(JSONObject)array.get(0);			
		
		ComputationResponse.put("uId", Integer.parseInt(playerDetails.get("uId").toString()));
		ComputationResponse.put("uSkin", playerDetails.get("skin").toString());
		ComputationResponse.put("xPos", Float.parseFloat(playerDetails.get("xPos").toString()));
		ComputationResponse.put("yPos", Float.parseFloat(playerDetails.get("yPos").toString()));
		ComputationResponse.put("zPos", Float.parseFloat(playerDetails.get("zPos").toString()));
		ComputationResponse.put("rotY", Float.parseFloat(playerDetails.get("rotY").toString()));		
		return ComputationResponse;
	}
	
	
	public static void sendUserPosition(int uId, float[] positionData){
		String[] urlName, urlAttribute = null;
		
		urlName = new String[]{"target","uId","xPos","yPos","zPos","rotY","isMoving"};
		urlAttribute = new String[]{"positionUpdater",String.valueOf(uId),String.valueOf(positionData[0]),String.valueOf(positionData[1]),String.valueOf(positionData[2]),String.valueOf(positionData[3]),String.valueOf(positionData[4])};
		
		excutePostAsync(urlName,urlAttribute, true);
				
			
	}
	/*
	public static Map<String, Object> downloadUserPosition(Integer userId, Boolean doAsync){
		Map<String, Object> ComputationResponse = new HashMap<String, Object>();
		String JSONRaw = null;		
		
		if(!doAsync){
			JSONRaw = excutePost(new String[]{"target","uTargetId"}, new String[]{"positionDownloader",String.valueOf(userId)});
			
			Object obj=JSONValue.parse(JSONRaw);
			JSONArray array=(JSONArray)obj;
			JSONObject playerDetails=(JSONObject)array.get(0);
			
			ComputationResponse.put("xPos", Float.parseFloat(playerDetails.get("xPos").toString()));
			ComputationResponse.put("yPos", Float.parseFloat(playerDetails.get("yPos").toString()));
			ComputationResponse.put("zPos", Float.parseFloat(playerDetails.get("zPos").toString()));
			ComputationResponse.put("rotY", Float.parseFloat(playerDetails.get("rotY").toString()));
			ComputationResponse.put("isMoving", Boolean.parseBoolean(playerDetails.get("isMoving").toString()));
		} else {
			JSONRaw = excutePostAsync(new String[]{"target","uTargetId"}, new String[]{"positionDownloader",String.valueOf(userId)});
			
		}
		return ComputationResponse;
	}
	public Map<String, Object> locateAsyncUserPosition(Integer userId){
		Map<String, Object> ComputationResponse = new HashMap<String, Object>();
		String JSONRaw = null;
				
		Object obj=JSONValue.parse(JSONRaw);
		JSONArray array=(JSONArray)obj;
		JSONObject playerDetails=(JSONObject)array.get(0);
		
		ComputationResponse.put("xPos", Float.parseFloat(playerDetails.get("xPos").toString()));
		ComputationResponse.put("yPos", Float.parseFloat(playerDetails.get("yPos").toString()));
		ComputationResponse.put("zPos", Float.parseFloat(playerDetails.get("zPos").toString()));
		ComputationResponse.put("rotY", Float.parseFloat(playerDetails.get("rotY").toString()));
		ComputationResponse.put("isMoving", Boolean.parseBoolean(playerDetails.get("isMoving").toString()));
	
		Util.logC("located: " + JSONRaw);
		return ComputationResponse;
	}*/
	 /**
	    * Creates an appropriate digest and salt for the new user name and password provided
	    * @param username String The user name of the user
	    * @param password String The password of the user
	    * @return boolean Returns true if the creation is a success, false otherwise
	    */
	public static boolean addUser(String username, String password) {
		Util.logC("=== AddUser");
		Boolean ComputationResponse = false;
		String userAcceptedResponse = "";
		
			
		// Uses a secure Random not a simple Random
        SecureRandom random = null;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			WEB_STATUS = 4;
			Util.logC(" === Error in addUser Web Status: " + WEB_STATUS);
		}
        // Salt generation 64 bits long
        byte[] bSalt = new byte[8];
        random.nextBytes(bSalt);
        // Digest computation
        byte[] bDigest = getHash(ITERATION_NUMBER,password,bSalt);
        String sDigest = byteToBase64(bDigest);
        String sSalt = byteToBase64(bSalt);		
        
        Util.logC("Add Users Digest: " + sDigest);
        Util.logC("Add Users Digest Salt: " + sSalt);
         
		userAcceptedResponse = excutePost(new String[]{"target","uName","sDigest","sSalt"}, new String[]{"addUser",username,sDigest,sSalt});
		
		// Does the web server accept this request
		if(userAcceptedResponse.equals("true") && userAcceptedResponse != "" ){
			ComputationResponse = true;				
		} else {
			ComputationResponse = false;
			
			// Web status failed to accept new request
			WEB_STATUS = 2;
			Util.logC("Add User failed since user exists already");
		}
			
			
		
		Util.logC("User Add Web Status: " + WEB_STATUS);
		Util.logC("User Added Final Status: " + ComputationResponse.toString());
		return ComputationResponse;	
		
	}
	/**
	    * Locates a user on the web server
	    * @param username String The user name of the user
	    * @return boolean Returns a JSON array of users digest and salt
	    */
	public static void findUser(String username) {
		excutePostAsync(new String[]{"target","uName"}, new String[]{"findUser",username}, false);	
	}
	 /**
	    * Authenticates the user with a given login and password
	    * If password and/or login is null then always returns false.
	    * If the user does not exist in the database returns false.
	    * @param username String The login of the user
	    * @param password String The password of the user
	    * @return boolean Returns true if the user is authenticated, false otherwise
	    */
	public static boolean isLoginOK(String password) {
		String JSONRaw = null;
		Util.logC("=== isLoginOK");
		boolean ComputationResponse = false;
		JSONRaw = JSONRawMain;
		JSONRawMain = "";
		if(JSONRaw.length() < 200 && !JSONRaw.trim().equals("[false]")){
			try {		
				Object obj=JSONValue.parse(JSONRaw);
				JSONArray array=(JSONArray)obj;
				JSONObject hashDetails = null;				
				hashDetails=(JSONObject)array.get(0);
				
				// Log details
				Util.logC("Existing Users Digest (From DB): " + hashDetails.get("storedDigest"));		
				Util.logC("Existing Users Digest Salt (From DB): " + hashDetails.get("storedDigestSalt"));			
				
				// Compute the existing digest
				byte[] bDigest = base64ToByte(hashDetails.get("storedDigest").toString().trim());
				byte[] bSalt = base64ToByte(hashDetails.get("storedDigestSalt").toString().trim());
				
		        // Compute the new digest
		        byte[] proposedDigest = getHash(ITERATION_NUMBER, password, bSalt);
		        
		        // Log details
		        Util.logC("Existing Users Digest Salt: " + byteToBase64(bSalt).toString());		
				Util.logC("Existing Users Proposed Digest: " + byteToBase64(proposedDigest).toString());
		        
		        if(Arrays.equals(proposedDigest, bDigest)){
		        	ComputationResponse = true;		        	
		        	
		        } else {
		        	WEB_STATUS = 2;
		        	Util.logC(" === Error in isLoginOK Web Status: " + WEB_STATUS);
		        }
				
			} catch (IOException ex){
				WEB_STATUS = 6;
				Util.logC(" === Error in isLoginOk Web Status: " + WEB_STATUS);
				ComputationResponse = false;
		      }
		} else { 
			// User has not web access			
			WEB_STATUS = 3;
			Util.logC(" === Error in isLoginOk Web Status: " + WEB_STATUS);
			ComputationResponse = false;
		}	
		
		Util.logC("User Login Final Status: " + ComputationResponse);
		return ComputationResponse;	
	
	}
	   /**
	    * From a base 64 representation, returns the corresponding byte[] 
	    * @param data String The base64 representation
	    * @return byte[]
	    * @throws IOException
	    */
	   private static byte[] base64ToByte(String data) throws IOException {
	       return Base64.decodeBase64(data);
	   }
	 
	   /**
	    * From a byte[] returns a base 64 representation
	    * @param data byte[]
	    * @return String
	    */
	   private static String byteToBase64(byte[] data){
	       
	       return Base64.encodeBase64String(data);
	   }
	   /**
	    * From a password, a number of iterations and a salt,
	    * returns the corresponding digest
	    * @param iterationNb int The number of iterations of the algorithm
	    * @param password String The password to encrypt
	    * @param salt byte[] The salt
	    * @return byte[] The digested password
	    * @throws NoSuchAlgorithmException If the algorithm doesn't exist
	    * @throws UnsupportedEncodingException 
	    */
	   private static byte[] getHash(int iterationNb, String password, byte[] salt) {
		   byte[] input = null;
		   try {
				MessageDigest digest = MessageDigest.getInstance("SHA-1");
			       digest.reset();
			       digest.update(salt);
			       
				input = digest.digest(password.getBytes("UTF-8"));
				for (int i = 0; i < iterationNb; i++) {
			           digest.reset();
			           input = digest.digest(input);
			       }
			      
			} catch (UnsupportedEncodingException e) {
				WEB_STATUS = 5;
				Util.logC(" ===== Error in hash" + WEB_STATUS);
			} catch (NoSuchAlgorithmException e) {
				WEB_STATUS = 4;
				Util.logC(" ===== Error in hash" + WEB_STATUS);				
			}
		 
		 return input;
	       
	   }
	
	   /**
	    * 
	    * Executes a post request on the web server
	    * @param urlParameters Various parameters sent as a POST request
	    * @return boolean Returns the response received from the web server

	    */

	public static String excutePostAsync(String[] urlName, String[] urlAttribute, final Boolean doPassive)
	  {
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost("benallen.info").setPath("/projects/GameJJB/bin/dataHandler.php");
		for (int i = 0; i < urlName.length; i++){
			builder.setParameter(urlName[i], urlAttribute[i]);
		}
		URI requestURL = null;
		try {
		    requestURL = builder.build();
		} catch (URISyntaxException use) {}

		ExecutorService threadpool = Executors.newFixedThreadPool(2);
		Async async = Async.newInstance().use(threadpool);
		final Request request = Request.Get(requestURL);

		Future<Content> future = async.execute(request, new FutureCallback<Content>() {
		    public void failed (final Exception e) {
		        System.out.println(e.getMessage() +": "+ request);
		        System.out.println("fail");
		    }
		    public void completed (final Content content) {
		        if(!doPassive){JSONRawMain = content.asString();}
		    }

		    public void cancelled () {
		    	Util.logC("Internet Cancelled in excutePost");		    	
		    }
		});
		
		return "";
	  }
	private static String excutePost(String[] urlName, String[] urlAttribute)
	  {
		String urlParameters = "x=y";
		try {
			for (int i = 0; i< urlName.length; i++){
				urlParameters += "&"+urlName[i]+"=" + URLEncoder.encode(urlAttribute[i], "UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			WEB_STATUS = 5;
			Util.logC(" === Error in downloadUserPosition Web Status: " + WEB_STATUS);
		}
		
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	    	String newWeb = WEB_SERVER + "?x=y";
	    	for (int i = 0; i < urlName.length; i++){
	    		newWeb += "&" + urlName[i] + "=" + urlAttribute[i];
			}
			url = new URL(newWeb);
			connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");				
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US"); 				
			connection.setUseCaches (false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
			wr.writeBytes (urlParameters);
			wr.flush();
			wr.close();
			
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer(); 
			
			while((line = rd.readLine()) != null) {
			  response.append(line);
			  response.append('\r');
			}
			rd.close();
			
			return response.toString();
			
	    } catch (IOException e) {
			WEB_STATUS = 3;
			Util.logC(" ===== Error in post Exectution: " + WEB_STATUS);
	      return "";
	    } finally {
	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	  }
}