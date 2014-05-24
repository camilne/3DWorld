package com.longarmx.smplx;

import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL11.glReadPixels;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import com.base.engine.Input;
import com.base.engine.RenderUtil;
import com.base.engine.Time;
import com.base.engine.Window;

public class Main
{
	public static Main instance;
	
	public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final String TITLE = "Simplex Noise";
    public static final double FRAME_CAP = 120.0;
    
    public static int FPS = 0;
    public static int MAJOR_VERSION;
    public static int MINOR_VERSION;
    
    
    private boolean isRunning;
    private Game game;
    
    public Main()
    {
    	System.out.println("Using OpenGL Version: " + RenderUtil.getOpenGLVersion());
        RenderUtil.initGraphics();
        isRunning = false;
        
        game = new Game();
    }
    
    public void start()
    {
        if(isRunning)
            return;
        
        run();
    }
    
    public void stop()
    {
        if(!isRunning)
            return;
        
        isRunning = false;
    }
    
    private void run()
    {
        isRunning = true;
        
        int frames = 0;
        long frameCounter = 0;
        
        final double frameTime = 1.0 / FRAME_CAP;
        
        long lastTime = Time.getTime();
        double unprocessedTime = 0;
        
        while(isRunning)
        {
            boolean render = false;
            
            long startTime = Time.getTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;
            
            unprocessedTime += passedTime / (double)Time.SECOND;
            frameCounter += passedTime;
            
            
            while(unprocessedTime > frameTime)
            {
                render = true;
                
                unprocessedTime -= frameTime;
                
                if(Window.isCloseRequested())
                    stop();
                
                Time.setDelta(frameTime);
                
                if(Input.getKeyDown(Input.KEY_F1))
                	screenshot();
                
                game.input();
                Input.update();
                
                game.update();
                
                if(frameCounter >= Time.SECOND)
                {
                    //System.out.println(frames);
                	FPS = frames;
                    frames = 0;
                    frameCounter = 0;
                }
            }
            if(render)
            {
                render();
                frames++;
            }
            else
            {
                try 
                {
                    Thread.sleep(1);
                } 
                catch (InterruptedException e) 
                {
                    e.printStackTrace();
                }
            }
        }
        
        cleanUp();
    }
    
    private void render()
    {
        RenderUtil.clearScreen();
        game.render();
        Window.render();
    }
    
    private void cleanUp()
    {
    	game.dispose();
        Window.dispose();
        System.exit(0);
    }
  
    // Thanks HeroesGrave
    private static String jarDir()
    {
       try
       {
          return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
       }
       catch(URISyntaxException e)
       {
          e.printStackTrace();
          return null;
       }
    }
	
	private static void loadNatives()
	{
		String os = System.getProperty("os.name").toLowerCase();
		String folder = "";
    	
    	if (os.indexOf("win") >= 0)
    	{
			System.out.println("OS: Windows");
			folder = "windows";
		} 
    	else if (os.indexOf("mac") >= 0) 
    	{
			System.out.println("OS: Mac");
			folder = "macosx";
		} 
    	else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0) 
    	{
			System.out.println("OS: Unix or Linux");
			folder = "linux";
		} 
    	else if (os.indexOf("sunos") >= 0) 
		{
			System.out.println("OS: Solaris");
			folder = "solaris";
		} 
    	else 
		{
			System.err.println("OS NOT SUPPORTED");
			System.exit(1);
		}
    	
//    	String jarDir = System.getProperty("user.dir");    	
    	String nativeLibDir = jarDir() + File.separator + "native" + File.separator + folder;
    	
    	System.setProperty("org.lwjgl.librarypath", nativeLibDir);
	}
	
	public static void screenshot()
	{
		glReadBuffer(GL_FRONT);
		ByteBuffer buffer = BufferUtils.createByteBuffer(WIDTH * HEIGHT * 4);
		glReadPixels(0, 0, WIDTH, HEIGHT, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		for(int x = 0; x < WIDTH; x++)
			for(int y = 0; y < HEIGHT; y++)
			{
				int pixel = 4 * (x + WIDTH * y);
				int red = buffer.get(pixel) & 0xFF;
				int green = buffer.get(pixel+1) & 0xFF;
				int blue = buffer.get(pixel+2) & 0xFF;
				image.setRGB(x, HEIGHT-y-1, ((red << 16) | (green << 8) | blue));
			}
		
		try
		{
			String date = getDate();
			date = "[" + date.substring(DATE_PREFIX.length(), date.length() - SUFFIX.length()) + "]_";
			String time = getTime();
			time = "[" + time.substring(TIME_PREFIX.length(), time.length() - SUFFIX.length()) + "]";
			
			new File("screenshots" + File.separator).mkdirs();
			
			String type = ".png";
			String name = "screenshots" + File.separator + date + time + type;
			File file = new File(name);
			
			int i = 1;
			while(file.exists())
			{
				file = new File(name.substring(0, name.length() - type.length()) + String.valueOf(i) + type);
				i++;
			}
			
			file.createNewFile();
			
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
			ImageIO.write(image, "PNG", out);
			
			System.out.println("Screenshot saved as: " + file.getName());
			
			out.close();
		}
		catch(IOException e)
		{
			System.err.println("Screenshot failed to save!");
			e.printStackTrace();
		}
	}
	
	private static final String DATE_PREFIX = "DATE[";
	private static final String TIME_PREFIX = "[";
	private static final String SUFFIX = "] - ";
	
	public static String getDate()
	{
		Calendar cal = Calendar.getInstance();
		return DATE_PREFIX + (cal.get(Calendar.MONTH) + 1) + "-" + (cal.get(Calendar.DAY_OF_MONTH)) + "-" + (cal.get(Calendar.YEAR)) + SUFFIX;
	}
	
	public static String getTime()
	{
		Calendar cal = Calendar.getInstance();
		return TIME_PREFIX + cal.get(Calendar.HOUR_OF_DAY) + "-" + cal.get(Calendar.MINUTE) + "-" + cal.get(Calendar.SECOND) + SUFFIX;
	}
    
    public static void main(String[] args)
    {
    	boolean useLog = true;
	    if(useLog)
	    {
	    	try
			{
				PrintStream out = new PrintStream("log.txt");
				System.setOut(out);
				System.setErr(out);
			} catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
    	}
    	
    	System.out.println("Using Java Version: " + System.getProperty("java.version"));
    	//System.setProperty("org.lwjgl.librarypath", new File("native").getAbsolutePath());
    	loadNatives();
    	
        Window.createWindow(WIDTH, HEIGHT, TITLE, false);
        
        instance = new Main();
        instance.start();
    }

}
