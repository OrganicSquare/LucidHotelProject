package game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class Camera {
	private float x,y,z,rotX,rotY,rotZ;
	private float fov,aspect,near,far;
	public Camera(float fov, float aspect, float near, float far){
		this.fov = fov;
		this.aspect = aspect;
		this.near = near;
		this.far = far;
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.rotX = 0;
		this.rotY = 0;
		this.rotZ = 0;
		initOpenGL();
	}
	public void initOpenGL(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fov, aspect, near, far);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);
	}
	public void useCam(){
		glRotatef(rotX,1,0,0);
		glRotatef(rotY,0,1,0);
		glRotatef(rotZ,0,0,1);
		glTranslatef(-x,-y,-z);
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}
	public float getRotX() {
		return rotX;
	}
	public void setRotX(float rotX) {
		this.rotX = rotX;
	}
	public float getRotY() {
		return rotY;
	}
	public void setRotY(float rotY) {
		this.rotY = rotY;
	}
	public float getRotZ() {
		return rotZ;
	}
	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}
}
