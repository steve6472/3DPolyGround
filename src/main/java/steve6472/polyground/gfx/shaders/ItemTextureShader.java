package steve6472.polyground.gfx.shaders;

import steve6472.sge.gfx.shaders.StaticShader2D;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2019
 * Project: SJP
 *
 ***********************/
public class ItemTextureShader extends StaticShader2D
{
	public static Type ATLAS;

	public ItemTextureShader()
	{
		super("game/sprite_shader");
	}

	@Override
	protected void createUniforms()
	{
		addUniform("atlas", ATLAS = new Type(EnumUniformType.INT_1));
	}
}
