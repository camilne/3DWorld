package com.longarmx.smplx.world;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.base.engine.Input;
import com.base.engine.Shader;
import com.base.engine.Transform;
import com.base.engine.Vector3f;
import com.longarmx.smplx.Spritesheet;
import com.longarmx.smplx.noise.SimplexNoise;

public class World
{
	private static final int MAX_CHUNKS_LOADED_PER_FRAME = 15;
	
	public static int width = 100;
	public static int height = 100;
	public static int viewRange = 5;
	public static int loadRange = 7;
	
	private WorldData data;
	private int chunksLoaded = 0;
	private Shader skyboxShader;

	
	private volatile ArrayList<Chunk> chunks = new ArrayList<Chunk>();
	private static volatile Vector3f playerPos = new Vector3f(0, 0, 0);
	
	public World()
	{
		data = new WorldData();
		Chunk.loadTextures();
		Tile.getTile(0);
		
		
		skyboxShader = new Shader("skybox.vs", "skybox.fs");
		skyboxShader.compileShader();
		skyboxShader.addUniform("projectedTransform");
		
		load();
	}
	
	public void load()
	{
		generate();
	}
	
	private void generate()
	{
		data.tNoise = new SimplexNoise(data.largestFeature, data.persistence);
		data.fNoise = new SimplexNoise(100, .2f);
	}
	
	private void load(int largestFeature, float persistence)
	{
		data.largestFeature = Math.max(largestFeature, 10);
		data.persistence = Math.max(persistence, .1f);
		load();
	}
	
	private void loadChunk(int x, int z)
	{
		if(chunksLoaded++ < MAX_CHUNKS_LOADED_PER_FRAME)
			chunks.add(ChunkFactory.createChunk(data, x, z));
	}
	
	private void unloadChunk(Chunk chunk)
	{
		if(chunk != null)
		{
			chunk.dispose();
			chunks.remove(chunk);
		}
	}
	
	public void input()
	{
		if(Input.isKeyPressed(Input.KEY_R))
			load();
		
		if(Input.isKeyPressed(Input.KEY_PERIOD))
			load(data.largestFeature+20, data.persistence);
		if(Input.isKeyPressed(Input.KEY_COMMA))
			load(data.largestFeature-20, data.persistence);
		if(Input.isKeyPressed(Input.KEY_SEMICOLON))
			load(data.largestFeature, data.persistence+.1f);
		if(Input.isKeyPressed(Input.KEY_L))
			load(data.largestFeature, data.persistence-.1f);
		if(Input.isKeyPressed(Input.KEY_LBRACKET))
			if(viewRange > 1)
				viewRange--;
		if(Input.isKeyPressed(Input.KEY_RBRACKET))
			if(viewRange < 20)
				viewRange++;
	}
	
	public void update(Vector3f plr) // Aux Thread
	{
		chunksLoaded = 0;
		Chunk.updatePlrPos(plr);
		playerPos = plr;
		
		for(int i = (int)plr.x / Chunk.size - viewRange; i < (int)plr.x / Chunk.size + viewRange; i++)
			for(int j = (int)plr.z/ Chunk.size - viewRange; j < (int)plr.z/ Chunk.size + viewRange; j++)
				if(withinRange(plr.x, plr.z, i * Chunk.size, j * Chunk.size))
					if(getChunk(i * Chunk.size, j * Chunk.size) == null)
						loadChunk(i, j);
	}
	
	public void render(Transform transform) // Main Thread
	{
		Spritesheet.bind();
		GL11.glDisable(GL11.GL_BLEND);
		for(int i = chunks.size() - 1; i > 0; i--)
		{				
			if(!chunks.get(i).isCreated()) // Need to create in GL thread because we're uploading mesh data
				chunks.get(i).create();
			
			chunks.get(i).render();
			
			if(!withinRange(playerPos.x, playerPos.z, chunks.get(i).getX(), chunks.get(i).getZ()))
				unloadChunk(chunks.get(i)); // Similarly, we have to unload chunks in the GL thread
		}
		
		skyboxShader.bind();
		skyboxShader.setUniform("projectedTransform", transform.getProjectedTransformation());
		Skybox.instance.setCenter(playerPos);
		Skybox.instance.render();
		
		Spritesheet.bind();
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_CULL_FACE);
		
