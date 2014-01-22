package com.longarmx.smplx.gui;

import java.util.ArrayList;

public class Screen
{
	
	private ArrayList<Component> components = new ArrayList<Component>();
	
	public Screen()
	{

	}
	
	public void input()	
	{
		
	}
	
	public void update()
	{
		for(int i = 0; i < components.size(); i++)
			components.get(i).update();
	}
	
	public void render()
	{
		for(int i = 0; i < components.size(); i++)
			components.get(i).render();
	}
	
	public void dispose()
	{
		for(int i = 0; i < components.size(); i++)
			components.get(i).dispose();
	}
	
	public void addComponents(Component... c)
	{
		for(int i = 0; i < c.length; i++)
			components.add(c[i]);
	}
	
	public void addComponent(Component c)
	{
		components.add(c);
	}
	
	public Component getComponent(int i)
	{
		return components.get(i);
	}
}
