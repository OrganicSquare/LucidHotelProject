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
	private Timer messageResponseTimer;
	public static void main(String args[]){
		// Send out chat message
		
	}
	public void sendMessage(String message, HashMap<String, Object> userInfo){
		message = message.trim();

		String[] urlName = new String[]{"target", "uId", "uName","message"};
		String[] urlAttribute = new String[]{"sendMessage",(String)userInfo.get("uId"),(String)userInfo.get("uName"),message};	
		InternetConnector.excutePostAsync(urlName, urlAttribute, 0);
	}
	public void checkForMessages(){
		String[] urlName = new String[]{"target"};
		String[] urlAttribute = new String[]{"checkForMessage"};	
		InternetConnector.excutePostAsync(urlName, urlAttribute, 2);
	}
	public Map<Integer, Map<String, Object>> decodeMessage(){
		Map<Integer, Map<String, Object>> ComputationResponse = new HashMap<Integer, Map<String, Object>>();
		String JSONRaw = InternetConnector.JSONRawMain[2];
		if(JSONRaw.trim().equals("false") || JSONRaw.trim().equals("") || JSONRaw.length() > 200){
			//System.out.println("No chat available");
		} else {
			Object obj=JSONValue.parse(JSONRaw);
			JSONArray array=(JSONArray)obj;
			for (int i = 0; i< array.size(); i++){
				Map<String, Object> individualMessage = new HashMap<String, Object>();
				JSONObject individualMessageJSON=(JSONObject)array.get(i);			
				try {
					individualMessage.put("mId", Integer.parseInt(individualMessageJSON.get("id").toString()));
					individualMessage.put("uName", individualMessageJSON.get("uName").toString());
					individualMessage.put("uId", Integer.parseInt(individualMessageJSON.get("uId").toString()));
					individualMessage.put("message", individualMessageJSON.get("message").toString());
					individualMessage.put("timeAgo", individualMessageJSON.get("timeAgo").toString());
				} catch (Exception ex) {
					System.out.println("Error converting the chat data w/ data : " + JSONRaw);				
				}
				ComputationResponse.put(i, individualMessage);
			}	
		}
		return ComputationResponse;
	}
	 public void asyncCheckMessage(int seconds) {
		 	messageResponseTimer = new Timer();
		 	messageResponseTimer.schedule(new initialDownloadCompleteUser(), seconds);
		}
	 class initialDownloadCompleteUser extends TimerTask {
	        public void run() {
	            if(InternetConnector.JSONRawMain[2].equals("")){
	            	asyncCheckMessage(500);
	            } else {
	            	decodeMessage();
	            	messageResponseTimer.cancel(); 
	            }
	        }
	    }
	
}