		for(int i = chunks.size() - 1; i > 0; i--)
			chunks.get(i).renderTransparency();
		
		GL11.glEnable(GL11.GL_CULL_FACE);
	}
	
	private boolean withinRange(float x, float z, int i, int j)
	{
		return Math.sqrt((x-i)*(x-i)+(z-j)*(z-j)) < viewRange * Chunk.size;
	}
	
	private float getWorldMap(int x, int z)
	{
		int x1 = (int)Math.floor(x/Chunk.size) * Chunk.size;
		int z1 = (int)Math.floor(z/Chunk.size) * Chunk.size;
		
		if(x < 0)
			x1-=Chunk.size;
		
		if(z < 0)
			z1-=Chunk.size;
		
		Chunk chunk = getChunk(x1, z1);
		
		if(chunk != null)
		{
//			System.out.println("C(" + chunk.getX() + "," + chunk.getZ() + ") == [" + x + "," + z + "]");
			int _x = x, _z = z;
			
			if(x < 0 || z < 0)
			{
				if(x < 0)
				{
					while(_x < 0)
						_x += Chunk.size;
					_z = _z%Chunk.size;
				}
				else
				{
					while(_z < 0)
						_z += Chunk.size;
					_x = _x%Chunk.size;
				}
			}
			else
			{
				_x = _x%Chunk.size;
				_z = _z%Chunk.size;
			}
				
			
			return chunk.getMap(_x , _z);
		}
		
//		System.out.println("Error - [" + x + "," + z + "]");
		return 0;
	}
	
	private Chunk getChunk(int x, int z)
	{
		for(int i = chunks.size() - 1; i > 0; i--)
			if(chunks.get(i).getX() == x && chunks.get(i).getZ() == z)
				if(i < chunks.size() && i >= 0)
					return chunks.get(i);
		
		return null;
	}
	
	/**
	 * Creates the vertices for the triangle the player is over
	 * @param x
	 * @param z
	 * @return The triangle which the player is standing on
	 */
	private float[] getTriangle(float x, float z)
	{
		
		float[] tmp = new float[3 * 3];
		
		int xF = (int)Math.floor(x);
		int xF1 = xF + 1;
		int zF = (int)Math.floor(z);
		int zF1 = zF + 1;
		
//		System.out.println(" - BEGIN - ");
//		System.out.println(playerPos.toString());
		
		if(x + z < xF + zF + 1)
		{
			tmp[0] = xF;
			tmp[1] = getWorldMap(xF, zF);
			tmp[2] = zF;
			tmp[3] = xF1;
			tmp[4] = getWorldMap(xF1, zF);
			tmp[5] = zF;
			tmp[6] = xF;
			tmp[7] = getWorldMap(xF, zF1);
			tmp[8] = zF1;
		}
		else
		{
			tmp[0] = xF;
			tmp[1] = getWorldMap(xF, zF1);
			tmp[2] = zF1;
			tmp[3] = xF1;
			tmp[4] = getWorldMap(xF1, zF);
			tmp[5] = zF;
			tmp[6] = xF1;
			tmp[7] = getWorldMap(xF1, zF1);
			tmp[8] = zF1;
		}
		
//		System.out.println("@@ END");
		
		return tmp;
	}
	
	/**
	 * Computes a y value that is on the plane of the terrain triangle based on the x and z position
	 * @param x
	 * @param z
	 * @return The y value
	 */
	public float getTriangleY(float x, float z)
	{		
		float[] points = getTriangle(x, z);
		
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

	public void dispose()
	{
		if(chunks.size() != 0)
			for(int i = chunks.size()-1; i > 0; i--)
				unloadChunk(chunks.get(i));
	}
	
	public static Vector3f getPlayerPos()
	{
		return playerPos;
	}
}
