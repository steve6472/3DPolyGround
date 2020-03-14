package com.steve6472.polyground.shaders.particles;

import com.steve6472.sge.gfx.shaders.StaticGeometryShader3D;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.8.2018
 * Project: 3DTest
 *
 ***********************/
public class FlatTexturedParticleShader extends StaticGeometryShader3D
{
	public static Type SAMPLER;

	public FlatTexturedParticleShader()
	{
		super("particles/flat_textured/shader");
	}

	@Override
	protected void createUniforms()
	{
		addUniform("sampler", SAMPLER = new Type(EnumUniformType.INT_1));
	}
}
