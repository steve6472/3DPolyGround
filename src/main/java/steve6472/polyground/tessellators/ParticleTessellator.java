package steve6472.polyground.tessellators;

import org.joml.Vector4f;
import steve6472.sge.gfx.AbstractTessellator;

import java.nio.FloatBuffer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.11.2018
 * Project: Main3D
 *
 ***********************/
public class ParticleTessellator extends AbstractTessellator
{
	private final FloatBuffer pos;
	private final FloatBuffer color;
	private final FloatBuffer size;

	private float x, y, z;
	private float r, g, b, a;
	private float s;

	public ParticleTessellator(int maxLength)
	{
		this.pos = this.createBuffer(maxLength * 3);
		this.color = this.createBuffer(maxLength * 4);
		this.size = this.createBuffer(maxLength);
	}

	public ParticleTessellator pos(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public ParticleTessellator color(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return this;
	}

	public ParticleTessellator color(Vector4f color)
	{
		this.r = color.x;
		this.g = color.y;
		this.b = color.z;
		this.a = color.w;
		return this;
	}

	public ParticleTessellator size(float s)
	{
		this.s = s;
		return this;
	}

	@Override
	public void endVertex()
	{
		pos.put(x).put(y).put(z);
		color.put(r).put(g).put(b).put(a);
		size.put(s);
		super.endVertex();
	}

	@Override
	public void begin(int vertexCount)
	{
		setBuffer(pos, vertexCount * 3);
		setBuffer(color, vertexCount * 4);
		setBuffer(size, vertexCount);

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

	public void loadSize(int index)
	{
		loadBuffer(size, index, 1);
	}

	@Override
	public void draw(int mode)
	{
		super.draw(mode);

		pos.clear();
		color.clear();
		size.clear();
	}
}
