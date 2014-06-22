package com.longarmx.smplx.gui;

import com.base.engine.Mesh;
import com.base.engine.TextureRegion;
import com.base.engine.Vector3f;
import com.base.engine.Vertex;


public class Glyph
{
	
	private static int[] indices = {
		0, 1, 3,
		3, 1, 2
	};
	
	private char character;
	private int width;
	private Vertex[] vertices = new Vertex[4];
	private Mesh mesh;
	
	public static final int glyphHeight = 55;
	
	public Glyph(char character, int width, TextureRegion region)
	{
		this.character = character;
		this.width = width;
		
		Vector3f zero = new Vector3f(0, 0, 0);
		
		vertices[0] = new Vertex(zero, region.getSV());
		vertices[1] = new Vertex(zero, region.getUV());
		vertices[2] = new Vertex(zero, region.getUT());
		vertices[3] = new Vertex(zero, region.getST());
	}
	
	public void render(float x, float y, float z, float scale)
	{
		vertices[0].setPos(new Vector3f(x, y, 0));
		vertices[1].setPos(new Vector3f(x + width * scale, y, 0));
		vertices[2].setPos(new Vector3f(x + width * scale, y + glyphHeight * scale, 0));
		vertices[3].setPos(new Vector3f(x, y + glyphHeight * scale, 0));
		
		mesh = new Mesh(vertices, indices);
		
		mesh.draw();
	}

	public char getCharacter()
	{
		return character;
	}

	public int getWidth()
	{
		return width;
	}

}
