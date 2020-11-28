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
public class StackTessellator extends AbstractTessellator
{
	private final FloatBuffer pos;
	private final FloatBuffer color;
	private final FloatBuffer texture;
	private final FloatBuffer normal;

	private float x, y, z;
	private float r, g, b, a;
	private float tx, ty;
	private float nx, ny, nz;

	public int current, maxCount;

	public StackTessellator(int maxLength)
	{
		this.pos = this.createBuffer(maxLength * 3);
		this.color = this.createBuffer(maxLength * 4);
		this.texture = this.createBuffer(maxLength * 2);
		this.normal = this.createBuffer(maxLength * 3);
	}

	public StackTessellator pos(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public StackTessellator pos(Vector3f position)
	{
		this.x = position.x;
		this.y = position.y;
		this.z = position.z;
		return this;
	}

	public StackTessellator color(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		return this;
	}

	public StackTessellator uv(float tx, float ty)
	{
		this.tx = tx;
		this.ty = ty;
		return this;
	}

	public StackTessellator normal(float nx, float ny, float nz)
	{
		this.nx = nx;
		this.ny = ny;
		this.nz = nz;
		return this;
	}

	public StackTessellator normal(Vector3f normal)
	{
		this.nx = normal.x;
		this.ny = normal.y;
		this.nz = normal.z;
		return this;
	}

	@Override
	public void endVertex()
	{
		pos.put(x).put(y).put(z);
		color.put(r).put(g).put(b).put(a);
		texture.put(tx).put(ty);
		normal.put(nx).put(ny).put(nz);
		current++;
		super.endVertex();
	}

	@Override
	public void begin(int vertexCount)
	{
		setBuffer(pos, vertexCount * 3);
		setBuffer(color, vertexCount * 4);
		setBuffer(texture, vertexCount * 2);
		setBuffer(normal, vertexCount * 3);

		current = 0;
		maxCount = vertexCount;

		super.begin(vertexCount);
	}

	public boolean hasSpace()
	{
		return current < maxCount;
	}

	public void loadPos(int index)
	{
		loadBuffer(pos, index, 3);
	}

	public void loadColor(int index)
	{
		loadBuffer(color, index, 4);
	}

	public void loadUv(int index)
	{
		loadBuffer(texture, index, 2);
	}

	public void loadNormal(int index)
	{
		loadBuffer(normal, index, 3);
	}

	@Override
	public void draw(int mode)
	{
		super.draw(mode);
	}

	public void clear()
	{
		pos.clear();
		color.clear();
		texture.clear();
		normal.clear();
	}
}
