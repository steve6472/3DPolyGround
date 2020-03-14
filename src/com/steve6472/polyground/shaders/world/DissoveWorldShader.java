package com.steve6472.polyground.shaders.world;

import com.steve6472.sge.gfx.shaders.StaticShader3D;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.08.2019
 * Project: SJP
 *
 ***********************/
public class DissoveWorldShader extends StaticShader3D
{
	public static Type ATLAS, NOISERESOLUTION, TIME, SHADE;

	public DissoveWorldShader()
	{
		super("dissolve_world");
	}

	@Override
	protected void createUniforms()
	{
		addUniform("atlas", ATLAS = new Type(EnumUniformType.INT_1));
		addUniform("noiseResolution", NOISERESOLUTION = new Type(EnumUniformType.FLOAT_1));
		addUniform("time", TIME = new Type(EnumUniformType.FLOAT_1));
		addUniform("shade", SHADE = new Type(EnumUniformType.FLOAT_1));
	}
}
