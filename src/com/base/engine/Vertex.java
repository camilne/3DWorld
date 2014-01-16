package com.base.engine;

public class Vertex
{
	public static final int SIZE = 8;
	
	private Vector3f pos;
	private Vector2f texCoord;
	private Vector3f normal;
	
	public Vertex(Vector3f pos)
	{
		this(pos, new Vector2f(0,0));
	}
	
	public Vertex(float x, float y, float z)
	{
		this(new Vector3f(x, y, z));
	}
	
	public Vertex(Vector3f pos, Vector2f texCoord)
	{
		this(pos, texCoord, new Vector3f(0,0,0));
	}
	
	public Vertex(float x, float y, float z, float u, float v)
	{
		this(new Vector3f(x, y, z), new Vector2f(u, v));
	}
	
	public Vertex(Vector3f pos, Vector2f texCoord, Vector3f normal)
	{
		this.pos = pos;
		this.texCoord = texCoord;
		this.normal = normal;
	}
	
	public Vertex(float x, float y, float z, float u, float v, float xn, float yn, float zn)
	{
		this(new Vector3f(x, y, z), new Vector2f(u, v), new Vector3f(xn, yn, zn));
	}

	public Vector3f getPos()
	{
		return pos;
	}

	public void setPos(Vector3f pos)
	{
		this.pos = pos;
	}

	public Vector2f getTexCoord()
	{
		return texCoord;
	}

	public void setTexCoord(Vector2f texCoord)
	{
		this.texCoord = texCoord;
	}

	public Vector3f getNormal()
	{
		return normal;
	}

	public void setNormal(Vector3f normal)
	{
		this.normal = normal;
	}
}
