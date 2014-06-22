package com.base.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import com.longarmx.smplx.Main;

public class Window 
{
	public static void createWindow(int width, int height, String title, boolean fullscreen)
	{
		Display.setTitle(title);
		try 
		{
			if(fullscreen)
				Display.setFullscreen(true);
			else
				Display.setDisplayMode(new DisplayMode(width, height));
			
			Display.create();
			Keyboard.create();
			Mouse.create();
		} 
		catch (LWJGLException e) 
		{
			e.printStackTrace();
		}
		
		Main.MAJOR_VERSION = RenderUtil.getMajorVersion();
		Main.MINOR_VERSION = RenderUtil.getMinorVersion();
		
		if(Main.MAJOR_VERSION < 3 && Main.MINOR_VERSION < 3)
		{
			try
			{
				Display.destroy();
				Display.setFullscreen(fullscreen);
				ContextAttribs attribs = new ContextAttribs(3, 3);
				attribs.withProfileCore(true);
				attribs.withForwardCompatible(true);
				Display.create(new PixelFormat(), attribs);
			}
			catch (LWJGLException e) 
			{
				e.printStackTrace();
				System.err.println("OpenGL version: " + RenderUtil.getOpenGLVersion() + " not supported. Requires V:3.3");
				System.exit(1);
			}
		}
	}
	
	public static void render()
	{
		Display.update();
	}
	
	public static void dispose()
	{
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
	
	public static boolean isCloseRequested()
	{
		return Display.isCloseRequested();
	}
	
	public static int getWidth()
	{
		return Display.getDisplayMode().getWidth();
	}
	
	public static int getHeight()
	{
		return Display.getDisplayMode().getHeight();
	}
	
	public static String getTitle()
	{
		return Display.getTitle();
	}
}
