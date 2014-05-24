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
		movementSpeed = 1;
	}
	
	public void input()
	{
		if(Input.getKey(Input.KEY_Q))
			movementSpeed = 10;
		else if(Input.getKey(Input.KEY_LSHIFT))
			movementSpeed = 0.25f;
		else
			movementSpeed = 1;
		
		camera.setMovAmt((float)(10 * Time.getDelta() * movementSpeed));
		camera.input();
		
		if(Input.getKeyDown(Input.KEY_E))
			isFlying = !isFlying;
	}
	
	private void fall()
	{
		camera.getPos().setY(y - (float)Time.getDelta() * 10 * (Input.getKey(Input.KEY_Q) ? 10 : 1));
	}
	
	private void collision()
	{
		if(!isFlying)
			camera.getPos().setY(Math.max(camera.getPos().getY(), world.getTriangleY(x, z, y - 1) + 1));
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
