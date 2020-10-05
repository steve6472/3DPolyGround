package steve6472.polyground.block.model.elements;

import org.joml.GeometryUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;
import steve6472.polyground.world.ModelBuilder;
import steve6472.sge.main.util.ColorUtil;

import java.awt.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.09.2020
 * Project: CaveGame
 *
 ***********************/
public class Bakery
{
	private static ModelBuilder builder;

	private static final Vector3f normal = new Vector3f();
	private final static Vector2f whiteUV = new Vector2f();

	public static void load(Rectangle r, ModelBuilder builder)
	{
		Bakery.builder = builder;
		System.out.println(r);
		float x = r.x;
		float y = r.y;
		float w = r.width;
		float h = r.height;
		float texel = builder.texel;
		whiteUV.set((x + w * 0.5f) * texel, (y + h * 0.5f) * texel);
		System.out.println(whiteUV);
	}

	public static int coloredCube_1x1(int x, int y, int z, int color)
	{
		Vector3f from = new Vector3f(x * 1f / 16f, y * 1f / 16f, z * 1f / 16f);
		Vector3f to = new Vector3f(x * 1f / 16f + 1f / 16f, y * 1f / 16f + 1f / 16f, z * 1f / 16f + 1f / 16f);

		coloredQuad(
			new Vector3f(to.x, to.y, to.z),
			new Vector3f(to.x, from.y, to.z),
			new Vector3f(to.x, from.y, from.z),
			new Vector3f(to.x, to.y, from.z), color);
		coloredQuad(
			new Vector3f(from.x, to.y, to.z),
			new Vector3f(from.x, from.y, to.z),
			new Vector3f(to.x, from.y, to.z),
			new Vector3f(to.x, to.y, to.z), color);
		coloredQuad(
			new Vector3f(from.x, to.y, from.z),
			new Vector3f(from.x, from.y, from.z),
			new Vector3f(from.x, from.y, to.z),
			new Vector3f(from.x, to.y, to.z), color);
		coloredQuad(
			new Vector3f(to.x, to.y, from.z),
			new Vector3f(to.x, from.y, from.z),
			new Vector3f(from.x, from.y, from.z),
			new Vector3f(from.x, to.y, from.z), color);
		coloredQuad(
			new Vector3f(to.x, to.y, from.z),
			new Vector3f(from.x, to.y, from.z),
			new Vector3f(from.x, to.y, to.z),
			new Vector3f(to.x, to.y, to.z), color);
		coloredQuad(
			new Vector3f(from.x, from.y, from.z),
			new Vector3f(to.x, from.y, from.z),
			new Vector3f(to.x, from.y, to.z),
			new Vector3f(from.x, from.y, to.z), color);

		return 12;
	}

	public static int coloredQuad(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, int color)
	{
		return coloredTriangle(v0, v1, v2, color) + coloredTriangle(v2, v3, v0, color);
	}

	public static int coloredTriangle(Vector3f v0, Vector3f v1, Vector3f v2, int color)
	{
		GeometryUtils.normal(v0, v1, v2, normal);

		builder.tri(v0, v1, v2);
		builder.uv(whiteUV);
		builder.uv(whiteUV);
		builder.uv(whiteUV);
		builder.normalTri(normal);
		builder.colorTri(ColorUtil.getRed(color) / 255f, ColorUtil.getGreen(color) / 255f, ColorUtil.getBlue(color) / 255f);

		return 1;
	}
}
