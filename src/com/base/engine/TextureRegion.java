package com.base.engine;


public class TextureRegion
{
	
	private float s, t, u, v, width, height;
	private Vector2f st, ut, uv, sv;
	private Texture texture;
	
	public TextureRegion(Texture texture, int x, int y, int width, int height)
	{
		this.texture = texture;
		
		float invTexWidth = 1f / texture.getWidth();
		float invTexHeight = 1f / texture.getHeight();
		
		this.s = x * invTexWidth;
		this.t = y * invTexHeight;
		this.u = (x + width) * invTexWidth;
		this.v = (y + height) * invTexHeight;
		this.width = Math.abs(width);
		this.height = Math.abs(height);
		
		this.st = new Vector2f(s, t);
		this.ut = new Vector2f(u, t);
		this.uv = new Vector2f(u, v);
		this.sv = new Vector2f(s, v);

	}

	public void bind()
	{
		texture.bind();
	}

	public float getS()
	{
		return s;
	}

	public float getT()
	{
		return t;
	}

	public float getU()
	{
		return u;
	}

	public float getV()
	{
		return v;
	}

	public float getWidth()
	{
		return width;
	}

	public float getHeight()
	{
		return height;
	}

	public Texture getTexture()
	{
		return texture;
	}

	public Vector2f getST()
	{
		return st;
	}

	public Vector2f getUT()
	{
		return ut;
	}
	
	public Vector2f getUV()
	{
		return uv;
	}

	public Vector2f getSV()
	{
		return sv;
	}

}
