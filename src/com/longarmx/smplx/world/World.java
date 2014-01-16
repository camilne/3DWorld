package com.longarmx.smplx.world;

import com.base.engine.Input;
import com.base.engine.Vector3f;
import com.longarmx.smplx.noise.SimplexNoise;

public class World
{
	public static int width = 100;
	public static int height = 100;
	public static int viewRange = 5;
	
	private int largestFeature = 100;
	private float persistence = .2f;
	private float multiplier = 20;
	private SimplexNoise noise;
	
	private Chunk[][] chunks = new Chunk[width][height];
	
	public World()
	{
		noise = new SimplexNoise(largestFeature, persistence);
		
		load();
	}
	
	public void load()
	{
		generate();
	}
	
	private void generate()
	{
		noise = new SimplexNoise(largestFeature, persistence);
		
		for(int i = 0; i < chunks.length; i++)
			for(int j = 0; j < chunks[0].length; j++)
				chunks[i][j] = null;
	}
	
	private void load(int largestFeature, float persistence)
	{
		this.largestFeature = Math.max(largestFeature, 10);
		this.persistence = Math.max(persistence, .1f);
		load();
	}
	
	private void loadChunk(int x, int z)
	{
		chunks[x][z] = new Chunk(x, z, this);
	}
	
	private void unloadChunk(int x, int z)
	{
		if(chunks[x][z] != null)
		{
			chunks[x][z].dispose();
			chunks[x][z] = null;
		}
	}
	
	public void input()
	{
		if(Input.getKeyDown(Input.KEY_R))
			load();
		
		if(Input.getKeyDown(Input.KEY_PERIOD))
			load(largestFeature+20, persistence);
		if(Input.getKeyDown(Input.KEY_COMMA))
			load(largestFeature-20, persistence);
		if(Input.getKeyDown(Input.KEY_SEMICOLON))
			load(largestFeature, persistence+.1f);
		if(Input.getKeyDown(Input.KEY_L))
			load(largestFeature, persistence-.1f);
	}
	
	public void render(float x, float z)
	{
		for(int i = 0; i < chunks.length; i++)
			for(int j = 0; j < chunks[0].length; j++)
				if(withinRange(x, z, i * Chunk.size, j * Chunk.size))
					if(chunks[i][j] != null)
						chunks[i][j].render();
					else
						loadChunk(i, j);
				else
					unloadChunk(i, j);
	}
	
	private boolean withinRange(float x, float z, int i, int j)
	{
		return Math.sqrt((x-i)*(x-i)+(z-j)*(z-j)) < viewRange * Chunk.size;
	}
	
	private Vector3f getWorldMap(int x, int z)
	{
		int x1 = (int)Math.floor(x/Chunk.size);
		int z1 = (int)Math.floor(z/Chunk.size);
		
		if(chunks[x1][z1] != null)
			return chunks[x1][z1].getMap(x%Chunk.size, z%Chunk.size);
		
		return new Vector3f(x, 0, z);
	}
	
	private float getY(int x, int z)
	{
		return getWorldMap(x, z).getY();
	}
	
	private float[] getYPos(float x, float z)
	{
		
		float[] tmp = new float[3 * 3];
		
		int xF = (int)Math.floor(x);
		int xF1 = xF + 1;
		int zF = (int)Math.floor(z);
		int zF1 = zF + 1;
		
		if(x + z < xF + zF + 1)
		{
			tmp[0] = xF;
			tmp[1] = getY(xF, zF);
			tmp[2] = zF;
			tmp[3] = xF1;
			tmp[4] = getY(xF1, zF);
			tmp[5] = zF;
			tmp[6] = xF;
			tmp[7] = getY(xF, zF1);
			tmp[8] = zF1;
		}
		else
		{
			tmp[0] = xF;
			tmp[1] = getY(xF, zF1);
			tmp[2] = zF1;
			tmp[3] = xF1;
			tmp[4] = getY(xF1, zF);
			tmp[5] = zF;
			tmp[6] = xF1;
			tmp[7] = getY(xF1, zF1);
			tmp[8] = zF1;
		}
		
		return tmp;
	}
	
	public float getTriangleY(float x, float z, float defaultY)
	{
		
		if(x >= width * Chunk.size - 1 || x <= 0 || z >= height * Chunk.size - 1 || z <= 0)
			return defaultY;
		
		float[] points = getYPos(x, z);
		
		float x1 = points[0];
		float y1 = points[1];
		float z1 = points[2];
		
		float x2 = points[3];
		float y2 = points[4];
		float z2 = points[5];
		
		float x3 = points[6];
		float y3 = points[7];
		float z3 = points[8];
		
		float a = y1*(z2-z3)+y2*(z3-z1)+y3*(z1-z2);
		float b = z1*(x2-x3)+z2*(x3-x1)+z3*(x1-x2);
		float c = x1*(y2-y3)+x2*(y3-y1)+x3*(y1-y2);
		float d = -x1*(y2*z3-y3*z2)-x2*(y3*z1-y1*z3)-x3*(y1*z2-y2*z1);
		
		return (-d-a*x-c*z)/b;
	}
	
	public float getNoise(int x, int z)
	{
		return (float)noise.getNoise(x, z) * multiplier;
	}
	
	public void dispose()
	{
		for(int i = 0; i < chunks.length; i++)
			for(int j = 0; j < chunks[0].length; j++)
				if(chunks[i][j] != null)
					unloadChunk(i, j);
	}
}
