package com.base.engine;


public class TextureRegion
{
	
	private float s, t, u, v, width, height;
	private Vector2f st, uv;
	private Texture texture;
	private Mesh mesh;
	
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
		this.uv = new Vector2f(u, v);
		
		mesh = new Mesh(new Vertex[]{
				new Vertex(new Vector3f(0, 0, 0), st),
				new Vertex(new Vector3f(1, 0, 0), new Vector2f(u, t)),
				new Vertex(new Vector3f(1, 1, 0), uv),
				new Vertex(new Vector3f(0, 1, 0), new Vector2f(s, v))
		}, new int[]
				{
				3, 1, 0,
				2, 1, 3
				});
	}
	
	
	
	public void render(float x, float y, float z, float width, float height, boolean single)
	{
		texture.bind();
		
		mesh.updateVertices(new Vertex[]{
				new Vertex(new Vector3f(x + width, y + height, z), new Vector2f(u, t)),
				new Vertex(new Vector3f(x, y + height, z), new Vector2f(s, t)),
				new Vertex(new Vector3f(x, y, z), new Vector2f(s, v)),
				new Vertex(new Vector3f(x + width, y, z), new Vector2f(u, v))
		}, false, true);
		
		mesh.draw();
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

	public Vector2f getSt()
	{
		return st;
	}

	public Vector2f getUv()
	{
		return uv;
	}

}
