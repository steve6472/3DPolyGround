package steve6472.polyground.gfx.shaders.world;

import steve6472.sge.gfx.shaders.StaticShader3D;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class FlatTexturedShader extends StaticShader3D
{
	public static Type ATLAS;

	public FlatTexturedShader()
	{
		super("flat_textured");
	}

	@Override
	protected void createUniforms()
	{
		addUniform("atlas", ATLAS = new Type(EnumUniformType.INT_1));
	}
}
