package steve6472.polyground.gfx.stack;

import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.tessellators.BasicTessellator;
import steve6472.sge.gfx.Tessellator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public class Stack extends Matrix4fStack
{
	private static final int MAX_SIZE = 4096;

	private final BasicTessellator tess;
	private final Vector3f dest3f;
	private final Vector4f lastColor;

	public Stack()
	{
		super(16);

		tess = new BasicTessellator(MAX_SIZE);
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
		MainRender.shaders.mainShader.bind(view);

		tess.loadPos(0);
		tess.loadColor(1);
		tess.draw(Tessellator.TRIANGLES);
		tess.disable(0, 1);

		tess.begin(MAX_SIZE);
		tess.color(1, 1, 1, 1);
	}
}
