package com.steve6472.polyground.shaders.particles;

import com.steve6472.sge.gfx.shaders.StaticGeometryShader3D;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.8.2018
 * Project: 3DTest
 *
 ***********************/
public class BreakParticleShader extends StaticGeometryShader3D
{
	public static Type SAMPLER;

	public BreakParticleShader()
	{
		super("shaders\\particles\\break\\shader");
	}

	@Override
	protected void createUniforms()
	{
		addUniform("sampler", SAMPLER = new Type(EnumUniformType.INT_1));
	}
}
