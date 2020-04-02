package steve6472.polyground.gfx.shaders.particles;

import steve6472.sge.gfx.shaders.StaticGeometryShader3D;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.8.2018
 * Project: 3DTest
 *
 ***********************/
public class BasicParticleShader extends StaticGeometryShader3D
{
	public BasicParticleShader()
	{
		super("particles/basic/particleShader");
	}

	@Override
	protected void createUniforms()
	{
	}
}
