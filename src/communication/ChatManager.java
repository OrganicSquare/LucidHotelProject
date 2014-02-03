package communication;

import game.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class ChatManager {
	public String ServerRespones = null;
	static int SYNC_DELAY = 600;
	static final int SYNC_DELAY_MAX = 500;
	public static void main(String args[]){
		// Send out chat message
			//HashMap<String, Object> userInfo = new HashMap<String, Object>();
			//userInfo.put("uId",3);
			//userInfo.put("uName","Ben");
			//sendMessage("Hello Third", userInfo, "1");
		// Check for messages
			//checkForMessages(11);
		
		// Must be called first time round: InternetConnector.JSONRawMain[2] = "";
		
	}
	public static void sendMessage(String message, HashMap<String, Object> userInfo, String type){
		message = message.trim();

		String[] urlName = new String[]{"target", "uId", "uName","message","type"};
		String[] urlAttribute = new String[]{"sendMessage",userInfo.get("uId").toString(),(String)userInfo.get("uName").toString(),message, type};	
		InternetConnector.excutePostAsync(urlName, urlAttribute, 0);
	}
	public static void checkForMessages(int numberOfPlayersOnline){
		
		if(SYNC_DELAY > SYNC_DELAY_MAX) {
			String[] urlName = new String[]{"target", "numberOfPlayersOnline"};
			String[] urlAttribute = new String[]{"checkForMessage", Integer.toString(numberOfPlayersOnline)};	
			InternetConnector.excutePostAsync(urlName, urlAttribute, 2);
			// asyncCheckMessage(100); ~ For testing purposes only
			
			SYNC_DELAY = 0;
		} else {
			SYNC_DELAY += 1;
		}
	}
	public static Map<Integer, Map<String, Object>> decodeMessage(){
		Map<Integer, Map<String, Object>> ComputationResponse = new HashMap<Integer, Map<String, Object>>();
		String JSONRaw = InternetConnector.JSONRawMain[2];
		if(JSONRaw.trim().equals("false") || JSONRaw.trim().equals("") || JSONRaw.length() > 200){
			//System.out.println("No chat available");
		} else {
			Object obj=JSONValue.parse(JSONRaw);
			InternetConnector.JSONRawMain[2] = "";
			JSONArray array=(JSONArray)obj;
			for (int i = 0; i< array.size() -1; i++){
				Map<String, Object> individualMessage = new HashMap<String, Object>();
				JSONObject individualMessageJSON=(JSONObject)array.get(i);			
				try {
					individualMessage.put("mId", Integer.parseInt(individualMessageJSON.get("id").toString()));
					individualMessage.put("uName", individualMessageJSON.get("uName").toString());
					individualMessage.put("uId", Integer.parseInt(individualMessageJSON.get("uId").toString()));
					individualMessage.put("message", individualMessageJSON.get("message").toString());
					individualMessage.put("type", individualMessageJSON.get("type").toString());
					individualMessage.put("timeAgo", individualMessageJSON.get("timeAgo").toString());
				} catch (Exception ex) {
					System.out.println("Error converting the chat data w/ data : " + JSONRaw);				
				}
				ComputationResponse.put(i, individualMessage);
			}	
		}
		return ComputationResponse;
	}
	
	// The below is for testing the java only .. Ignore
 static Timer messageResponseTimer;
	public static void asyncCheckMessage(int seconds) {
				 	messageResponseTimer = new Timer();
				 	System.out.println("Called w/ data: " + InternetConnector.JSONRawMain[2]);
				 	messageResponseTimer.schedule(new initialDownloadCompleteUser(), seconds);
				}
	 public static class initialDownloadCompleteUser extends TimerTask {
	        public void run() {
	            if(InternetConnector.JSONRawMain[2].equals("") || InternetConnector.JSONRawMain[2] == null){
	            	asyncCheckMessage(500);
	            } else {
	            	System.out.println("Data Recieved: " + InternetConnector.JSONRawMain[2]);
	            	decodeMessage();
	            	messageResponseTimer.cancel(); 
	            }
	        }
	    }
			
		 
}
