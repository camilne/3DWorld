package com.longarmx.smplx.world;

import com.base.engine.Mesh;
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
		
		mesh = new Mesh(vertices, indices, true);
	}
	
	private void populateMeshData(Vertex[] vertices, int i, int j, int[] indices)
	{
		indices[iindex++] = 3 + index;
		indices[iindex++] = 1 + index;
		indices[iindex++] = 0 + index;
		indices[iindex++] = 2 + index;
		indices[iindex++] = 1 + index;
		indices[iindex++] = 3 + index;
		
		vertices[index++] = new Vertex(map[i][j], new Vector2f(0, 0));
		vertices[index++] = new Vertex(map[i + 1][j], new Vector2f(1, 0));
		vertices[index++] = new Vertex(map[i + 1][j + 1], new Vector2f(1, 1));
		vertices[index++] = new Vertex(map[i][j + 1], new Vector2f(0, 1));
	}
	
	public void render()
	{
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
