package com.steve6472.polyground.shaders.world;

import com.steve6472.sge.gfx.shaders.StaticShader3D;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class WorldShader extends StaticShader3D
{
	public static Type ATLAS;

	public WorldShader()
	{
		super("shaders\\world");
	}

	@Override
	protected void createUniforms()
	{
		addUniform("atlas", ATLAS = new Type(EnumUniformType.INT_1));
	}
}
