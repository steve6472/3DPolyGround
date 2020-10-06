package steve6472.polyground.tessellators;

import org.joml.Vector2f;
import steve6472.sge.gfx.AbstractTessellator;

import java.nio.FloatBuffer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 20.12.2018
 * Project: Poly Creator 2.0
 *
 ***********************/
public class Basic2DTessellator extends AbstractTessellator
{
	private final FloatBuffer pos;
	private final FloatBuffer color;

	private float offsetX, offsetY;

	private float x, y;
	private float r, g, b, a;

	public int current, size;

	public Basic2DTessellator(int maxLength)
	{
		this.pos = this.createBuffer(maxLength * 2);
		this.color = this.createBuffer(maxLength * 4);
	}

	public void offset(float offsetX, float offsetY)
	{
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public Basic2DTessellator pos(float x, float y)
	{
		this.x = x + offsetX;
		this.y = y + offsetY;
		return this;
	}

	public Basic2DTessellator pos(Vector2f vector)
	{
		this.x = vector.x + offsetX;
		this.y = vector.y + offsetY;
		return this;
	}

	public Basic2DTessellator color(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return this;
	}

	@Override
	public void endVertex()
	{
		pos.put(x).put(y);
		color.put(r).put(g).put(b).put(a);
		current++;
		super.endVertex();
	}

	@Override
	public void begin(int vertexCount)
	{
		setBuffer(pos, vertexCount * 2);
		setBuffer(color, vertexCount * 4);

		current = 0;
		size = vertexCount;

		super.begin(vertexCount);
	}

	public boolean hasSpace()
	{
		return current < size;
	}

	public void loadPos(int index)
	{
		loadBuffer(pos, index, 2);
	}

	public void loadColor(int index)
	{
		loadBuffer(color, index, 4);
	}

	@Override
	public void draw(int mode)
	{
		super.draw(mode);

		pos.clear();
		color.clear();
	}
}
