package com.longarmx.smplx;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import org.lwjgl.opengl.Display;

import com.base.engine.BaseLight;
import com.base.engine.DirectionalLight;
import com.base.engine.Input;
import com.base.engine.Material;
import com.base.engine.RenderUtil;
import com.base.engine.Texture;
import com.base.engine.Transform;
import com.base.engine.Vector3f;
import com.base.engine.Window;
import com.longarmx.smplx.gui.StringRenderer;
import com.longarmx.smplx.world.Skybox;
import com.longarmx.smplx.world.World;

public class Game
{
	
	private WorldShader worldShader;
	private HudShader hudShader;
	private Material material;
	private Transform transform;
	private Texture fontTexture;

	private World world;
	private volatile Player player;
	private MainScreen hud;
	
	private Thread auxThread;
	
	public boolean gameFocusable = true;
	
	public Game()
	{
		worldShader = WorldShader.getInstance();
		hudShader = HudShader.getInstance();
		material = new Material(null, new Vector3f(.1f, .1f, .1f), 1, 8);
		transform = new Transform();

		world = new World();
		player = new Player(world);
		hud = new MainScreen();
		
		Transform.setProjection(70f, Window.getWidth(), Window.getHeight(), 0.1f, 5000f);
		Transform.setCamera(player.getCamera());
		
		WorldShader.setAmbientLight(new Vector3f(3f, 3f, 3f));
		WorldShader.setDirectionalLight(new DirectionalLight(new BaseLight(new Vector3f(1,1,1), 10f), new Vector3f(1,-1,-1)));

		
		fontTexture = new Texture("arista.png", GL_LINEAR, GL_LINEAR);
		StringRenderer.create(fontTexture, "res/arista.fnt");
		
		RenderUtil.setClearColor(new Vector3f(119f/255f, 160f/255f, 1));
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		auxThread = new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				while(true)
				{
					update();
					
					try
					{
						Thread.sleep(17);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
					Display.sync(60);
				}
			}
			
		});
		
		auxThread.start();
	}
	
	public void input()
	{
		if(gameFocusable)
		{
			player.input();
			world.input();
			hud.input();
		}
		
		if(Input.isKeyPressed(Input.KEY_ESCAPE))
			Main.instance.stop();
		if(Input.isKeyPressed(Input.KEY_LCONTROL))
			Transform.setProjection(Transform.getFOV() - 40, Window.getWidth(), Window.getHeight(), 0.1f, 1000f);
		else if(Input.getKeyUp(Input.KEY_LCONTROL))
			Transform.setProjection(Transform.getFOV() + 40, Window.getWidth(), Window.getHeight(), 0.1f, 1000f);
		
		if(Input.getMouseDown(1))
		{
			player.getCamera().grabMouse(false);
		}
	}

	public void update() // Aux
	{	
		if(gameFocusable)
		{
			//player.update();
			hud.update();
			world.update(player.getPos());
		}
	}
	
	public void playerUpdate() // Main
	{
		player.update();
	}
	
	public void render()
	{
		worldShader.bind();
		worldShader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
		world.render(transform);
		
		hudShader.bind();
		hudShader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), null);
		hud.render();

	}
	
	public void dispose()
	{
		fontTexture.dispose();
		world.dispose();
		Spritesheet.dispose();
		hud.dispose();
		Skybox.instance.dispose();
	}

}
