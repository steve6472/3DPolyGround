package steve6472.polyground.gfx.shaders;

import steve6472.sge.gfx.shaders.ILightUniform;
import steve6472.sge.gfx.shaders.StaticShaderBase;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.03.2020
 * Project: CaveGame
 *
 ***********************/
public class LightUniform extends ILightUniform
{
	public final Uniform position = new Uniform("position", StaticShaderBase.EnumUniformType.FLOAT_3);
	public final Uniform color = new Uniform("color", StaticShaderBase.EnumUniformType.FLOAT_3);
	public final Uniform attenuation = new Uniform("attenuation", StaticShaderBase.EnumUniformType.FLOAT_3);
	public final Uniform spotlight = new Uniform("spotlight", StaticShaderBase.EnumUniformType.FLOAT_4);

	@Override
	public String getArrayName()
	{
		return "lights";
	}

	@Override
	public ILightUniform.Uniform[] getUniforms()
	{
		return new ILightUniform.Uniform[]
			{
				position, color, attenuation, spotlight
			};
	}
}
