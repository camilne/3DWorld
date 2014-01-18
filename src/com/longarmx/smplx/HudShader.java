package com.longarmx.smplx;

import com.base.engine.Material;
import com.base.engine.Matrix4f;
import com.base.engine.RenderUtil;
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
		super("hudVertex.vs", "hudFragment.fs", "hudBasicVertex.vs", "hudBasicFragment.fs");

		compileShader();
		
		addUniform("transform");
	}
	
	public void updateUniforms(Matrix4f worldMatrix, Matrix4f projectedMatrix, Material material)
	{
		if(material.getTexture() != null)
			material.getTexture().bind();
		else
			RenderUtil.unbindTextures();
		
		setUniform("transform", projectedMatrix);
	}

}
