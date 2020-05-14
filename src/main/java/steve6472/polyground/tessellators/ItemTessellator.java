package steve6472.polyground.tessellators;

import steve6472.sge.gfx.AbstractTessellator;

import java.nio.FloatBuffer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.09.2019
 * Project: SJP
 *
 ***********************/
public class ItemTessellator extends AbstractTessellator
{
	private final FloatBuffer pos;
	private final FloatBuffer color;
	private final FloatBuffer texture;

	private float x, y, z;
	private float r, g, b, a;
	private float u, v;

	public ItemTessellator(int maxLength)
	{
		this.pos = this.createBuffer(maxLength * 3);
		this.color = this.createBuffer(maxLength * 4);
		this.texture = this.createBuffer(maxLength * 2);
	}

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
		pos.put(x).put(y).put(z);
		color.put(r).put(g).put(b).put(a);
		texture.put(u).put(v);
		super.endVertex();
	}

	@Override
	public void begin(int vertexCount)
	{
		setBuffer(pos, vertexCount * 3);
		setBuffer(color, vertexCount * 4);
		setBuffer(texture, vertexCount * 2);

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
