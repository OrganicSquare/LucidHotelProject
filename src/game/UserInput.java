package game;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.lwjgl.input.Keyboard;

public class UserInput {
	
	public static void update(Camera cam){
		float speed = 0.1f;
		float rotSpeed = 3f;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W) && !Main.initOtherUserInfo){
			Main.userInfo.put("xPos", (Float)Main.userInfo.get("xPos")+(float)Math.sin(Math.toRadians(Main.clientUser.yRot))*Main.clientUser.speed);
			Main.userInfo.put("zPos", (Float)Main.userInfo.get("zPos")+(float)Math.cos(Math.toRadians(Main.clientUser.yRot))*Main.clientUser.speed);
			Main.clientUser.xPos += Math.sin(Math.toRadians(Main.clientUser.yRot))*Main.clientUser.speed;
			Main.clientUser.zPos += Math.cos(Math.toRadians(Main.clientUser.yRot))*Main.clientUser.speed;
			Main.clientUser.isMoving = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			Main.userTurn-= 1f;
			cam.setX(Main.clientUser.xPos-(float)(Math.cos((Main.userTurn%100)/100*Math.PI*2)*5));
			cam.setZ(Main.clientUser.zPos-(float)(Math.sin((Main.userTurn%100)/100*Math.PI*2)*5));
			Main.userInfo.put("rotY", (Float)Main.userInfo.get("rotY") + 180-(float)Vector.angle(cam.getX(),cam.getZ(),Main.clientUser.xPos,Main.clientUser.zPos));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S) && !Main.initOtherUserInfo){
			Main.userInfo.put("xPos", (Float)Main.userInfo.get("xPos")-(float)Math.sin(Math.toRadians(Main.clientUser.yRot))*Main.clientUser.speed);
			Main.userInfo.put("zPos", (Float)Main.userInfo.get("zPos")-(float)Math.cos(Math.toRadians(Main.clientUser.yRot))*Main.clientUser.speed);
			Main.clientUser.xPos -= Math.sin(Math.toRadians(Main.clientUser.yRot))*Main.clientUser.speed;
			Main.clientUser.zPos -= Math.cos(Math.toRadians(Main.clientUser.yRot))*Main.clientUser.speed;
			Main.clientUser.isMoving = true;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			Main.userTurn+= 1f;
			cam.setX(Main.clientUser.xPos-(float)(Math.cos((Main.userTurn%100)/100*Math.PI*2)*5));
			cam.setZ(Main.clientUser.zPos-(float)(Math.sin((Main.userTurn%100)/100*Math.PI*2)*5));
			Main.userInfo.put("rotY", (Float)Main.userInfo.get("rotY") + 180-(float)Vector.angle(cam.getX(),cam.getZ(),Main.clientUser.xPos,Main.clientUser.zPos));
		}
		if(!Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S) && !Main.initOtherUserInfo){
			Main.clientUser.isMoving = false;
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
			Main.clientUser.zPos += 0.1f;
			Main.userInfo.put("zPos",Main.clientUser.zPos);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_V)){
			Main.clientUser.zPos -= 0.1f;
			Main.userInfo.put("zPos",Main.clientUser.zPos);
		}
	}
	static WindowListener closeListener = new WindowAdapter() {
		public void windowClosing(WindowEvent w) {
			Main.windowClosed = true;
			if(Main.loginSuccessful){
				communication.InternetConnector.logoutUser((Integer)Main.userInfo.get("uId"));
			}
			System.exit(0);
		}
	};
}
