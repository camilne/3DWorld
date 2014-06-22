package com.longarmx.smplx.gui;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import com.base.engine.Texture;
import com.base.engine.TextureRegion;
import com.longarmx.smplx.Main;


public class StringRenderer
{
	// The maximum width of a glyph in the texture
	public static final int glyphWidth = 38;
	// The maximum height of a glyph in the texture
	public static final int glyphHeight = 55;
	// The spacing between glyphs when rendering
	public static final float spacingX = 1.5f;
	// The spacing between lines when rendering
	public static final float spacingY = 0f;
	
	// The name or path of the font texture (however you load it)
	public static Texture texture = new Texture(0);
	// The name or path of the font file (however you load it)
	public static String fontPath = "font.fnt";
	// The x position of the first glyph in the font texture
	public static int glyphTextureX = 0;
	// The y position of the firts glyph in the font texture
	public static int glyphTextureY = 0;
	
	// All of the glyphs
	private static HashMap<Character, Glyph> glyphs = new HashMap<Character, Glyph>();
	
	/**
	 * Creates the String Renderer (loads glyphs and defines variables)
	 * @param textureName The name or path of the font texture (however you load it)
	 * @param fontPath The name or path of the font file (however you load it)
	 */
	public static void create(Texture texture, String fontPath)
	{
		create(texture, fontPath, 1, 2 * glyphHeight);
	}
	
	/**
	 * Creates the String Renderer (loads glyphs and defines variables)
	 * @param textureName The name or path of the font texture (however you load it)
	 * @param fontPath The name or path of the font file (however you load it)
	 * @param glyphTextureX The x position of the first glyph in the font texture
	 * @param glyphTextureY The y position of the first glyph in the font texture
	 */
	public static void create(Texture texture, String fontPath, int glyphTextureX, int glyphTextureY)
	{
		StringRenderer.texture = texture;
		StringRenderer.fontPath = fontPath;
		StringRenderer.glyphTextureX = glyphTextureX;
		StringRenderer.glyphTextureY = glyphTextureY;
		
		glyphs.clear();
		loadGlyphs();
	}
	
	/**
	 * Loads glyphs defined in the fnt file and puts them into a HashMap
	 */
	private static void loadGlyphs()
	{
		try{
			// Loads file and splits it into lines\
			String file = loadFromFile(fontPath);
			
			String[] lines = file.split("\n");
			
			for(int i = 0; i < lines.length; i++)
			{
				// Creates tokens from lines {character, width}
				String[] tokens = lines[i].split("->");
	
				char c = tokens[0].charAt(0);
				int w = Integer.valueOf(tokens[1].substring(0, tokens[1].length()-1));
				
				// Adds the glyph to the HashMap
				add(c, w);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Renders a string on a single line
	 * @param string The string to render
	 * @param x The starting x position
	 * @param y The starting y position
	 * @param scale The scale to draw at (normal=1.0f)
	 */
	public static void drawString(String string, float x, float y, float z, float scale, boolean centered)
	{
		texture.bind();
		
		if(string.length() == 0)
			return;
		
		scale /= 1000;
		x-= (Main.WIDTH/2 + 350) * scale;
		y-= (Main.HEIGHT + 260) * scale;
		
		float currentXOffset = 0;
		float currentYOffset = 0;
		float centeredOffset = 0;
		
		if(centered)
		{
			float temp = 0;
			for(int i = 0; i < string.length(); i++)
				temp += glyphs.get(string.charAt(i)).getWidth() * scale + spacingX * scale;
			centeredOffset = -temp/2;
		}
		
		for(int i = 0; i < string.length(); i++)
		{
			char character = string.charAt(i);
			
			if(character == '\n')
			{
				currentYOffset -= glyphHeight * scale + spacingY;
				centeredOffset = 0;
				continue;
			}
			
			if(!glyphs.containsKey(character))
				continue;
			
			
			Glyph glyph = glyphs.get(character);
			glyph.render(x + currentXOffset + centeredOffset, y + currentYOffset, z, scale);
			currentXOffset += glyph.getWidth() * scale + spacingX * scale;
		}
	}
	
	/**
	 * Renders a string and creates a new line when it goes past the specified position
	 * @param string The string to render
	 * @param batch The current sprite batch
	 * @param x The starting x position
	 * @param y The starting y position
	 * @param scale The scale (normal=1.0f)
	 * @param wrapWidth How wide the string should be until it creates a new line
	 */
	public static void drawWrappedString(String string, float x, float y, float z, float scale, int wrapWidth)
	{
		texture.bind();
		
		scale /= 1000;
		float currentXOffset = 0;
		float currentYOffset = 0;
		for(int i = 0; i < string.length(); i++)
		{
			char character = string.charAt(i);
			
			if(character == '\n')
			{
				currentYOffset -= glyphHeight * scale + spacingY;
				currentXOffset = 0;
				continue;
			}
			
			if(!glyphs.containsKey(character))
				continue;
			
			if(currentXOffset + glyphs.get(character).getWidth() > wrapWidth / scale)
			{
				currentXOffset = 0;
				currentYOffset -= glyphHeight * scale + spacingY;
			}
			
			Glyph glyph = glyphs.get(character);
			glyph.render(x + currentXOffset, y + currentYOffset, z, scale);
			currentXOffset += glyph.getWidth() * scale + spacingX * scale;
		}
	}
	
	/**
	 * 
	 * @param c The character to add to the HashMap
	 * @param width The width of the glyph
	 */
	private static void add(char c, int width)
	{
		glyphs.put(c, new Glyph(c, width, get(glyphs.size(), width)));
	}
	
	/**
	 * 
	 * @param i The glyph number
	 * @param width The width of the glyph
	 * @return The texture region for the correct glyph
	 */
	private static TextureRegion get(int i, int width)
	{
		// Transferring from libgdx
		return new TextureRegion(texture, glyphTextureX + (i%16) * glyphWidth, glyphTextureY + (int)Math.floor(i / 16) * glyphHeight, width, glyphHeight);
	}
	
	private static String loadFromFile(String fileName)
	{
		String res = "";
		try
		{
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(new File(fontPath)).useDelimiter("\\A");
			res = scanner.next();
			scanner.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return res;
	}
	
}
