package com.steve6472.polyground.shaders.particles;

import com.steve6472.sge.gfx.shaders.StaticGeometryShader3D;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.8.2018
 * Project: 3DTest
 *
 ***********************/
public class FlatParticleShader extends StaticGeometryShader3D
{
	public FlatParticleShader()
	{
		super("particles/flat/shader");
	}

	@Override
	protected void createUniforms()
	{
	}
}
