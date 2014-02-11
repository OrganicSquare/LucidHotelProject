package game;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glVertex3f;

public class Util {
 public static Boolean DEBUG_MODE = false;

  /**
     * If Debug mode is activated this will log the reports from program
     * @param message The message returned if debug mode is active
     */
 public static void logC(String content){
  if(DEBUG_MODE){
   System.out.println(content);
  }
 }
 public static boolean isValid(String content){
	 if(content != null && !content.isEmpty() && !content.equals("") && !content.equals("[false]") && !content.equals("null") && content.length() != 0 && content.length() < 500){
		 return true;
	 } else {
		 return false;
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
}