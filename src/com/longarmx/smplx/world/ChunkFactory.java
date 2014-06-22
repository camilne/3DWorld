package com.longarmx.smplx.world;

public class ChunkFactory // Aux thread
{

	public static Chunk createChunk(WorldData data, int x, int z)
	{
		Chunk chunk = new Chunk(x, z, data);
		
		return chunk;
	}
	
}
