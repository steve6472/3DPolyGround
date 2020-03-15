package com.steve6472.polyground.world;

import com.steve6472.polyground.shaders.CaveGameSkyboxShader;
import com.steve6472.sge.gfx.Skybox;
import com.steve6472.sge.gfx.StaticCubeMap;
import com.steve6472.sge.gfx.shaders.Shader;
import com.steve6472.sge.gfx.shaders.StaticShaderCubeMap;
import org.joml.Matrix4f;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.03.2020
 * Project: SJP
 *
 ***********************/
public class CaveGameSkybox extends Skybox
{
	public CaveGameSkybox(StaticCubeMap skyboxTexture, Matrix4f projection)
	{
		super(skyboxTexture, projection);
	}

	@Override
	protected StaticShaderCubeMap createShader(Matrix4f projection)
	{
		StaticShaderCubeMap shader = new CaveGameSkyboxShader();
		shader.setProjection(projection);
		shader.setUniform(CaveGameSkyboxShader.SKYBOX, 0);
		Shader.releaseShader();

		return shader;
	}

	public void setShade(float shade)
	{
		shader.bind();
		shader.setUniform(CaveGameSkyboxShader.SHADE, shade);
	}
}
