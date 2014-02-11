package game;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glNormal3f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class Animation{
	String directory, fileName;
	int start, end, animationlength;
	int model[] = new int[100];
	
	public Animation(String dir, String filename, int start){
		this.directory = dir;
		this.fileName = filename;
		this.start = start;
		this.end = start;
		this.animationlength = end-start+2;
		
		for(int i=1;i<this.animationlength;i++){
			model[i] = glGenLists(i);
			glNewList(model[i], GL_COMPILE);
			Model m = null;
			try{
				
				m = OBJLoader.loadModel(new File("res" + File.separator+dir+File.separator+filename+"_"+String.format("%06d", i+start-1)+".obj"));
				System.out.println("Loading: " + i);
				System.out.println(("res"+File.separator+dir+File.separator+filename+"_"+String.format("%06d", i+start-1)+".obj"));
			}
			catch(FileNotFoundException e){
				e.printStackTrace();
				Display.destroy();
				System.exit(1);
			}
			catch(IOException e){
				e.printStackTrace();
				Display.destroy();
				System.exit(1);
			}
			glBegin(GL_TRIANGLES);
			for(Face face: m.faces){
				Vector3f n1 = m.normals.get((int)face.normal.x - 1);
				glNormal3f(n1.x, n1.y, n1.z);
				Vector3f v1 = m.vertices.get((int)face.vertex.x - 1);
				glVertex3f(v1.x, v1.y, v1.z);
				Vector3f n2 = m.normals.get((int) face.normal.y - 1);
				glNormal3f(n2.x, n2.y, n2.z);
				Vector3f v2 = m.vertices.get((int) face.vertex.y - 1);
				glVertex3f(v2.x, v2.y, v2.z);
				Vector3f n3 = m.normals.get((int)face.normal.z - 1);
				glNormal3f(n3.x, n3.y, n3.z);
				Vector3f v3 = m.vertices.get((int)face.vertex.z - 1);
				glVertex3f(v3.x, v3.y, v3.z);
			}
			glEnd();
			glEndList();
		}
	}	
	public Animation(String dir, String filename, int start, int end){
		this.directory = dir;
		this.fileName = filename;
		this.start = start;
		this.end = end;
		this.animationlength = end-start+2;
		
		for(int i=1;i<this.animationlength;i++){
			model[i] = glGenLists(i);
			glNewList(model[i], GL_COMPILE);
			Model m = null;
			try{
				m = OBJLoader.loadModel(new File("res"+File.separator+dir+File.separator+filename+"_"+String.format("%06d", i+start-1)+".obj"));
				System.out.println(("res"+File.separator+dir+File.separator+filename+"_"+String.format("%06d", i+start-1)+".obj"));
				if(i == 38){
					
					game.Util.logC("Loading Finished");
				}
			}
			catch(FileNotFoundException e){
				e.printStackTrace();
				Display.destroy();
				System.exit(1);
			}
			catch(IOException e){
				e.printStackTrace();
				Display.destroy();
				System.exit(1);
			}
			glBegin(GL_TRIANGLES);
			for(Face face: m.faces){
				Vector3f n1 = m.normals.get((int)face.normal.x - 1);
				glNormal3f(n1.x, n1.y, n1.z);
				Vector3f v1 = m.vertices.get((int)face.vertex.x - 1);
				glVertex3f(v1.x, v1.y, v1.z);
				Vector3f n2 = m.normals.get((int) face.normal.y - 1);
				glNormal3f(n2.x, n2.y, n2.z);
				Vector3f v2 = m.vertices.get((int) face.vertex.y - 1);
				glVertex3f(v2.x, v2.y, v2.z);
				Vector3f n3 = m.normals.get((int)face.normal.z - 1);
				glNormal3f(n3.x, n3.y, n3.z);
				Vector3f v3 = m.vertices.get((int)face.vertex.z - 1);
				glVertex3f(v3.x, v3.y, v3.z);
			}
			glEnd();
			glEndList();
		}
	}
}
