package com.steve6472.polyground.shaders;

import com.steve6472.sge.gfx.shaders.StaticShader3D;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class RiftShader extends StaticShader3D
{
	public static Type TEXTURE, TINT;

	public RiftShader()
	{
		super("shaders\\rift\\shader");
	}

	@Override
	protected void createUniforms()
	{
		addUniform("tex", TEXTURE = new Type(EnumUniformType.INT_1));
		addUniform("tint", TINT = new Type(EnumUniformType.FLOAT_3));
	}
}
