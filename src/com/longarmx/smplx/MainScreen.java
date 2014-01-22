package com.longarmx.smplx;

import com.longarmx.smplx.gui.Label;
import com.longarmx.smplx.gui.Screen;

public class MainScreen extends Screen
{
	
	Label health;
	Label mana;
	Label fps;
	
	public MainScreen()
	{
		//health = new Label(Main.HEIGHT - StringRenderer.glyphHeight, 0, "Health: 100", 1, false);
		//mana = new Label(Main.HEIGHT - StringRenderer.glyphHeight * 2, 0, "Mana: 100", 1, false);
		fps = new Label(0, 0, "FPS: ", 1, false);
		
		addComponents(fps);
	}
	
	public void update()
	{
		super.update();
		
		fps.setText("FPS: " + Main.FPS);
	}

}
