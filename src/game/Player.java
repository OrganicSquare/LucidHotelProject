package game;

import java.util.Map;

import communication.InternetConnector;

import static org.lwjgl.opengl.GL11.*;

public class Player {
	Animation animation;
	int user_id;
	float xPos, yPos, zPos, xRot, yRot, zRot;
	boolean isMoving;
	public Player(Map<String, Object> userInfo){
		user_id = (Integer)userInfo.get("uId");
		xPos = (Float)userInfo.get("xPos");
		yPos = (Float)userInfo.get("yPos");
		zPos = (Float)userInfo.get("zPos");
		yRot = (Float)userInfo.get("rotY");
		animation = new Animation("Animations/Walking Man","Walking Man",1,20);
	}	
	
	public void drawUser(){
		glPushMatrix();
		glRotatef(xRot,1,0,0);
		glRotatef(yRot,0,1,0);
		glRotatef(zRot,0,0,1);
		glTranslatef(xPos,yPos,zPos);
		animation.animate(0.5f);
		glPopMatrix();
	}
}
