package steve6472.polyground.world;

import steve6472.polyground.gfx.shaders.CGSkyboxShader;
import steve6472.sge.gfx.Skybox;
import steve6472.sge.gfx.StaticCubeMap;
import steve6472.sge.gfx.shaders.Shader;
import steve6472.sge.gfx.shaders.StaticShaderCubeMap;
import org.joml.Matrix4f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.03.2020
 * Project: SJP
 *
 ***********************/
public class CGSkybox extends Skybox
{
	public CGSkybox(StaticCubeMap skyboxTexture, Matrix4f projection)
	{
		super(skyboxTexture, projection);
	}

	@Override
	protected StaticShaderCubeMap createShader(Matrix4f projection)
	{
		StaticShaderCubeMap shader = new CGSkyboxShader();
		shader.setProjection(projection);
		shader.setUniform(CGSkyboxShader.SKYBOX, 0);
		Shader.releaseShader();

		return shader;
	}

	public void setShade(float shade)
	{
		shader.bind();
		shader.setUniform(CGSkyboxShader.SHADE, shade);
	}
}
