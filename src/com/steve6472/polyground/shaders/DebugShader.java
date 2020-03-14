package com.steve6472.polyground.shaders;

import com.steve6472.sge.gfx.shaders.StaticShader3D;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class DebugShader extends StaticShader3D
{
	public static Type COLOR;

	public DebugShader()
	{
		super("debug\\shader");
	}

	@Override
	protected void createUniforms()
	{
		addUniform("color", COLOR = new Type(EnumUniformType.FLOAT_4));
	}
}
