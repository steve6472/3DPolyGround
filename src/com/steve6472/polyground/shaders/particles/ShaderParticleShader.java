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
public class ShaderParticleShader
{
	private final GeometryShader shader;

	private final int transformation;
	private final int projection;
	private final int view;

	private final int sampler;
	private final int noise;
	private final int time;

	public ShaderParticleShader()
	{
		shader = new GeometryShader("shaders\\particles\\shader\\shader");

		transformation = glGetUniformLocation(shader.getProgram(), "transformation");
		projection = glGetUniformLocation(shader.getProgram(), "projection");
		view = glGetUniformLocation(shader.getProgram(), "view");

		sampler = glGetUniformLocation(shader.getProgram(), "sampler");
		noise = glGetUniformLocation(shader.getProgram(), "noise");
		time = glGetUniformLocation(shader.getProgram(), "time");

		if (sampler == -1) System.err.println("sampler is -1");
		if (noise == -1) System.err.println("noise is -1");
		if (time == -1) System.err.println("time is -1");

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

	public void setTime(float time)
	{
		glUniform1f(this.time, time);
	}

	public void setNoise(int noise)
	{
		glUniform1i(this.noise, noise);
	}

	public GeometryShader getShader()
	{
		return shader;
	}
}
