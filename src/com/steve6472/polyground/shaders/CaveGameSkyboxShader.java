package com.steve6472.polyground.shaders;

import com.steve6472.sge.gfx.shaders.StaticShaderCubeMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.03.2020
 * Project: SJP
 *
 ***********************/
public class CaveGameSkyboxShader extends StaticShaderCubeMap
{
	public static Type SKYBOX, SHADE;

	public CaveGameSkyboxShader()
	{
		super("skybox");
	}

	@Override
	protected void createUniforms()
	{
		addUniform("skybox", SKYBOX = new Type(EnumUniformType.INT_1));
		addUniform("shade", SHADE = new Type(EnumUniformType.FLOAT_1));
	}
}
