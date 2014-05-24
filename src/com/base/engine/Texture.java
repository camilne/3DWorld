package com.base.engine;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public class Texture
{
	private static int boundID = 0;
	private int id;
	private int width;
	private int height;
	
	public Texture(String fileName)
	{
		this(fileName, GL_NEAREST, GL_NEAREST);
	}
	
	public Texture(String fileName, int minFilter, int magFilter)
	{
		this(loadTexture(fileName, minFilter, magFilter));
	}
	
	public Texture(int id)
	{
		this.id = id;
	}
	
	private Texture(Vector3i vec)
	{
		this(vec.getX());
		width = vec.getY();
		height = vec.getZ();
	}
	
	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, id);
		boundID = id;
	}
	
	public void dispose()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
		glDeleteTextures(id);
	}
	
	private static Vector3i loadTexture(String fileName, int minFilter, int magFilter)
	{
		String path = "res" + File.separator + "textures" + File.separator + fileName;
		
		try
		{		
			System.out.println("Loading Texture: " + fileName);
			
			BufferedImage bimg = null;
			
			try{
				bimg = ImageIO.read(new FileInputStream(new File(path)));
			}
			catch(IOException e)
			{
				e.printStackTrace();
				System.err.println("Unable to load texture:" + path);
				System.exit(1);
			}
			
			int width = bimg.getWidth();
			int height = bimg.getHeight();
			
			glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
			
			int textureID = glGenTextures();
			
			int[] pixels = new int[width * height * 4];
			bimg.getRGB(0, 0, width, height, pixels, 0, width);
			
			ByteBuffer buffer = Util.createByteBuffer(pixels.length);
			
			for(int y = 0; y < height; y++)
			{
				for(int x = 0; x < width; x++)
				{
					int pixel = pixels[y * width + x];
					buffer.put((byte) ((pixel >> 16) & 0xFF));
					buffer.put((byte) ((pixel >> 8) & 0xFF));
					buffer.put((byte) (pixel & 0xFF));
					buffer.put((byte) ((pixel >> 24) & 0xFF));
				}
			}
			
			buffer.flip();
			
			glBindTexture(GL_TEXTURE_2D, textureID);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			
			if(minFilter == GL_LINEAR_MIPMAP_LINEAR || minFilter == GL_LINEAR_MIPMAP_NEAREST)
				glGenerateMipmap(GL_TEXTURE_2D);
			
			return new Vector3i(textureID, width, height);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		return new Vector3i(0, 1, 1);
	}
	

	public int getID()
	{
		return id;
	}
	
	public boolean isBound()
	{
		return boundID == id;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
}
