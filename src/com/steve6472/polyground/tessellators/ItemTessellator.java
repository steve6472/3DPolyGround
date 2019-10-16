package com.steve6472.polyground.tessellators;

import com.steve6472.sge.gfx.TessellatorCreator;

import java.nio.FloatBuffer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.09.2019
 * Project: SJP
 *
 ***********************/
public class ItemTessellator extends TessellatorCreator
{
	private FloatBuffer pos;
	private FloatBuffer color;
	private FloatBuffer texture;

	private float x, y, z;
	private float r, g, b, a;
	private float u, v;

	public ItemTessellator pos(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public ItemTessellator color(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return this;
	}

	public ItemTessellator texture(float u, float v)
	{
		this.u = u;
		this.v = v;
		return this;
	}

	@Override
	public void endVertex()
	{
		put(pos, x, y, z);
		put(color, r, g, b, a);
		put(texture, u, v);
		super.endVertex();
	}

	@Override
	public void begin(int vertexCount)
	{
		pos = createBuffer(vertexCount * 3);
		color = createBuffer(vertexCount * 4);
		texture = createBuffer(vertexCount * 2);

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

	public void loadTexture(int index)
	{
		loadBuffer(texture, index, 2);
	}

	@Override
	public void draw(int mode)
	{
		super.draw(mode);

		pos.clear();
		color.clear();
		texture.clear();
	}
}
