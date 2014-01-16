package com.longarmx.smplx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

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
    public static final double FRAME_CAP = 5000.0;
    
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
        
        @SuppressWarnings("unused")
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
                
                game.input();
                Input.update();
                
                game.update();
                
                if(frameCounter >= Time.SECOND)
                {
                    //System.out.println(frames);
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
    	
    	String jarDir = System.getProperty("user.dir");    	
    	String nativeLibDir = jarDir + File.separator + "native" + File.separator + folder;
    	
    	System.setProperty("org.lwjgl.librarypath", nativeLibDir);
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
