package entities;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LIGHT0;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_POSITION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLight;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glVertex3f;
import game.Animation;
import game.Camera;
import game.Lighting;
import game.Player;
import game.Vector;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.FloatBuffer;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class EntityViewer {
	static JFrame frame = new JFrame("EntityViewer");
	static Canvas lwjglCanvas = new Canvas();
	static boolean initialisedUserInfo = false,  windowClosed = false;;
	static Player clientUser;
	static Map<String, Object> userInfo;
	static WindowListener listener = new WindowAdapter() {
		public void windowClosing(WindowEvent w) {
			windowClosed = true;
			
			System.exit(0);
		}
	};
	public static void main(String args[]){
		initWindow();
		gameLoop();
		cleanUp();
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
		frame.setPreferredSize(new Dimension(400,400));
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		lwjglCanvas.setBounds(0,0,400,400);
		
		frame.add(lwjglCanvas);
		lwjglCanvas.setVisible(false);
		frame.setVisible(true);
		frame.addWindowListener(listener);
	}
	public static void gameLoop(){
		Animation standingMan = new Animation("Animations/Walking Man","Standing Man",1);
		userInfo.put("xPos", 1.0f);
		userInfo.put("yPos", 0.0f);
		userInfo.put("zPos", 0.0f);
		userInfo.put("rotY", 0.0f);
		clientUser =  new Player(userInfo, standingMan);
		Lighting.initLight();
		Camera cam = new Camera(70,(float)400/(float)400,0.3f,1000);
		cam.setZ(10);
		cam.setY(3);
		while(!Display.isCloseRequested() && !windowClosed){
			glClear(GL_COLOR_BUFFER_BIT);
			glClear(GL_DEPTH_BUFFER_BIT);
			glLight(GL_LIGHT0, GL_POSITION, asFloatBuffer(new float[]{0f,3f,0f,1f}));	
			glLoadIdentity();
			cam.setRotY((float)Vector.angle(10,0,cam.getX(),cam.getZ()) + 180);
			thirdPersonCam(cam);
			cam.useCam();
			drawAxis();
		}
		Display.update();
		Display.sync(60);
	}
	public static void thirdPersonCam(Camera cam){
		cam.setX(10-(float)(Math.cos((50%100)/100*Math.PI*2)*5));
		cam.setZ(0-(float)(Math.sin((50%100)/100*Math.PI*2)*5));
		//clientUser.yRot = 180-(float)Vector.angle(cam.getX(),cam.getZ(),clientUser.xPos,clientUser.zPos);
	}
	
	private static FloatBuffer asFloatBuffer(float[] values){
		FloatBuffer buffer = BufferUtils.createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		return buffer;
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
	public static void cleanUp() {
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
}
