package com.base.engine;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public class Texture
{
	private int id;
	
	public Texture(String fileName)
	{
		this(loadTexture(fileName));
	}
	
	public Texture(int id)
	{
		this.id = id;
	}
	
	public void bind()
	{
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public int getID()
	{
		return id;
	}
	
	private static int loadTexture(String fileName)
	{
		//String[] splitArray = fileName.split("\\.");
		//String ext = splitArray[splitArray.length - 1];
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
			
			int textureID = glGenTextures();
			
			int[] pixels = new int[bimg.getWidth() * bimg.getHeight() * 4];
			bimg.getRGB(0, 0, bimg.getWidth(), bimg.getHeight(), pixels, 0, bimg.getWidth());
			
			ByteBuffer buffer = Util.createByteBuffer(pixels.length);
			
			for(int y = 0; y < bimg.getHeight(); y++)
			{
				for(int x = 0; x < bimg.getWidth(); x++)
				{
					int pixel = pixels[y * bimg.getWidth() + x];
					buffer.put((byte) ((pixel >> 16) & 0xFF));
					buffer.put((byte) ((pixel >> 8) & 0xFF));
					buffer.put((byte) (pixel & 0xFF));
					buffer.put((byte) ((pixel >> 24) & 0xFF));
				}
			}
			
			buffer.flip();
			
			glBindTexture(GL_TEXTURE_2D, textureID);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, bimg.getWidth(), bimg.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			
			return textureID;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		
		return 0;
	}
}
