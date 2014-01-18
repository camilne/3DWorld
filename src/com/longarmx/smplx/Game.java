package com.longarmx.smplx;

import com.base.engine.BaseLight;
import com.base.engine.DirectionalLight;
import com.base.engine.Input;
import com.base.engine.Material;
import com.base.engine.RenderUtil;
import com.base.engine.Texture;
import com.base.engine.Transform;
import com.base.engine.Vector3f;
import com.base.engine.Window;
import com.longarmx.smplx.gui.Screen;
import com.longarmx.smplx.gui.StringRenderer;
import com.longarmx.smplx.world.World;

public class Game
{
	
	private WorldShader worldShader;
	private HudShader hudShader;
	private Material material;
	private Transform transform;
	private Texture fontTexture;

	private World world;
	private Player player;
	private Screen hud;
	
	public Game()
	{
		worldShader = WorldShader.getInstance();
		hudShader = HudShader.getInstance();
		material = new Material(null, new Vector3f(.1f, .1f, .1f), 1, 8);
		transform = new Transform();

		world = new World();
		player = new Player(world);
		hud = new Screen();
		
		Transform.setProjection(70f, Window.getWidth(), Window.getHeight(), 0.1f, 1000f);
		Transform.setCamera(player.getCamera());
		
		WorldShader.setAmbientLight(new Vector3f(3f, 3f, 3f));
		WorldShader.setDirectionalLight(new DirectionalLight(new BaseLight(new Vector3f(1,1,1), 10f), new Vector3f(1,1,1)));
		
		fontTexture = new Texture("arista.png");
		StringRenderer.create(fontTexture, "res/arista.fnt");
	}
	
	public void input()
	{
		player.input();
		world.input();
		
		if(Input.getKeyDown(Input.KEY_ESCAPE))
			Main.instance.stop();
		if(Input.getKeyDown(Input.KEY_LCONTROL))
			Transform.setProjection(Transform.getFOV() - 40, Window.getWidth(), Window.getHeight(), 0.1f, 1000f);
		else if(Input.getKeyUp(Input.KEY_LCONTROL))
			Transform.setProjection(Transform.getFOV() + 40, Window.getWidth(), Window.getHeight(), 0.1f, 1000f);
	}
	

	public void update()
	{		
		player.update();
		
		transform.setTranslation(0, 0, 0);
		transform.setRotation(0, 0, 0);
		transform.setScale(1, 1, 1);
	}
	
	public void render()
	{
		RenderUtil.setClearColor(new Vector3f(119f/255f, 160f/255f, 1));
		worldShader.bind();
		worldShader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
		world.render(player.getPos().getX(), player.getPos().getZ());
		
		hudShader.bind();
		hudShader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
		hud.render();
	}
	
	
	
	public void dispose()
	{
		fontTexture.dispose();
		world.dispose();
		Spritesheet.dispose();
	}

}
