package steve6472.polyground.gfx.stack;

import org.joml.AABBf;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import steve6472.polyground.gfx.MainRender;
import steve6472.polyground.tessellators.LineTessellator;
import steve6472.sge.gfx.Tessellator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 14.11.2020
 * Project: CaveGame
 *
 ***********************/
public class LineTess extends StackTess
{
	private static final int MAX_SIZE = 1024 * 8;

	private final LineTessellator tess;
	private final Vector3f dest3f;
	private final Vector4f lastColor;

	public LineTess(Stack stack)
	{
		super(stack);

		tess = new LineTessellator(MAX_SIZE);
		tess.begin(MAX_SIZE);
		tess.color(1, 1, 1, 1);

		dest3f = new Vector3f();
		lastColor = new Vector4f();
	}

	public LineTess pos(float x, float y, float z)
	{
		stack.transformPosition(x, y, z, dest3f);
		tess.pos(dest3f);
		return this;
	}

	public LineTess color(float r, float g, float b, float a)
	{
		tess.color(r, g, b, a);
		lastColor.set(r, g, b, a);
		return this;
	}

	public LineTess color(Vector4f color)
	{
		tess.color(color.x, color.y, color.z, color.w);
		lastColor.set(color);
		return this;
	}

	public Vector4f getLastColor()
	{
		return lastColor;
	}

	public LineTess endVertex()
	{
		tess.endVertex();
		return this;
	}

	@Override
	public void render(Matrix4f view)
	{
		MainRender.shaders.mainShader.bind(view);
		MainRender.shaders.mainShader.setTransformation(new Matrix4f());

		tess.loadPos(0);
		tess.loadColor(1);
		tess.draw(Tessellator.LINES);
		tess.disable(0, 1);
	}

	@Override
	public void reset()
	{
		tess.clear();
		tess.begin(MAX_SIZE);
		tess.color(1, 1, 1, 1);
	}

	public void debugBox(AABBf box)
	{
		debugBox(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
	}

	public void debugBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
	{
		pos(minX, minY, minZ).color(1f, 0f, 0f, 1f).endVertex();
		pos(maxX, minY, minZ).color(1f, 0f, 0f, 1f).endVertex();
		pos(minX, minY, minZ).color(0f, 0f, 1f, 1f).endVertex();
		pos(minX, minY, maxZ).color(0f, 0f, 1f, 1f).endVertex();
		pos(minX, minY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		pos(maxX, minY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		pos(maxX, minY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		pos(maxX, minY, maxZ).color(1f, 1f, 1f, 1f).endVertex();

		pos(minX, minY, minZ).color(0f, 1f, 0f, 1f).endVertex();
		pos(minX, maxY, minZ).color(0f, 1f, 0f, 1f).endVertex();
		pos(maxX, minY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		pos(maxX, maxY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		pos(minX, minY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		pos(minX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		pos(maxX, minY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		pos(maxX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();

		pos(minX, maxY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		pos(maxX, maxY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		pos(minX, maxY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		pos(minX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		pos(minX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		pos(maxX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		pos(maxX, maxY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		pos(maxX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
	}

	public void coloredBoxWHD(float x, float y, float z, float w, float h, float d, float r, float g, float b, float a)
	{
		coloredBox(x, y, z, x + w, y + h, z + d, r, g, b, a);
	}

	public void coloredBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float r, float g, float b, float a)
	{
		pos(minX, minY, minZ).color(r, g, b, a).endVertex();
		pos(maxX, minY, minZ).color(r, g, b, a).endVertex();
		pos(minX, minY, minZ).color(r, g, b, a).endVertex();
		pos(minX, minY, maxZ).color(r, g, b, a).endVertex();
		pos(minX, minY, maxZ).color(r, g, b, a).endVertex();
		pos(maxX, minY, maxZ).color(r, g, b, a).endVertex();
		pos(maxX, minY, minZ).color(r, g, b, a).endVertex();
		pos(maxX, minY, maxZ).color(r, g, b, a).endVertex();

		pos(minX, minY, minZ).color(r, g, b, a).endVertex();
		pos(minX, maxY, minZ).color(r, g, b, a).endVertex();
		pos(maxX, minY, minZ).color(r, g, b, a).endVertex();
		pos(maxX, maxY, minZ).color(r, g, b, a).endVertex();
		pos(minX, minY, maxZ).color(r, g, b, a).endVertex();
		pos(minX, maxY, maxZ).color(r, g, b, a).endVertex();
		pos(maxX, minY, maxZ).color(r, g, b, a).endVertex();
		pos(maxX, maxY, maxZ).color(r, g, b, a).endVertex();

		pos(minX, maxY, minZ).color(r, g, b, a).endVertex();
		pos(maxX, maxY, minZ).color(r, g, b, a).endVertex();
		pos(minX, maxY, minZ).color(r, g, b, a).endVertex();
		pos(minX, maxY, maxZ).color(r, g, b, a).endVertex();
		pos(minX, maxY, maxZ).color(r, g, b, a).endVertex();
		pos(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
		pos(maxX, maxY, minZ).color(r, g, b, a).endVertex();
		pos(maxX, maxY, maxZ).color(r, g, b, a).endVertex();
	}

	public static int getMaxSize()
	{
		return MAX_SIZE;
	}

	public int current()
	{
		return tess.current;
	}

	public int maxCount()
	{
		return tess.maxCount;
	}

	public LineTessellator getTess()
	{
		return tess;
	}
}
