package com.longarmx.smplx;

import com.base.engine.Texture;
import com.base.engine.TextureRegion;

public class Spritesheet
{
	
	private static Texture texture;
	
	public static TextureRegion get(int x, int y, int width, int height)
	{
		if(texture == null)
			texture = new Texture("spritesheet.png");
		return new TextureRegion(texture, x, y, width, height);
	}
	
	public static void dispose()
	{
		texture.dispose();
	}
	
}
