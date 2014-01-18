package com.longarmx.smplx.world;

import com.base.engine.Mesh;
import com.base.engine.TextureRegion;
import com.base.engine.Vector2f;
import com.base.engine.Vector3f;
import com.base.engine.Vertex;

public class Chunk
{
	
	public static int size = 16;
	
	private int x;
	private int z;
	private World world;
	private Vector3f[][] map = new Vector3f[size + 1][size + 1];
	private byte[][] tiles = new byte[map.length][map[0].length];
	private Mesh mesh;
	
	public Chunk(int x, int z, World world)
	{
		this.x = x * size;
		this.z = z * size;
		this.world = world;
		generate();
	}
	
	private int index = 0;
	private int iindex = 0;
	
	public void generate()
	{
		for(int i = 0; i < map.length; i++)
			for(int j = 0; j < map[0].length; j++)
				map[i][j] = new Vector3f(i + x, (float)(world.getNoise(i + x, j + z)), j + z);
		
		for(int i = 0; i < tiles.length; i++)
			for(int j = 0; j < tiles[0].length; j++)
				tiles[i][j] = 0;
		
		index = 0;
		iindex = 0;
		Vertex[] vertices = new Vertex[size * size * 4];
		int[] indices = new int[vertices.length * 3/2];
		
		for(int j = 0; j < map[0].length; j++)
		{
			for(int i = 0; i < map.length; i++)
			{
				if(i != map.length - 1 && j != map[0].length - 1)
				{
					populateMeshData(vertices, i, j, indices);
				}
			}
		}
		
		mesh = new Mesh(vertices, indices, true, false);
	}
	
	private void populateMeshData(Vertex[] vertices, int i, int j, int[] indices)
	{
		indices[iindex++] = 3 + index;
		indices[iindex++] = 1 + index;
		indices[iindex++] = 0 + index;
		indices[iindex++] = 2 + index;
		indices[iindex++] = 1 + index;
		indices[iindex++] = 3 + index;
		
		TextureRegion region = Tile.getTile(tiles[i][j]).getRegion();
		
		vertices[index++] = new Vertex(map[i][j], region.getSt());
		vertices[index++] = new Vertex(map[i + 1][j], new Vector2f(region.getU(), region.getT()));
		vertices[index++] = new Vertex(map[i + 1][j + 1], region.getUv());
		vertices[index++] = new Vertex(map[i][j + 1], new Vector2f(region.getS(), region.getV()));
	}
	
	public void render()
	{
		Tile.getTile(0).getRegion().getTexture().bind();
		mesh.draw();
	}
	
	public Vector3f getMap(int x, int z)
	{
		if(x < 0 || x >= size || z < 0 || z >= size)
			return new Vector3f(x, 0, z);
		
		return map[x][z];
	}
	
	public void dispose()
	{
		mesh.dispose();
	}

}
