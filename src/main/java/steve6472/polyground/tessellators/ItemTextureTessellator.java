package steve6472.polyground.tessellators;

import steve6472.sge.gfx.AbstractTessellator;

import java.nio.FloatBuffer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.09.2019
 * Project: SJP
 *
 ***********************/
public class ItemTextureTessellator extends AbstractTessellator
{
	private final FloatBuffer pos;
	private final FloatBuffer texture;
	private final FloatBuffer alpha;

	private float x, y;
	private float u, v;
	private float a;

	public ItemTextureTessellator(int maxLimit)
	{
		pos = createBuffer(maxLimit * 2);
		texture = createBuffer(maxLimit * 2);
		alpha = createBuffer(maxLimit);
	}

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
		pos.put(x).put(y);
		texture.put(u).put(v);
		alpha.put(a);
		super.endVertex();
	}

	@Override
	public void begin(int vertexCount)
	{
		setBuffer(pos, vertexCount * 2);
		setBuffer(texture, vertexCount * 2);
		setBuffer(alpha, vertexCount);

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
