package com.longarmx.smplx.world;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.base.engine.Mesh;
import com.base.engine.TextureRegion;
import com.base.engine.Util;
import com.base.engine.Vector3f;
import com.base.engine.Vertex;
import com.longarmx.smplx.Spritesheet;

public class Grass
{	
	private Mesh mesh;
	
	private boolean isCreated = false;
	private FloatBuffer vertices;
	private IntBuffer indices;
	
	public Grass(Vector3f position)
	{

		Vertex[] tmp_vertices = new Vertex[8];//translateVertices(grassMeshTypes[type][lod].getVertices(), position);
		
		TextureRegion region = Spritesheet.get(0, 257, 255, 255);
		
		int i = 0;
		tmp_vertices[i++] = new Vertex(position.add(new Vector3f(-0.5f, 0, -0.5f)), region.getSV());
		tmp_vertices[i++] = new Vertex(position.add(new Vector3f(0.5f, 0, 0.5f)), region.getUV());
		tmp_vertices[i++] = new Vertex(position.add(new Vector3f(0.5f, 1, 0.5f)), region.getUT());
		tmp_vertices[i++] = new Vertex(position.add(new Vector3f(-0.5f, 1,- 0.5f)), region.getST());
		
		tmp_vertices[i++] = new Vertex(position.add(new Vector3f(0.5f, 0, -0.5f)), region.getSV());
		tmp_vertices[i++] = new Vertex(position.add(new Vector3f(-0.5f, 0, 0.5f)), region.getUV());
		tmp_vertices[i++] = new Vertex(position.add(new Vector3f(-0.5f, 1, 0.5f)), region.getUT());
		tmp_vertices[i++] = new Vertex(position.add(new Vector3f(0.5f, 1,- 0.5f)), region.getST());
		 
		int[] tmp_indices = {0, 1, 3, 1, 2, 3, 4, 5, 7, 5, 6, 7};//grassMeshTypes[type][lod].getIndices();
		indices = Util.createFlippedBuffer(tmp_indices);
		
		Mesh.calcNormals(tmp_vertices, tmp_indices);
		vertices = Util.createFlippedBuffer(tmp_vertices);
	}
	
	public void create()
	{
		mesh = new Mesh(vertices, indices);
		isCreated = true;
	}
	
	public void draw(Vector3f plr)
	{
		if(!isCreated)
			return;

		mesh.draw();
	}

	
	public void dispose()
	{
		if(mesh != null)
			mesh.dispose();
	}
	
	public boolean isCreated()
	{
		return isCreated;
	}

}
