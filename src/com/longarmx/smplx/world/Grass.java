package com.longarmx.smplx.world;

import static org.lwjgl.opengl.GL11.GL_LINEAR;

import java.util.Random;

import com.base.engine.Mesh;
import com.base.engine.Texture;
import com.base.engine.Vector3f;
import com.base.engine.Vertex;

public class Grass
{
	private static Texture grassBlade;
	private static Mesh[][] grassMeshTypes;
	private static Random random;
	
	private Vector3f position;
	private int type;
	private int lod;
	private Mesh mesh;
	
	public Grass(Vector3f position)
	{
		this.position = position;
		
		if(grassBlade == null)
			grassBlade = new Texture("grassBlade2.png", GL_LINEAR, GL_LINEAR);
		
		if(grassMeshTypes == null)
		{
			grassMeshTypes = new Mesh[3][3];
			for(int i = 0; i < grassMeshTypes.length; i++)
			{
				grassMeshTypes[i][0] = new Mesh("grass_patch_low" + i + ".obj", true);
				grassMeshTypes[i][1] = new Mesh("grass_patch_mid" + i + ".obj", true);
				grassMeshTypes[i][2] = new Mesh("grass_patch_high" + i + ".obj", true);
			}
		}
		
		if(random == null)
			random = new Random();
		
		type = random.nextInt(grassMeshTypes.length);
		lod = 1;
		
		mesh = new Mesh(translateVertices(grassMeshTypes[type][lod].getVertices()), grassMeshTypes[type][lod].getIndices());
	}
	
	public void draw(float x, float y, float z)
	{
		int lod = lod(x, y, z);
		if(this.lod != lod)
		{
			this.lod = lod;
			mesh = new Mesh(translateVertices(grassMeshTypes[type][lod].getVertices()), grassMeshTypes[type][lod].getIndices());
		}
		
		grassBlade.bind();
		mesh.draw();
	}
	
	private Vertex[] translateVertices(Vertex[] vertices)
	{
		Vertex[] tmp = new Vertex[vertices.length];
		for(int i = 0; i < tmp.length; i++)
			tmp[i] = new Vertex(vertices[i].getPos().add(position), vertices[i].getTexCoord(), vertices[i].getNormal());
		return tmp;
	}
	
	private int lod(float x, float y, float z)
	{
		float xx = position.getX();
		float yy = position.getY();
		float zz = position.getZ();
		
		float distance = (float) Math.sqrt((xx - x) * (xx - x) + (yy - y) * (yy - y) + (zz - z) * (zz - z));
		
		if(distance < 3)
			return 2;
		else if(distance < 50)
			return 1;
		else
			return 0;
	}

}
