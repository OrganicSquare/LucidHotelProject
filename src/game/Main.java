package game;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Canvas;
import java.awt.Dimension;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import communication.InternetConnector;
import entities.Entites;

public class Main{
	static int WIDTH = 1200;
	static int HEIGHT = 675;
	static final int MAX_PLAYERS = 8;
	static int SYNC_DELAY = 0;
	static final int SYNC_DELAY_MAX = 45;
	
	static Map<String, Object> userInfo;
	static Player clientUser;
	static boolean loginSuccessful = false, windowClosed = false;
	
	static Map<Integer, Map<String, Object>> otherUserInfo = null;
	
	static Animation animation[] = new Animation[16];
	static Player player[] = new Player[MAX_PLAYERS];
	static int[] playerIntToID;
	
	static JFrame frame = new JFrame("Multiplayer");
	static Canvas lwjglCanvas = new Canvas();
	static JPanel mainMenu = new JPanel();
	static boolean initialisedUserInfo = false, initOtherUserInfo = true;
	static int playerNum = 0;
	static float userTurn = 0;
	

	
	public static void main(String args[]){
		initWindow();
		gameLoop();
		cleanUp();
	}
	
	public static void gameLoop(){
		Lighting.initLight();
		Camera cam = new Camera(70,(float)WIDTH/(float)HEIGHT,0.3f,1000);
		cam.setZ(10);
		cam.setY(3);
		Entites.loadPlayer();
		
		while(!Display.isCloseRequested() && !windowClosed){
			if(loginSuccessful){
				UserInput.update(cam);
				glClear(GL_COLOR_BUFFER_BIT);
				glClear(GL_DEPTH_BUFFER_BIT);
				
				if(SYNC_DELAY > SYNC_DELAY_MAX) {
					// Other Players have been created already so update their positions 
						// Download other user data
						otherUserInfo = InternetConnector.decodeUserPositions();
						for(int i = 0; i < otherUserInfo.size(); i++){
							Map<String, Object> userInfoOnline = otherUserInfo.get(i);
							try{
							player[i].xPos = (Float)userInfoOnline.get("xPos");
							player[i].yPos = (Float)userInfoOnline.get("yPos");
							player[i].zPos = (Float)userInfoOnline.get("zPos");
							player[i].yRot = (Float)userInfoOnline.get("rotY");
							}
							catch(Exception e){
								e.printStackTrace();
							}
						}
						// Set up download for next time
						InternetConnector.downloadAllUserPositions((Integer)userInfo.get("uId"));
					
					// Send the users position data
					//InternetConnector.sendUserPosition((Integer)userInfo.get("uId"),new float[]{(Float)userInfo.get("xPos"),(Float)userInfo.get("yPos"),(Float)userInfo.get("zPos"),(Float)userInfo.get("rotY"),1});
					
					// Will reset the delay
					SYNC_DELAY = 0;
				} else {SYNC_DELAY += 1;}
				
				glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(new float[]{0f,3f,0f,1f}));	
				
				// Draw all the other user positions
				for(int i = 0;i<playerNum;i++){
					player[i].drawUser();
				}
				
				// Animate the user
				/*if(clientUser.isMoving){
					clientUser.setModel(Entites.walkingPlayer);
				}
				else{
					clientUser.setModel(Entites.standingPlayer);
				}*/
				// Draw the user
				clientUser.drawUser();
				
				glLoadIdentity();
				//cam.setX(clientUser.xPos);
				//cam.setZ(clientUser.zPos-5);
				cam.setRotY((float)Vector.angle(clientUser.xPos,clientUser.zPos,cam.getX(),cam.getZ()) + 180);
				thirdPersonCam(cam);
				cam.useCam();
				
				
				Util.drawAxis();
			}
			Display.update();
			Display.sync(60);
		}
	}
	// Called once from menu.java to create the other players on the server.
	public static void initiateOtherUserInfo(){
		/*
		System.out.println(otherUserInfo);
		for(int i = 0; i < otherUserInfo.size(); i++){
			Map<String, Object> userInfoOnline = otherUserInfo.get(i);
			player[i] = new Player(userInfoOnline, Entites.standingPlayer);
			playerNum++;
		}
		*/
		for(int i = 0; i < 2; i++){
			Map<String, Object> userInfoOnline = new HashMap<String, Object>();
			userInfoOnline.put("uId", 1);
			userInfoOnline.put("uSkin", "red");
			userInfoOnline.put("xPos", 10.0f);
			userInfoOnline.put("yPos", 10.0f);
			userInfoOnline.put("zPos", 10.0f);
			userInfoOnline.put("rotY", 10.0f);
			
			player[i] = new Player(userInfoOnline, Entites.standingPlayer);
			playerNum++;
		}
	}
	public static void thirdPersonCam(Camera cam){
		cam.setX(clientUser.xPos-(float)(Math.cos((userTurn%100)/100*Math.PI*2)*5));
		cam.setZ(clientUser.zPos-(float)(Math.sin((userTurn%100)/100*Math.PI*2)*5));
		clientUser.yRot = 180-(float)Vector.angle(cam.getX(),cam.getZ(),clientUser.xPos,clientUser.zPos);
	}
	
	private static FloatBuffer asFloatBuffer(float[] values){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		return buffer;
	}
	
	public static void initUserInfo(Animation startingModel){
		initialisedUserInfo = true;
		clientUser = new Player(userInfo, startingModel);
	}
	public static void initWindow(){
		lwjglCanvas.setFocusable(true);
		
		// set Ideal size
		frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		lwjglCanvas.setBounds(0,0,WIDTH,HEIGHT);
		
		frame.add(lwjglCanvas);
		lwjglCanvas.setVisible(false);
		initMenu();
		frame.setVisible(true);
		initDisplay();
		frame.addWindowListener(UserInput.closeListener);
	}
	public static void initDisplay(){
		try{
			Display.setParent(lwjglCanvas);
			Display.create();
			Keyboard.create();
			Mouse.create();
		}
		catch(LWJGLException e){
			e.printStackTrace();
		}
	}
	public static void initMenu(){
		Menu menu = new Menu();
		frame.add(menu);
	}
	public static void cleanUp() {
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
	
}