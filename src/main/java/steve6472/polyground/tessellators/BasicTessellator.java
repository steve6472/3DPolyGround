package steve6472.polyground.tessellators;

import steve6472.sge.gfx.TessellatorCreator;
import org.joml.Vector3f;

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
		put(pos, x, y, z);
		put(color, r, g, b, a);
		super.endVertex();
	}

	@Override
	public void begin(int vertexCount)
	{
		pos = createBuffer(vertexCount * 3);
		color = createBuffer(vertexCount * 4);

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

	@Override
	public void draw(int mode)
	{
		super.draw(mode);

		pos.clear();
		color.clear();
	}
}
