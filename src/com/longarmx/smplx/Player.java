package com.longarmx.smplx;

import com.base.engine.Camera;
import com.base.engine.Input;
import com.base.engine.Time;
import com.base.engine.Vector3f;
import com.longarmx.smplx.world.Chunk;
import com.longarmx.smplx.world.World;

public class Player
{
	private boolean isFlying;
	private float x;
	private float y;
	private float z;
	private World world;
	private Camera camera;
	private float movementSpeed;
	
	public Player(World world){
		this.world = world;
		isFlying = false;
		camera = new Camera();
		camera.setPos(new Vector3f(10 * Chunk.size, 0, 10 * Chunk.size));
		movementSpeed = 0.01f;
	}
	
	public void input()
	{
		if(Input.getKey(Input.KEY_Q))
			movementSpeed = 0.1f;
		else if(Input.getKey(Input.KEY_LSHIFT))
			movementSpeed = 0.0025f;
		else
			movementSpeed = 0.01f;
		
		camera.setMovAmt((float)(Time.getDelta() * movementSpeed));
		camera.input();
		
		if(Input.isKeyPressed(Input.KEY_E))
			isFlying = !isFlying;
	}
	
	private void fall()
	{
		camera.getPos().setY(y - (float)Time.getDelta() * movementSpeed);
	}
	
	private void collision()
	{
		if(!isFlying)
			camera.getPos().setY(Math.max(camera.getPos().getY(), world.getTriangleY(x, z) + 2));
	}
	
	public void update()
	{
		x = camera.getPos().getX();
		y = camera.getPos().getY();
		z = camera.getPos().getZ();
		
		if(!isFlying)
			fall();
		collision();
	}
	
	public Vector3f getPos()
	{
		return camera.getPos();
	}
	
	public Camera getCamera()
	{
		return camera;
	}

}
