package com.longarmx.smplx;

import com.base.engine.BaseLight;
import com.base.engine.DirectionalLight;
import com.base.engine.Input;
import com.base.engine.Material;
import com.base.engine.PhongShader;
import com.base.engine.RenderUtil;
import com.base.engine.Shader;
import com.base.engine.Texture;
import com.base.engine.Transform;
import com.base.engine.Vector3f;
import com.base.engine.Window;
import com.longarmx.smplx.world.World;

public class Game
{
	
	private Shader shader;
	private Material material;
	private Transform transform;

	private World world;
	private Player player;
	
	public Game()
	{
		shader = PhongShader.getInstance();
		material = new Material(new Texture("grass13.png"), new Vector3f(.1f, .1f, .1f));
		transform = new Transform();

		world = new World();
		player = new Player(world);
		
		Transform.setProjection(70f, Window.getWidth(), Window.getHeight(), 0.1f, 1000f);
		Transform.setCamera(player.getCamera());
		
		PhongShader.setAmbientLight(new Vector3f(3f, 3f, 3f));
		PhongShader.setDirectionalLight(new DirectionalLight(new BaseLight(new Vector3f(1,1,1), 10f), new Vector3f(1,1,1)));

	}
	
	public void input()
	{
		player.input();
		world.input();
		
		if(Input.getKeyDown(Input.KEY_ESCAPE))
			Main.instance.stop();
	}
	
	float temp = 0.0f;

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
		shader.bind();
		shader.updateUniforms(transform.getTransformation(), transform.getProjectedTransformation(), material);
		world.render(player.getPos().getX(), player.getPos().getZ());
	}
	
	
	
	public void dispose()
	{
		world.dispose();
	}

}
