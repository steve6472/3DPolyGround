package steve6472.polyground.tessellators;

import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.sge.gfx.AbstractTessellator;

import java.nio.FloatBuffer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.11.2018
 * Project: Main3D
 *
 ***********************/
public class BreakParticleTessellator extends AbstractTessellator
{
	private final FloatBuffer pos;
	private final FloatBuffer uv;
	private final FloatBuffer color;
	private final FloatBuffer size;

	private float x, y, z;
	private float r, g, b;
	private float uMin, uMax, vMin, vMax;
	private float s;

	public BreakParticleTessellator(int maxLength)
	{
		this.pos = this.createBuffer(maxLength * 3);
		this.uv = this.createBuffer(maxLength * 4);
		this.color = this.createBuffer(maxLength * 3);
		this.size = this.createBuffer(maxLength);
	}

	public BreakParticleTessellator pos(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public BreakParticleTessellator color(float r, float g, float b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		return this;
	}

	public BreakParticleTessellator uv(float uMin, float uMax, float vMin, float vMax)
	{
		this.uMin = uMin;
		this.uMax = uMax;
		this.vMin = vMin;
		this.vMax = vMax;
		return this;
	}

	public BreakParticleTessellator uv(Vector4f uv)
	{
		this.uMin = uv.x;
		this.uMax = uv.y;
		this.vMin = uv.z;
		this.vMax = uv.w;
		return this;
	}

	public BreakParticleTessellator color(Vector3f color)
	{
		this.r = color.x;
		this.g = color.y;
		this.b = color.z;
		return this;
	}

	public BreakParticleTessellator size(float s)
	{
		this.s = s;
		return this;
	}

	@Override
	public void endVertex()
	{
		pos.put(x).put(y).put(z);
		color.put(r).put(g).put(b);
		uv.put(uMin).put(uMax).put(vMin).put(vMax);
		size.put(s);
		super.endVertex();
	}

	@Override
	public void begin(int vertexCount)
	{
		setBuffer(pos, vertexCount * 3);
		setBuffer(uv, vertexCount * 4);
		setBuffer(color, vertexCount * 3);
		setBuffer(size, vertexCount);

		super.begin(vertexCount);
	}

	public void loadPos(int index)
	{
		loadBuffer(pos, index, 3);
	}

	public void loadUv(int index)
	{
		loadBuffer(uv, index, 4);
	}

	public void loadColor(int index)
	{
		loadBuffer(color, index, 3);
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
		uv.clear();
		color.clear();
		size.clear();
	}
}
