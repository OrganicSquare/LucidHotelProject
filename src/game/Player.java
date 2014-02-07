package game;

import java.util.Map;

import communication.InternetConnector;
import static org.lwjgl.opengl.GL11.*;

public class Player {
	Animation animation;
	int user_id;
	float xPos, yPos, zPos, xRot, yRot, zRot, timer, speed = 0.1f;
	boolean isMoving, isAnimating = true;
	public Player(Map<String, Object> userInfo, Animation anim){
		this.user_id = (Integer)userInfo.get("uId");
		this.xPos = (Float)userInfo.get("xPos");
		this.yPos = (Float)userInfo.get("yPos");
		this.zPos = (Float)userInfo.get("zPos");
		this.yRot = (Float)userInfo.get("rotY");
		this.animation = anim;
	}	
	
	public void drawUser(){
		glPushMatrix();
		
		glTranslatef(xPos,yPos,zPos);
		
		glRotatef(xRot,1,0,0);
		glRotatef(yRot,0,1,0);
		glRotatef(zRot,0,0,1);
		
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
