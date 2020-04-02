package steve6472.polyground.gfx.shaders;

import steve6472.sge.gfx.shaders.StaticShaderCubeMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.03.2020
 * Project: SJP
 *
 ***********************/
public class CGSkyboxShader extends StaticShaderCubeMap
{
	public static Type SKYBOX, SHADE;

	public CGSkyboxShader()
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
