package com.longarmx.smplx.world;

import com.longarmx.smplx.noise.SimplexNoise;

public class WorldData
{

	public int largestFeature = 100;
	public float persistence = 0.2f, multiplier = 20;
	public SimplexNoise tNoise, fNoise;
	
	public float getTNoise(int x, int z)
	{
		return (float)tNoise.getNoise(x, z) * multiplier;
	}
	
	public float getFNoise(int x, int z)
	{
		return (float)fNoise.getNoise(x, z);
	}
	
}
