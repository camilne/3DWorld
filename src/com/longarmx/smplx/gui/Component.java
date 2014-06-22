package com.longarmx.smplx.gui;

import com.base.engine.Input;
import com.base.engine.Mesh;
import com.base.engine.Vector2f;
import com.base.engine.Vector3f;
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
	private Mesh mesh;
	
	public Component(float x, float y, float width, float height)
	{
		this(new Vector2f(x, y), new Vector2f(width, height));
	}
	
	public Component(Vector2f pos, Vector2f size)
	{
		this.pos = pos;
		this.size = size;
		
		vertices[0] = new Vertex(new Vector3f(pos.getX(), pos.getY(), 0));
		vertices[1] = new Vertex(new Vector3f(pos.getX() + size.getX(), pos.getY(), 0));
		vertices[2] = new Vertex(new Vector3f(pos.getX() + size.getX(), pos.getY() + size.getY(), 0));
		vertices[3] = new Vertex(new Vector3f(pos.getX(), pos.getY() + size.getY(), 0));
		
		mesh = new Mesh(vertices, indices);
	}
	
	public void input()
	{
		
	}
	
	public void update()
	{
		float mouseX = Input.getMousePosition().getX();
		float mouseY = Input.getMousePosition().getY();
		
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
