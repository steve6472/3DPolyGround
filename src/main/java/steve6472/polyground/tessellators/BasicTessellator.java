package steve6472.polyground.tessellators;

import org.joml.Vector3f;
import steve6472.sge.gfx.AbstractTessellator;

import java.nio.FloatBuffer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 20.12.2018
 * Project: Poly Creator 2.0
 *
 ***********************/
public class BasicTessellator extends AbstractTessellator
{
	private final FloatBuffer pos;
	private final FloatBuffer color;

	private float x, y, z;
	private float r, g, b, a;

	public int current, size;

	public BasicTessellator(int maxLength)
	{
		this.pos = this.createBuffer(maxLength * 3);
		this.color = this.createBuffer(maxLength * 4);
	}

	public BasicTessellator pos(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public BasicTessellator pos(Vector3f vector)
	{
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
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

	@Override
	public void endVertex()
	{
		pos.put(x).put(y).put(z);
		color.put(r).put(g).put(b).put(a);
		current++;
		super.endVertex();
	}

	@Override
	public void begin(int vertexCount)
	{
		setBuffer(pos, vertexCount * 3);
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
		loadBuffer(pos, index, 3);
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
