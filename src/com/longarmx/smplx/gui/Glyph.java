package com.longarmx.smplx.gui;

import com.base.engine.TextureRegion;



public class Glyph
{
	
	private char character;
	private int width;
	private TextureRegion region;
	
	public static final int glyphHeight = 55;
	
	public Glyph(char character, int width, TextureRegion region)
	{
		this.character = character;
		this.width = width;
		this.region = region;
	}
	
	public void render(float x, float y, float z, float scale)
	{
		region.render(x, y, z, width * scale, Glyph.glyphHeight * scale, true);
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
