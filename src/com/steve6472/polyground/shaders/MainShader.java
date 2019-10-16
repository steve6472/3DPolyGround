package com.steve6472.polyground.shaders;

import com.steve6472.polyground.CaveGame;
import com.steve6472.sge.gfx.Shader;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.12.2018
 * Project: Poly Creator 2.0
 *
 ***********************/
public class MainShader
{
	private Shader shader;

	private final int transformation;
	private final int projection;
	private final int view;
	private final int lightPosition;

	private final int maxLight;
	private final int lightColor;
	private final int shineDamper;
	private final int reflectivity;

	public MainShader()
	{
		shader = new Shader("shaders\\shader");

		transformation = glGetUniformLocation(shader.getProgram(), "transformation");
		projection = glGetUniformLocation(shader.getProgram(), "projection");
		view = glGetUniformLocation(shader.getProgram(), "view");
		lightPosition = glGetUniformLocation(shader.getProgram(), "lightPosition");

		maxLight = glGetUniformLocation(shader.getProgram(), "maxLight");
		lightColor = glGetUniformLocation(shader.getProgram(), "lightColor");
		shineDamper = glGetUniformLocation(shader.getProgram(), "shineDamper");
		reflectivity = glGetUniformLocation(shader.getProgram(), "reflectivity");

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

	public void setLightPosition(Vector3f lightPosition)
	{
		glUniform3f(this.lightPosition, lightPosition.x, lightPosition.y, lightPosition.z);
	}

	public void setLightColor(Vector3f lightColor)
	{
		glUniform3f(this.lightColor, lightColor.x, lightColor.y, lightColor.z);
	}

	public void setMaxLight(float maxLight)
	{
		glUniform1f(this.maxLight, maxLight);
	}

	public void setShineDamper(float shineDamper)
	{
		glUniform1f(this.shineDamper, shineDamper);
	}

	public void setReflectivity(float reflectivity)
	{
		glUniform1f(this.reflectivity, reflectivity);
	}

	public Shader getShader()
	{
		return shader;
	}
}
