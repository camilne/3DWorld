package com.longarmx.smplx.world;

import org.lwjgl.opengl.GL11;

import com.base.engine.Mesh;
import com.base.engine.Texture;
import com.base.engine.TextureRegion;
import com.base.engine.Vector3f;
import com.base.engine.Vertex;

public class Skybox
{
	public static final Skybox instance = new Skybox();
	
	private Texture texture;
	private TextureRegion[] regions = new TextureRegion[6]; // F R B L U D
	private Mesh mesh;
	
	private Vertex[] vertices = new Vertex[24];
	private int[] indices = new int[36];
	
	private int size = 5000;
	
	private Skybox()
	{
		texture = new Texture("Above_the_Sea.png", GL11.GL_NEAREST, GL11.GL_NEAREST);
		int width = texture.getWidth()/4;
		int height = texture.getHeight()/3;
		regions[0] = new TextureRegion(texture, 1, height, width, height);
		regions[1] = new TextureRegion(texture, width, height, width, height);
		regions[2] = new TextureRegion(texture, width*2, height, width, height);
		regions[3] = new TextureRegion(texture, width*3, height, width, height);
		regions[4] = new TextureRegion(texture, width+1, 1, width-2, height-1);
		regions[5] = new TextureRegion(texture, width, height*2, width, height);
		
		Vector3f zero = new Vector3f(0, 0, 0);
		
		int index = 0;
		for(int i = 0; i < indices.length; i+=6)
		{
			indices[i] = 0 + index;
			indices[i+1] = 1 + index;
			indices[i+2] = 3 + index;
			indices[i+3] = 1 + index;
			indices[i+4] = 2 + index;
			indices[i+5] = 3 + index;
			index += 4;
		}
		
		for(int j = 0; j < vertices.length; j+=4)
		{
			vertices[j] = new Vertex(zero, regions[j/4].getSV());
			vertices[j+1] = new Vertex(zero, regions[j/4].getUV());
			vertices[j+2] = new Vertex(zero, regions[j/4].getUT());
			vertices[j+3] = new Vertex(zero, regions[j/4].getST());
		}

		setCenter(new Vector3f(0, 0, 0));
	}
	
	public void setCenter(Vector3f position)
	{
		int j = 0;
		
		position = position.sub(new Vector3f(size/2, 0, size/2));
		
		vertices[j++].setPos(position.add(new Vector3f(size, -size/2, 0)));
		vertices[j++].setPos(position.add(new Vector3f(0, -size/2, 0)));
		vertices[j++].setPos(position.add(new Vector3f(0, size/2, 0)));
		vertices[j++].setPos(position.add(new Vector3f(size, size/2, 0)));
		
		vertices[j++].setPos(position.add(new Vector3f(0, -size/2, 0)));
		vertices[j++].setPos(position.add(new Vector3f(0, -size/2, size)));
		vertices[j++].setPos(position.add(new Vector3f(0, size/2, size)));
		vertices[j++].setPos(position.add(new Vector3f(0, size/2, 0)));
		
		vertices[j++].setPos(position.add(new Vector3f(0, -size/2, size)));
		vertices[j++].setPos(position.add(new Vector3f(size, -size/2, size)));
		vertices[j++].setPos(position.add(new Vector3f(size, size/2, size)));
		vertices[j++].setPos(position.add(new Vector3f(0, size/2, size)));
		
		vertices[j++].setPos(position.add(new Vector3f(size, -size/2, size)));
		vertices[j++].setPos(position.add(new Vector3f(size, -size/2, 0)));
		vertices[j++].setPos(position.add(new Vector3f(size, size/2, 0)));
		vertices[j++].setPos(position.add(new Vector3f(size, size/2, size)));
		
		vertices[j++].setPos(position.add(new Vector3f(0, size/2, 0)));
		vertices[j++].setPos(position.add(new Vector3f(0, size/2, size)));
		vertices[j++].setPos(position.add(new Vector3f(size, size/2, size)));
		vertices[j++].setPos(position.add(new Vector3f(size, size/2, 0)));
		
		
		vertices[j++].setPos(position.add(new Vector3f(size, -size/2, 0)));
		vertices[j++].setPos(position.add(new Vector3f(size, -size/2, size)));
		vertices[j++].setPos(position.add(new Vector3f(0, -size/2, size)));
		vertices[j++].setPos(position.add(new Vector3f(0, -size/2, 0)));
		
		mesh = new Mesh(vertices, indices, false, false);
	}
	
	public void render()
	{
		texture.bind();
		mesh.draw();
	}
	
	public void dispose()
	{
		texture.dispose();
		mesh.dispose();
	}

}
