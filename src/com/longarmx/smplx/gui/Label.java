package com.longarmx.smplx.gui;

public class Label extends Component
{
	
	private String text;
	private float scale;
	private boolean center;

	public Label(float x, float y, String text, float scale, boolean center)
	{
		super(x, y, 1, 1);
		this.text = text;
		this.scale = scale;
		this.center = center;
	}
	
	public void render()
	{
		StringRenderer.drawString(text, getPos().getX(), getPos().getY(), 0, scale, center);
	}
	
	public void setText(String text)
	{
		this.text = text;
	}
	
	public String getText()
	{
		return text;
	}
	
	public void setScale(float scale)
	{
		this.scale = scale;
	}
	
	public float getScale()
	{
		return scale;
	}

}
