package steve6472.polyground.gfx.shaders.world;

import steve6472.sge.gfx.shaders.StaticShader3D;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.08.2019
 * Project: SJP
 *
 ***********************/
public class WorldShader extends StaticShader3D
{
	public static Type ATLAS, SHADE;

	public WorldShader()
	{
		super("world");
	}

	@Override
	protected void createUniforms()
	{
		addUniform("atlas", ATLAS = new Type(EnumUniformType.INT_1));
		addUniform("shade", SHADE = new Type(EnumUniformType.FLOAT_1));
	}
}
