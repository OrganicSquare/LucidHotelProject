package game;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import communication.InternetConnector;

public class Main{
	static int WIDTH = 1200;
	static int HEIGHT = 675;
	static final int MAX_PLAYERS = 8;
	static int SYNC_DELAY = 0;
	static final int SYNC_DELAY_MAX = 5;
	
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
	
	static WindowListener listener = new WindowAdapter() {
		public void windowClosing(WindowEvent w) {
			windowClosed = true;
			if(loginSuccessful){
				communication.InternetConnector.logoutUser((Integer)userInfo.get("uId"));
			}
			System.exit(0);
		}
	};
	
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
		Animation walkingMan = new Animation("Animations/Walking Man","Walking Man",1,38);
		Animation standingMan = new Animation("Animations/Walking Man","Standing Man",1);
		
		
		while(!Display.isCloseRequested() && !windowClosed){
			if(loginSuccessful){
				userInput(cam);
				glClear(GL_COLOR_BUFFER_BIT);
				glClear(GL_DEPTH_BUFFER_BIT);
				
				if(!initialisedUserInfo){
					initUserInfo(standingMan);
				}
				if(SYNC_DELAY > SYNC_DELAY_MAX) {
					
					// Download other user data
					otherUserInfo = InternetConnector.decodeUserPositions();
					// Load in the other users data
					if(initOtherUserInfo){
						for(int i = 0; i < otherUserInfo.size(); i++){
							Map<String, Object> userInfoOnline = otherUserInfo.get(i);
							player[i] = new Player(userInfoOnline, standingMan);
							initOtherUserInfo = false;
							playerNum++;
						}
					}
					else{
						for(int i = 0; i < otherUserInfo.size(); i++){
							Map<String, Object> userInfoOnline = otherUserInfo.get(i);
							try{
							player[i].xPos = (float)userInfoOnline.get("xPos");
							player[i].yPos = (float)userInfoOnline.get("yPos");
							player[i].zPos = (float)userInfoOnline.get("zPos");
							player[i].yRot = (float)userInfoOnline.get("rotY");
							}
							catch(Exception e){
								e.printStackTrace();
							}
						}
					}
					
					// Set up download for next time
					InternetConnector.downloadAllUserPositions((Integer)userInfo.get("uId"));
					
					// Send user data
					InternetConnector.sendUserPosition((int)userInfo.get("uId"),new float[]{(float)userInfo.get("xPos"),(float)userInfo.get("yPos"),(float)userInfo.get("zPos"),(float)userInfo.get("rotY"),1});
					
					// Will reset the delay
					SYNC_DELAY = 0;
				} else {SYNC_DELAY += 1;}
				
				glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(new float[]{0f,3f,0f,1f}));				
				for(int i = 0;i<playerNum;i++){
					player[i].drawUser();
				}
				
				if(clientUser.isMoving){
					clientUser.setModel(walkingMan);
				}
				else{
					clientUser.setModel(standingMan);
				}
				clientUser.drawUser();
				System.out.println("X: " + clientUser.xPos);
				System.out.println("Y: " + clientUser.yPos);
				System.out.println("Z: " + clientUser.zPos);
				
				glLoadIdentity();
				//cam.setX(clientUser.xPos);
				//cam.setZ(clientUser.zPos-5);
				cam.setRotY((float)Vector.angle(clientUser.xPos,clientUser.zPos,cam.getX(),cam.getZ()) + 180);
				thirdPersonCam(cam);
				cam.useCam();
				
				
				drawAxis();
			}
			Display.update();
			Display.sync(60);
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
	public static void userInput(Camera cam){
		float speed = 0.1f;
		float rotSpeed = 3f;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W) && !initOtherUserInfo){
			userInfo.put("xPos", (float)userInfo.get("xPos")+(float)Math.sin(Math.toRadians(clientUser.yRot))*clientUser.speed);
			userInfo.put("zPos", (float)userInfo.get("zPos")+(float)Math.cos(Math.toRadians(clientUser.yRot))*clientUser.speed);
			clientUser.xPos += Math.sin(Math.toRadians(clientUser.yRot))*clientUser.speed;
			clientUser.zPos += Math.cos(Math.toRadians(clientUser.yRot))*clientUser.speed;
			clientUser.isMoving = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			userTurn-= 1f;
			cam.setX(clientUser.xPos-(float)(Math.cos((userTurn%100)/100*Math.PI*2)*5));
			cam.setZ(clientUser.zPos-(float)(Math.sin((userTurn%100)/100*Math.PI*2)*5));
			clientUser.yRot = 180-(float)Vector.angle(cam.getX(),cam.getZ(),clientUser.xPos,clientUser.zPos);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S) && !initOtherUserInfo){
			userInfo.put("xPos", (float)userInfo.get("xPos")-(float)Math.sin(Math.toRadians(clientUser.yRot))*clientUser.speed);
			userInfo.put("zPos", (float)userInfo.get("zPos")-(float)Math.cos(Math.toRadians(clientUser.yRot))*clientUser.speed);
			clientUser.xPos -= Math.sin(Math.toRadians(clientUser.yRot))*clientUser.speed;
			clientUser.zPos -= Math.cos(Math.toRadians(clientUser.yRot))*clientUser.speed;
			clientUser.isMoving = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			userTurn+= 1f;
			cam.setX(clientUser.xPos-(float)(Math.cos((userTurn%100)/100*Math.PI*2)*5));
			cam.setZ(clientUser.zPos-(float)(Math.sin((userTurn%100)/100*Math.PI*2)*5));
			clientUser.yRot = 180-(float)Vector.angle(cam.getX(),cam.getZ(),clientUser.xPos,clientUser.zPos);
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S) && !initOtherUserInfo){
			clientUser.isMoving = false;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			cam.setRotY(cam.getRotY() - rotSpeed);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			cam.setRotY(cam.getRotY() + rotSpeed);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			cam.setRotX(cam.getRotX() - rotSpeed/2);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			cam.setRotX(cam.getRotX() + rotSpeed/2);
		}
		if(cam.getRotX() > 30){
			cam.setRotX(30);
		}
		if(cam.getRotX() < -30){
			cam.setRotX(-30);
		}
		if(cam.getRotY() >= 360){
			cam.setRotY(0);
		}
		if(cam.getRotY() <= -360){
			cam.setRotY(0);
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_R)){
			clientUser.zPos += 0.1f;
			userInfo.put("zPos",clientUser.zPos);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_V)){
			clientUser.zPos -= 0.1f;
			userInfo.put("zPos",clientUser.zPos);
		}
	}
	public static void drawAxis(){
		glPushMatrix();
		glColor3f(0,0,1);
		glBegin(GL_LINES);
		glVertex3f(-100,0,0);
		glVertex3f(100,0,0);
		glEnd();
		glBegin(GL_LINES);
		glVertex3f(0,-100,0);
		glVertex3f(0,100,0);
		glEnd();
		glBegin(GL_LINES);
		glVertex3f(0,0,-100);
		glVertex3f(0,0,100);
		glEnd();
		glPopMatrix();
	}
	public static void initWindow(){
		lwjglCanvas.setFocusable(true);
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		//WIDTH = width;		-- auto sizing
		//HEIGHT = height;
		// height of the task bar
		Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(gd.getDefaultConfiguration());
		int taskBarSize =scnMax.bottom;
		
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
		frame.addWindowListener(listener);
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