package com.steve6472.polyground.tessellators;

import com.steve6472.sge.gfx.TessellatorCreator;

import java.nio.FloatBuffer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 20.12.2018
 * Project: Poly Creator 2.0
 *
 ***********************/
public class BasicTessellator extends TessellatorCreator
{
	private FloatBuffer pos;
	private FloatBuffer color;
	private FloatBuffer normal;

	private float x, y, z;
	private float r, g, b, a;
	private float nx, ny, nz;

	public BasicTessellator pos(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public BasicTessellator color(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return this;
	}

	public BasicTessellator normal(float x, float y, float z)
	{
		this.nx = x;
		this.ny = y;
		this.nz = z;
		return this;
	}

	@Override
	public void endVertex()
	{
		put(pos, x, y, z);
		put(color, r, g, b, a);
		put(normal, nx, ny, nz);
		super.endVertex();
	}

	@Override
	public void begin(int vertexCount)
	{
		pos = createBuffer(vertexCount * 3);
		color = createBuffer(vertexCount * 4);
		normal = createBuffer(vertexCount * 3);

		super.begin(vertexCount);
	}

	public void loadPos(int index)
	{
		loadBuffer(pos, index, 3);
	}

	public void loadColor(int index)
	{
		loadBuffer(color, index, 4);
	}

	public void loadNormal(int index)
	{
		loadBuffer(normal, index, 1);
	}

	@Override
	public void draw(int mode)
	{
		super.draw(mode);

		pos.clear();
		color.clear();
		normal.clear();
	}
}
