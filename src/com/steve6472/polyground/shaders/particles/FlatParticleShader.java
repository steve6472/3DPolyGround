package com.steve6472.polyground.shaders.particles;

import com.steve6472.polyground.CaveGame;
import com.steve6472.sge.gfx.GeometryShader;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.8.2018
 * Project: 3DTest
 *
 ***********************/
public class FlatParticleShader
{
	private final GeometryShader shader;

	private final int transformation;
	private final int projection;
	private final int view;

	public FlatParticleShader()
	{
		shader = new GeometryShader("shaders\\particles\\flat\\shader");

		transformation = glGetUniformLocation(shader.getProgram(), "transformation");
		projection = glGetUniformLocation(shader.getProgram(), "projection");
		view = glGetUniformLocation(shader.getProgram(), "view");

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

	public GeometryShader getShader()
	{
		return shader;
	}
}
