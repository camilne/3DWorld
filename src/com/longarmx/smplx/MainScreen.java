package com.longarmx.smplx;

import com.base.engine.Input;
import com.longarmx.smplx.gui.Label;
import com.longarmx.smplx.gui.Screen;
import com.longarmx.smplx.world.World;

public class MainScreen extends Screen
{
	Label fps;
	Label delta;
	Label playerPos;
	
	private boolean debug = false;
	
	public MainScreen()
	{
		fps = new Label(0, 0, "FPS: ", 1, false);
		delta = new Label(0, 0.1f, "Delta: ", 1, false);
		playerPos = new Label(0, 0.2f, "", 1, false);
		
		addComponents(fps);
		addComponents(delta);
		addComponents(playerPos);
	}
	
	public void input()
	{
		if(Input.isKeyPressed(Input.KEY_F2))
			debug = !debug;
	}
	
	public void update()
	{
		super.update();
		
		fps.setText("FPS: " + Main.getSecondsFPS());
		delta.setText("Delta: " + String.format("%.2f", Main.getAverageDelta()) + "ms");
		
		if(debug)
			playerPos.setText("Pos: " + World.getPlayerPos().toString());
		else
			playerPos.setText("");
	}

}
