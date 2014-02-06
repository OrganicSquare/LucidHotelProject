package game;

import java.util.Map;

import communication.InternetConnector;
import static org.lwjgl.opengl.GL11.*;

public class Player {
	Animation animation;
	int user_id;
<<<<<<< HEAD
	float xPos, yPos, zPos, xRot, yRot, zRot;
	boolean isMoving;
	public Player(Map<String, Object> userInfo){
		user_id = (Integer)userInfo.get("uId");
		xPos = (Float)userInfo.get("xPos");
		yPos = (Float)userInfo.get("yPos");
		zPos = (Float)userInfo.get("zPos");
		yRot = (Float)userInfo.get("rotY");
		animation = new Animation("Animations/Walking Man","Walking Man",1,20);
=======
	float xPos, yPos, zPos, xRot, yRot, zRot, timer;
	boolean isMoving, isAnimating;
	public Player(Map<String, Object> userInfo, Animation anim){
		this.user_id = (Integer)userInfo.get("uId");
		this.xPos = (Float)userInfo.get("xPos");
		this.yPos = (Float)userInfo.get("yPos");
		this.zPos = (Float)userInfo.get("zPos");
		this.yRot = (Float)userInfo.get("rotY");
		this.animation = anim;
>>>>>>> origin/master
	}	
	
	public void drawUser(){
		glPushMatrix();
		glRotatef(xRot,1,0,0);
		glRotatef(yRot,0,1,0);
		glRotatef(zRot,0,0,1);
		glTranslatef(xPos,yPos,zPos);
		animate(0.5f);
		glPopMatrix();
	}
	public void animate(float rate){
		glCallList(animation.model[(int)Math.floor(timer)]);
		if(isAnimating){
			timer += rate;
		}
		if(timer >= animation.animationlength){
			timer = 1;
		}
		if(timer < 1){
			timer = 1;
		}
		return;
	}
	public boolean isAnimating() {
		return isAnimating;
	}
	public void setAnimating(boolean isAnimating) {
		this.isAnimating = isAnimating;
	}
	public void setModel(Animation animationModel){
		this.animation = animationModel;
	}
}
