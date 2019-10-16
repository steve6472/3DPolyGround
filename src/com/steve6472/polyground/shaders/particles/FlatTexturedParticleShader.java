package com.steve6472.polyground.shaders.particles;

import com.steve6472.polyground.CaveGame;
import com.steve6472.sge.gfx.GeometryShader;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.8.2018
 * Project: 3DTest
 *
 ***********************/
public class FlatTexturedParticleShader
{
	private final GeometryShader shader;

	private final int transformation;
	private final int projection;
	private final int view;

	private final int sampler;

	public FlatTexturedParticleShader()
	{
		shader = new GeometryShader("shaders\\particles\\flat_textured\\shader");

		transformation = glGetUniformLocation(shader.getProgram(), "transformation");
		projection = glGetUniformLocation(shader.getProgram(), "projection");
		view = glGetUniformLocation(shader.getProgram(), "view");

		sampler = glGetUniformLocation(shader.getProgram(), "sampler");

		shader.bind();
		setTransformation(new Matrix4f());
	}

	public void bind()
	{
		shader.bind();
		setView(CaveGame.getInstance().getCamera().getViewMatrix());
	}

	public void setTransformation(Matrix4f matrix4f)
	{
		FloatBuffer b1 = BufferUtils.createFloatBuffer(16);
		matrix4f.get(b1);
		glUniformMatrix4fv(transformation, false, b1);
	}

	public void setProjection(Matrix4f matrix4f)
	{
		FloatBuffer b1 = BufferUtils.createFloatBuffer(16);
		matrix4f.get(b1);
		glUniformMatrix4fv(projection, false, b1);
	}

	public void setView(Matrix4f matrix4f)
	{
		FloatBuffer b1 = BufferUtils.createFloatBuffer(16);
		matrix4f.get(b1);
		glUniformMatrix4fv(view, false, b1);
	}

	public void setSampler(int sampler)
	{
		glUniform1i(this.sampler, sampler);
	}

	public GeometryShader getShader()
	{
		return shader;
	}
}
