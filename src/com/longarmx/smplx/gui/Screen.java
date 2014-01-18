package com.longarmx.smplx.gui;

import org.lwjgl.opengl.GL11;

public class Screen
{
	
//	private ArrayList<Component> components = new ArrayList<Component>();
	
	private String test;
	
	public Screen()
	{
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		StringBuilder builder = new StringBuilder();
		for(int i = 32; i < 127; i++)
		{
			builder.append((char)i);
			if(i%16==15)
				builder.append("\n");
		}
		test = builder.toString();
	}
	
	public void input()	
	{
		
	}
	
	public void update()
	{
		
	}
	
	public void render()
	{
		StringRenderer.drawString(test, 150, 20, 175, .05f);
	}
	
	public void dispose()
	{
		
	}
}
