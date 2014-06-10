package org.xtream.core.workbench.controls;

import java.awt.Dimension;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Disk;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

public class LwjglCanvas extends AWTGLCanvas
{

	private static final long serialVersionUID = 8952851694848706195L;
	
	public LwjglCanvas() throws LWJGLException
	{
		super();
		
		setPreferredSize(new Dimension(640, 480));
	}
	
	@Override
	public void initGL()
	{
		// Clear framebuffer
		
		GL11.glClearColor(1f, 1f, 1f, 0f);
		GL11.glClearDepth(1f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		// Define light

		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		FloatBuffer materialSpecular = BufferUtils.createFloatBuffer(4);
		materialSpecular.put(1f).put(1f).put(1f).put(1f).flip();
		
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, materialSpecular);
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 50.0f);
		
		FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
		lightPosition.put(1f).put(1f).put(1f).put(0f).flip();
		FloatBuffer lightSpecular = BufferUtils.createFloatBuffer(4);
		lightSpecular.put(1f).put(1f).put(1f).put(1f).flip();
		FloatBuffer lightDiffuse = BufferUtils.createFloatBuffer(4);
		lightDiffuse.put(1f).put(1f).put(1f).put(1f).flip();
		
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPosition);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, lightSpecular);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, lightDiffuse);
		
		FloatBuffer ambient = BufferUtils.createFloatBuffer(4);
		ambient.put(0.5f).put(0.5f).put(0.5f).put(1f).flip();
		
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, ambient);
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		
		GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
	}
	
	@Override
	public void paintGL()
	{
		try
		{
			// Clear framebuffer
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			// Viewport
			
			GL11.glViewport(0, 0, getWidth(), getHeight());
			
			// Projection matrix
			
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GLU.gluPerspective(45f, (float)getWidth()/getHeight(), 1f, 1000f);
			
			// Modelview matrix
			
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GLU.gluLookAt(10f, 10f, 10f, 0f, 0f, 0f, 0f, 1f, 0f);
			
			// Draw sphere

			GL11.glPushMatrix();
			{
				GL11.glColor3f(0f,0f,1f);
				new Sphere().draw(1f, 100, 100);
			}
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			{
				GL11.glColor3f(1f,0f,0f);
				GL11.glTranslatef(5f * (float) Math.random(), 0f, -5f * (float) Math.random());
				new Cylinder().draw(1f, 1f, 2f, 100, 100);
			}	
			GL11.glPopMatrix();
			
			GL11.glPushMatrix();
			{
				GL11.glColor3f(0f,1f,0f);
				GL11.glTranslatef(-5f * (float) Math.random(), 0f, 5f * (float) Math.random());
				new Disk().draw(1f, 2f, 100, 100);
			}	
			GL11.glPopMatrix();
			
			// Finish rendering
			
			GL11.glFinish();
			
			// Swap buffers
			
			swapBuffers();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}