package com.longarmx.smplx.world;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Random;

import com.base.engine.Mesh;
import com.base.engine.TextureRegion;
import com.base.engine.Util;
import com.base.engine.Vector3f;
import com.base.engine.Vertex;
import com.longarmx.smplx.Spritesheet;

public class Chunk
{
	
	public static int size = 16;
	
	private static TextureRegion grassTexture;	
	private static Vector3f plr = new Vector3f(0, 0, 0);
	
	private int x;
	private int z;
	private WorldData data;
	private float[][] map = new float[size + 1][size + 1];
	private byte[][] tiles = new byte[map.length][map[0].length];
	private Mesh mesh;
	
	private FloatBuffer vertices;
	private IntBuffer indices;
	
	private boolean isCreated = false;
	
	private ArrayList<Grass> grassPatches = new ArrayList<Grass>();
	
	public Chunk(int x, int z, WorldData data)
	{
		this.x = x * size;
		this.z = z * size;
		this.data = data;

		generate();
	}
	
	private int index = 0;
	private int iindex = 0;
	
	public void generate()
	{		
		Random rand = new Random();
		
		// Generate tile types
		
				for(int i = 0; i < tiles.length; i++)
					for(int j = 0; j < tiles[0].length; j++)
					{
						byte id = 1;
						if(data.getFNoise(i + x, j + z) > .04f || data.getFNoise(i + x, j + z) < -.04f)
							id = 0;
						tiles[i][j] = id;
					}
//				
//				for(int i = 0; i < tiles.length; i++)
//					for(int j = 0; j < tiles[0].length; j++)
//					{
//						if(tiles[i][j] == 1)
//						{
//							int region = 0;
//							if(getTile(i-1, j) == 1)
//								region += 1;
//							if(getTile(i, j+1) == 1)
//								region += 2;
//							if(getTile(i+1, j) == 1)
//								region += 4;
//							if(getTile(i, j-1) == 1)
//								region += 8;
//							
//							
//						}
//					}
		
		// Generate terrain mesh
		
		for(int i = 0; i < map.length; i++)
			for(int j = 0; j < map[0].length; j++)
			{
				map[i][j] = (float)(data.getTNoise(i + x, j + z));
				
				if(data.getFNoise(i + x, j + z) > 0.05f || data.getFNoise(i + x, j + z) < -0.05f) // if it's in the middle .2
					grassPatches.add(new Grass(new Vector3f(i + x + rand.nextFloat() - 0.5f, map[i][j], j + z + rand.nextFloat() - 0.5f)));
			}
		
		index = 0;
		iindex = 0;
		
		Vertex[] tmp_vertices = new Vertex[size * size * 4];
		int[] tmp_indices = new int[tmp_vertices.length * 3/2];
		
		for(int j = 0; j < map[0].length - 1; j++)
			for(int i = 0; i < map.length - 1; i++)
				populateMeshData(tmp_vertices, i, j, tmp_indices);
		
		Mesh.calcNormals(tmp_vertices, tmp_indices);
		
		vertices = Util.createFlippedBuffer(tmp_vertices);
		indices = Util.createFlippedBuffer(tmp_indices);
	}
	
	private int getTile(int x, int y)
	{
		if(x < 0 || x >= tiles.length || y < 0 || y >= tiles[0].length)
			return 0;
		
		return tiles[x][y];
	}
	
	public static void loadTextures()
	{
		if(grassTexture == null)
			grassTexture = Spritesheet.get(257, 0, 255, 255);
	}
	
	public void create()
	{
		mesh = new Mesh(vertices, indices);
		
		for(int i = 0; i < grassPatches.size(); i++)
			grassPatches.get(i).create();
		
		vertices = null;
		indices = null;
		
		isCreated = true;
	}
	
	private void populateMeshData(Vertex[] vertices, int i, int j, int[] indices)
	{
		indices[iindex++] = 0 + index;
		indices[iindex++] = 1 + index;
		indices[iindex++] = 3 + index;
		indices[iindex++] = 1 + index;
		indices[iindex++] = 2 + index;
		indices[iindex++] = 3 + index;
		
		TextureRegion region = Tile.getTile(tiles[i][j]).getRegion();
		
		vertices[index++] = new Vertex(new Vector3f(i + x, map[i][j], j + z), region.getST());
		vertices[index++] = new Vertex(new Vector3f(i + x + 1, map[i + 1][j], j + z), region.getUT());
		vertices[index++] = new Vertex(new Vector3f(i + x + 1, map[i + 1][j + 1], j + z + 1), region.getUV());
		vertices[index++] = new Vertex(new Vector3f(i + x, map[i][j + 1], j + z + 1), region.getSV());
	}
	
	public void render()
	{
		if(!isCreated)
			return;
		
		if(mesh != null)
		{
			grassTexture.bind();
			mesh.draw();
		}
	}
	
	public void renderTransparency()
	{
		for(int i = 0; i < grassPatches.size(); i++)
			grassPatches.get(i).draw(plr);
	}
	
	public float getMap(int x, int z)
	{
//		System.out.println(plr.toString());
//		System.out.println("=" + x + " " + z);
		while(x < 0)
			x += map.length;
		
		while(z < 0)
			z += map[0].length;
		
//		System.out.println("==" + x + " " + z);
		
		return map[x][z];
	}
	
	public void dispose()
	{
		if(mesh != null)
			mesh.dispose();
		for(int i = 0; i < grassPatches.size(); i++)
			grassPatches.get(i).dispose();
	}
	
	public static void updatePlrPos(Vector3f plr)
	{
		Chunk.plr = plr;
	}

	public boolean isCreated()
	{
		return isCreated;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getZ()
	{
		return z;
	}
	
}
