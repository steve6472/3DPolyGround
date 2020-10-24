package steve6472.polyground.gfx.stack;

import org.joml.*;
import steve6472.polyground.block.BlockAtlas;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.gfx.shaders.EntityShader;
import steve6472.polyground.tessellators.StackTessellator;
import steve6472.sge.gfx.Tessellator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public class Stack extends Matrix4fStack
{
	private static final int MAX_SIZE = 1024 * 32;

	private final StackTessellator tess;
	private final Vector3f dest3f;
	private final Vector4f lastColor;

	public Stack()
	{
		super(16);

		tess = new StackTessellator(MAX_SIZE);
		tess.begin(MAX_SIZE);
		tess.color(1, 1, 1, 1);

		dest3f = new Vector3f();
		lastColor = new Vector4f();
	}

	public Stack pos(float x, float y, float z)
	{
		transformPosition(x, y, z, dest3f);
		tess.pos(dest3f);
		return this;
	}

	public Stack color(float r, float g, float b, float a)
	{
		tess.color(r, g, b, a);
		lastColor.set(r, g, b, a);
		return this;
	}

	public Stack color(Vector4f color)
	{
		tess.color(color.x, color.y, color.z, color.w);
		lastColor.set(color);
		return this;
	}

	public Stack uv(float u, float v)
	{
		tess.uv(u, v);
		return this;
	}

	public Stack normal(float nx, float ny, float nz)
	{
		tess.normal(nx, ny, nz);
		return this;
	}

	public Vector4f getLastColor()
	{
		return lastColor;
	}

	public Stack endVertex()
	{
		tess.endVertex();
		return this;
	}

	public void render(Matrix4f view)
	{
		MainRender.shaders.entityShader.bind(view);
		MainRender.shaders.entityShader.setTransformation(new Matrix4f());
		MainRender.shaders.entityShader.setUniform(EntityShader.NORMAL_MATRIX, new Matrix3f(new Matrix4f(this).invert().transpose3x3()));
		BlockAtlas.getAtlas().getSprite().bind();

		tess.loadPos(0);
		tess.loadColor(1);
		tess.loadUv(2);
		tess.loadNormal(3);
		tess.draw(Tessellator.TRIANGLES);
		tess.disable(0, 1, 2, 3);
	}

	public void reset()
	{
		tess.clear();
		tess.begin(MAX_SIZE);
		tess.color(1, 1, 1, 1);
	}
}
