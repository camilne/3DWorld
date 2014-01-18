package com.longarmx.smplx.gui;

import com.base.engine.Input;
import com.base.engine.Mesh;
import com.base.engine.Vector2f;
import com.base.engine.Vertex;

public class Component
{
	private static int[] indices = {
		3, 1, 0,
		2, 1, 3
	};
	
	private boolean mouseHover, lastMouseHover;
	private Vertex[] vertices = new Vertex[4];
	private Vector2f pos, size;
//	private String name;
	private Mesh mesh;
	
	public Component(float x, float y, float width, float height, String name)
	{
		this(new Vector2f(x, y), new Vector2f(width, height), name);
	}
	
	public Component(Vector2f pos, Vector2f size, String name)
	{
		this.pos = pos;
		this.size = size;
//		this.name = name;
		
		vertices[0] = new Vertex(pos.getX(), pos.getY(), 0);
		vertices[1] = new Vertex(pos.getX() + size.getX(), pos.getY(), 0);
		vertices[2] = new Vertex(pos.getX() + size.getX(), pos.getY() + size.getY(), 0);
		vertices[3] = new Vertex(pos.getX(), pos.getY() + size.getY(), 0);

		mesh = new Mesh(vertices, indices);
	}
	
	public void input()
	{
		
	}
	
	public void update()
	{
		float mouseX = Input.getMousePosition().getX();
		float mouseY = Input.getMousePosition().getX();
		
		lastMouseHover = mouseHover;
		
		if(mouseX < pos.getX() || mouseX  > pos.getX() + size.getX() || mouseY < pos.getY() || mouseY > pos.getY() + size.getY())
			mouseHover = true;
		else
			mouseHover = false;
		
		if(mouseHover && !lastMouseHover)
			onMouseEnter();
		else if(!mouseHover && lastMouseHover)
			onMouseLeave();
	}
	
	public void render()
	{
		mesh.draw();
	}
	
	public void dispose()
	{
		mesh.dispose();
	}
	
	public void onMouseEnter(){}
	
	public void onMouseLeave(){}
	

	public Vector2f getPos()
	{
		return pos;
	}

	public void setPos(Vector2f pos)
	{
		this.pos = pos;
	}

	public Vector2f getSize()
	{
		return size;
	}

	public void setSize(Vector2f size)
	{
		this.size = size;
	}

	public boolean isMouseHover()
	{
		return mouseHover;
	}

}
