package com.base.engine;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class Mesh
{
	private int vbo;
	private int ibo;
	private int size;
	
	private Vertex[] vertices;
	private int[] indices;
	
	private float x;
	private float y;
	private float z;
	
	private boolean storeData = false;
	
	public Mesh(String fileName)
	{
		this(fileName, false);
	}
	
	public Mesh(String fileName, boolean storeData)
	{
		initMeshData();
		this.storeData = storeData;
		loadMesh(fileName);
	}
	
	public Mesh(Vertex[] vertices, int[] indices)
	{
		this(vertices, indices, false);
	}
	
	public Mesh(Vertex[] vertices, int[] indices, boolean storeData)
	{
		this(vertices, indices, false, storeData);
	}
	
	public Mesh(Vertex[] vertices, int[] indices, boolean calcNormals, boolean storeData)
	{
		initMeshData();
		addVertices(vertices, indices, calcNormals);
		this.storeData = storeData;
	}
	
	public Mesh(FloatBuffer vertices, IntBuffer indices)
	{
		initMeshData();
		addVertices(vertices, indices);
	}
	
	private void initMeshData()
	{
		vbo = glGenBuffers();
		ibo = glGenBuffers();
		size = 0;
	}
	
	private void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals)
	{
		if(calcNormals)
			calcNormals(vertices, indices);
		
		if(storeData)
		{
			this.vertices = vertices;
			this.indices = indices;
		}
		
		this.x = vertices[0].getPos().getX();
		this.y = vertices[0].getPos().getY();
		this.z = vertices[0].getPos().getZ();
		
		addVertices(Util.createFlippedBuffer(vertices), Util.createFlippedBuffer(indices));
	}
	
	private void addVertices(FloatBuffer vertices, IntBuffer indices)
	{
		size = indices.capacity();
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
	}
	
	public void updateVertices(Vertex[] vertices, boolean calcNormals)
	{
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);
	}
	
	public void draw()
	{
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
	}
	
	public static void calcNormals(Vertex[] vertices, int[] indices)
	{
		for(int i = 0; i < indices.length; i += 3)
		{
			int i0 = indices[i];
			int i1 = indices[i + 1];
			int i2 = indices[i + 2];
			
			Vector3f v1 = vertices[i1].getPos().sub(vertices[i0].getPos());
			Vector3f v2 = vertices[i2].getPos().sub(vertices[i0].getPos());
			
			Vector3f normal = v1.cross(v2).normalized();
			
			vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
			vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
			vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
		}
		
		for(int i = 0; i < vertices.length; i++)
			vertices[i].setNormal(vertices[i].getNormal().normalized());
	}
	
	private Mesh loadMesh(String fileName)
	{
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];

		if(!ext.equals("obj"))
		{
			System.err.println("Error: '" + ext + "' file format not supported for mesh data.");
			new Exception().printStackTrace();
			System.exit(1);
		}

		OBJModel test = new OBJModel("./res/models/" + fileName);
		IndexedModel model = test.toIndexedModel();
		model.calcNormals();

		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		for(int i = 0; i < model.getPositions().size(); i++)
		{
			vertices.add(new Vertex(model.getPositions().get(i),
					model.getTexCoords().get(i),
					model.getNormals().get(i)/*,
					model.getTangents().get(i)*/));
		}

		Vertex[] vertexData = new Vertex[vertices.size()];
		vertices.toArray(vertexData);

		Integer[] indexData = new Integer[model.getIndices().size()];
		model.getIndices().toArray(indexData);

		addVertices(vertexData, Util.toIntArray(indexData), false);

		return null;
	}
	
	public Vertex[] getVertices()
	{
		return vertices;
	}
	
	public int[] getIndices()
	{
		return indices;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public float getZ()
	{
		return z;
	}
	
	public void dispose()
	{
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDeleteBuffers(vbo);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDeleteBuffers(ibo);
	}
}
