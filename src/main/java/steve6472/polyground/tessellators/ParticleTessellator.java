package steve6472.polyground.tessellators;

import steve6472.sge.gfx.TessellatorCreator;
import org.joml.Vector4f;

import java.nio.FloatBuffer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.11.2018
 * Project: Main3D
 *
 ***********************/
public class ParticleTessellator extends TessellatorCreator
{
	private FloatBuffer pos;
	private FloatBuffer color;
	private FloatBuffer size;

	private float x, y, z;
	private float r, g, b, a;
	private float s;

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
		put(pos, x, y, z);
		put(color, r, g, b, a);
		put(size, s);
		super.endVertex();
	}

	@Override
	public void begin(int vertexCount)
	{
		pos = createBuffer(vertexCount * 3);
		color = createBuffer(vertexCount * 4);
		size = createBuffer(vertexCount);

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
