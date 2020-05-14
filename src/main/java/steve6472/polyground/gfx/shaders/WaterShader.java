package steve6472.polyground.gfx.shaders;

import steve6472.sge.gfx.shaders.StaticShader2D;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.12.2018
 * Project: Poly Creator 2.0
 *
 ***********************/
public class WaterShader extends StaticShader2D
{
	public static Type TEXTURE;

	public WaterShader()
	{
		super("water/shader");
	}

	@Override
	protected void createUniforms()
	{
		addUniform("water", TEXTURE = new Type(EnumUniformType.INT_1));
	}
}
