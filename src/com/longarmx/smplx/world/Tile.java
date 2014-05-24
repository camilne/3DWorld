package com.longarmx.smplx.world;

import com.base.engine.TextureRegion;
import com.longarmx.smplx.Spritesheet;

public class Tile
{
	
	private static Tile[] tiles = new Tile[]{
				new Tile(0, 0)
	};
	
	private TextureRegion region;
			
	public Tile(int x, int y)
	{
		region = Spritesheet.get(x, y, 255, 255);
	}
	
	public static Tile getTile(int id)
	{
		return tiles[id];
	}
	
	public TextureRegion getRegion()
	{
		return region;
	}
	
}
