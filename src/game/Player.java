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
		user_id = (int)userInfo.get("uId");
		xPos = (float)userInfo.get("xPos");
		yPos = (float)userInfo.get("yPos");
		zPos = (float)userInfo.get("zPos");
		yRot = (float)userInfo.get("rotY");
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
