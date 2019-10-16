package com.steve6472.polyground.tessellators;

import com.steve6472.sge.gfx.TessellatorCreator;

import java.nio.FloatBuffer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.09.2019
 * Project: SJP
 *
 ***********************/
public class ItemTextureTessellator extends TessellatorCreator
{
	private FloatBuffer pos;
	private FloatBuffer texture;
	private FloatBuffer alpha;

	private float x, y;
	private float u, v;
	private float a;

	public ItemTextureTessellator pos(float x, float y)
	{
		this.x = x;
		this.y = y;
		return this;
	}

	public ItemTextureTessellator texture(float u, float v)
	{
		this.u = u;
		this.v = v;
		return this;
	}

	public ItemTextureTessellator alpha(float a)
	{
		this.a = a;
		return this;
	}

	@Override
	public void endVertex()
	{
		put(pos, x, y);
		put(texture, u, v);
		put(alpha, a);
		super.endVertex();
	}

	@Override
	public void begin(int vertexCount)
	{
		pos = createBuffer(vertexCount * 2);
		texture = createBuffer(vertexCount * 2);
		alpha = createBuffer(vertexCount);

		super.begin(vertexCount);
	}

	public void loadPos(int index)
	{
		loadBuffer(pos, index, 2);
	}

	public void loadTexture(int index)
	{
		loadBuffer(texture, index, 2);
	}

	public void loadAlpha(int index)
	{
		loadBuffer(alpha, index, 1);
	}

	@Override
	public void draw(int mode)
	{
		super.draw(mode);

		pos.clear();
		texture.clear();
		alpha.clear();
	}
}
