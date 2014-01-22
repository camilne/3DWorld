package com.longarmx.smplx;

import com.base.engine.Shader;

public class HudShader extends Shader
{
	
	private static final HudShader instance = new HudShader();
	
	public static HudShader getInstance()
	{
		return instance;
	}
	
	private HudShader()
	{
		super("hudVertex.vs", "hudFragment.fs", "hudVertexBasic.vs", "hudFragmentBasic.fs");

		compileShader();
	}

}
