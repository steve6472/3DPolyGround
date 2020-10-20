package steve6472.polyground.block.model.elements;

import org.joml.*;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.BlockAtlas;
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
	private static ModelBuilder builder, tempBuilder;

	private static final Vector3f normal = new Vector3f();
	private final static Vector2f whiteUV = new Vector2f();

	public static void load(Rectangle r, ModelBuilder builder)
	{
		Bakery.builder = builder;
		float x = r.x;
		float y = r.y;
		float w = r.width;
		float h = r.height;
		float texel = builder.texel;
		whiteUV.set((x + w * 0.5f) * texel, (y + h * 0.5f) * texel);
	}

	public static void tempBuilder(ModelBuilder tempBuilder)
	{
		Bakery.tempBuilder = builder;
		Bakery.builder = tempBuilder;
	}

	public static void worldBuilder()
	{
		Bakery.builder = Bakery.tempBuilder;
		Bakery.tempBuilder = null;
	}

	public static int createFaceFlags(boolean north, boolean east, boolean south, boolean west, boolean up, boolean down)
	{
		return (north ? 1 : 0) | ((east ? 1 : 0) << 1) | ((south ? 1 : 0) << 2) | ((west ? 1 : 0) << 3) | ((up ? 1 : 0) << 4) | ((down ? 1 : 0) << 5);
	}

	/**
	 *
	 * @param x x coordinate on cube
	 * @param y y coordinate on cube
	 * @param z z coordinate on cube
	 * @param color hex color
	 * @param faceFlags flags to disable rendering of faces
	 *                  1 - disable north
	 *                  2 - disable east
	 *                  4 - disable south
	 *                  8 - disable west
	 *                  16 - disable up
	 *                  32 - disable down
	 * @return number of triangles
	 */
	public static int coloredCube_1x1(int x, int y, int z, int color, int faceFlags)
	{
		return coloredCube(x, y, z, 1, 1, 1, color, faceFlags);
	}

	/**
	 *
	 * @param x x coordinate on cube
	 * @param y y coordinate on cube
	 * @param z z coordinate on cube
	 * @param w width (x)
	 * @param h height (y)
	 * @param d depth (z)
	 * @param color hex color
	 * @param faceFlags flags to disable rendering of faces
	 *                  1 - disable north
	 *                  2 - disable east
	 *                  4 - disable south
	 *                  8 - disable west
	 *                  16 - disable up
	 *                  32 - disable down
	 * @return number of triangles
	 */
	public static int coloredCube(int x, int y, int z, int w, int h, int d, int color, int faceFlags)
	{
		Vector3f from = new Vector3f(x * 1f / 16f, y * 1f / 16f, z * 1f / 16f);
		Vector3f to = new Vector3f(x * 1f / 16f + w * 1f / 16f, y * 1f / 16f + h * 1f / 16f, z * 1f / 16f + d * 1f / 16f);

		int tris = 0;

		if ((faceFlags & 1) != 1) tris += coloredQuad(
			new Vector3f(to.x, to.y, to.z),
			new Vector3f(to.x, from.y, to.z),
			new Vector3f(to.x, from.y, from.z),
			new Vector3f(to.x, to.y, from.z), color);
		if ((faceFlags & 2) != 2) tris += coloredQuad(
			new Vector3f(from.x, to.y, to.z),
			new Vector3f(from.x, from.y, to.z),
			new Vector3f(to.x, from.y, to.z),
			new Vector3f(to.x, to.y, to.z), color);
		if ((faceFlags & 4) != 4) tris += coloredQuad(
			new Vector3f(from.x, to.y, from.z),
			new Vector3f(from.x, from.y, from.z),
			new Vector3f(from.x, from.y, to.z),
			new Vector3f(from.x, to.y, to.z), color);
		if ((faceFlags & 8) != 8) tris += coloredQuad(
			new Vector3f(to.x, to.y, from.z),
			new Vector3f(to.x, from.y, from.z),
			new Vector3f(from.x, from.y, from.z),
			new Vector3f(from.x, to.y, from.z), color);
		if ((faceFlags & 16) != 16) tris += coloredQuad(
			new Vector3f(to.x, to.y, from.z),
			new Vector3f(from.x, to.y, from.z),
			new Vector3f(from.x, to.y, to.z),
			new Vector3f(to.x, to.y, to.z), color);
		if ((faceFlags & 32) != 32) tris += coloredQuad(
			new Vector3f(from.x, from.y, from.z),
			new Vector3f(to.x, from.y, from.z),
			new Vector3f(to.x, from.y, to.z),
			new Vector3f(from.x, from.y, to.z), color);

		return tris;
	}

	/**
	 *
	 * @param x x coordinate on cube
	 * @param y y coordinate on cube
	 * @param z z coordinate on cube
	 * @param w width (x)
	 * @param h height (y)
	 * @param d depth (z)
	 * @param texture block texture (has to be loaded)
	 * @param faceFlags flags to disable rendering of faces
	 *                  1 - disable north
	 *                  2 - disable east
	 *                  4 - disable south
	 *                  8 - disable west
	 *                  16 - disable up
	 *                  32 - disable down
	 * @return number of triangles
	 */
	public static int autoTexturedCube(int x, int y, int z, int w, int h, int d, String texture, int faceFlags)
	{
		return autoTexturedCube(x, y, z, w, h, d, texture, faceFlags, 0);
	}

	/**
	 *
	 * @param x x coordinate on cube
	 * @param y y coordinate on cube
	 * @param z z coordinate on cube
	 * @param w width (x)
	 * @param h height (y)
	 * @param d depth (z)
	 * @param texture block texture (has to be loaded)
	 * @param faceFlags flags to disable rendering of faces
	 *                  1 - disable north
	 *                  2 - disable east
	 *                  4 - disable south
	 *                  8 - disable west
	 *                  16 - disable up
	 *                  32 - disable down
	 * @return number of triangles
	 */
	public static int autoTexturedCube(float x, float y, float z, float w, float h, float d, String texture, int faceFlags)
	{
		return autoTexturedCube(x, y, z, w, h, d, texture, faceFlags, 0);
	}

	/**
	 *
	 * @param x x coordinate on cube
	 * @param y y coordinate on cube
	 * @param z z coordinate on cube
	 * @param w width (x)
	 * @param h height (y)
	 * @param d depth (z)
	 * @param texture block texture (has to be loaded)
	 * @param faceFlags flags to disable rendering of faces
	 *                  1 - disable north
	 *                  2 - disable east
	 *                  4 - disable south
	 *                  8 - disable west
	 *                  16 - disable up
	 *                  32 - disable down
	 * @param biomeTintFlags flags to enable biome tint
	 *                       use same bits as faceFlags
	 * @return number of triangles
	 */
	public static int autoTexturedCube(int x, int y, int z, int w, int h, int d, String texture, int faceFlags, int biomeTintFlags)
	{
		return autoTexturedCube((float) x, y, z, w, h, d, texture, faceFlags, biomeTintFlags);
	}

	/**
	 *
	 * @param x x coordinate on cube
	 * @param y y coordinate on cube
	 * @param z z coordinate on cube
	 * @param w width (x)
	 * @param h height (y)
	 * @param d depth (z)
	 * @param texture block texture (has to be loaded)
	 * @param faceFlags flags to disable rendering of faces
	 *                  1 - disable north
	 *                  2 - disable east
	 *                  4 - disable south
	 *                  8 - disable west
	 *                  16 - disable up
	 *                  32 - disable down
	 * @param biomeTintFlags flags to enable biome tint
	 *                       use same bits as faceFlags
	 * @return number of triangles
	 */
	public static int autoTexturedCube(float x, float y, float z, float w, float h, float d, String texture, int faceFlags, int biomeTintFlags)
	{
		Vector3f from = new Vector3f(x / 16f, y / 16f, z / 16f);
		Vector3f to = new Vector3f(x / 16f + w / 16f, y / 16f + h / 16f, z / 16f + d / 16f);

		int tex = BlockAtlas.getTextureId(texture);

		int tris = 0;

		if ((faceFlags & 1) != 1) tris += texturedQuad(
			new Vector3f(to.x, to.y, to.z),
			new Vector3f(to.x, from.y, to.z),
			new Vector3f(to.x, from.y, from.z),
			new Vector3f(to.x, to.y, from.z),
			uv(tex, new Vector2f(1 - to.z, 1 - to.y)),
			uv(tex, new Vector2f(1 - to.z, 1 - from.y)),
			uv(tex, new Vector2f(1 - from.z, 1 - from.y)),
			uv(tex, new Vector2f(1 - from.z, 1 - to.y)),
			(biomeTintFlags & 1) == 1
		);

		if ((faceFlags & 2) != 2) tris += texturedQuad(
			new Vector3f(from.x, to.y, to.z),
			new Vector3f(from.x, from.y, to.z),
			new Vector3f(to.x, from.y, to.z),
			new Vector3f(to.x, to.y, to.z),
			uv(tex, new Vector2f(from.x, 1 - to.y)),
			uv(tex, new Vector2f(from.x, 1 - from.y)),
			uv(tex, new Vector2f(to.x, 1 - from.y)),
			uv(tex, new Vector2f(to.x, 1 - to.y)),
			(biomeTintFlags & 2) == 2
		);

		if ((faceFlags & 4) != 4) tris += texturedQuad(
			new Vector3f(from.x, to.y, from.z),
			new Vector3f(from.x, from.y, from.z),
			new Vector3f(from.x, from.y, to.z),
			new Vector3f(from.x, to.y, to.z),
			uv(tex, new Vector2f(from.z, 1 - to.y)),
			uv(tex, new Vector2f(from.z, 1 - from.y)),
			uv(tex, new Vector2f(to.z, 1 - from.y)),
			uv(tex, new Vector2f(to.z, 1 - to.y)),
			(biomeTintFlags & 4) == 4
		);

		if ((faceFlags & 8) != 8) tris += texturedQuad(
			new Vector3f(to.x, to.y, from.z),
			new Vector3f(to.x, from.y, from.z),
			new Vector3f(from.x, from.y, from.z),
			new Vector3f(from.x, to.y, from.z),
			uv(tex, new Vector2f(1 - to.x, 1 - to.y)),
			uv(tex, new Vector2f(1 - to.x, 1 - from.y)),
			uv(tex, new Vector2f(1 - from.x, 1 - from.y)),
			uv(tex, new Vector2f(1 - from.x, 1 - to.y)),
			(biomeTintFlags & 8) == 8
		);

		if ((faceFlags & 16) != 16) tris += texturedQuad(
			new Vector3f(to.x, to.y, from.z),
			new Vector3f(from.x, to.y, from.z),
			new Vector3f(from.x, to.y, to.z),
			new Vector3f(to.x, to.y, to.z),
			uv(tex, new Vector2f(1 - to.x, from.z)),
			uv(tex, new Vector2f(1 - from.x, from.z)),
			uv(tex, new Vector2f(1 - from.x, to.z)),
			uv(tex, new Vector2f(1 - to.x, to.z)),
			(biomeTintFlags & 16) == 16
		);

		if ((faceFlags & 32) != 32) tris += texturedQuad(
			new Vector3f(from.x, from.y, from.z),
			new Vector3f(to.x, from.y, from.z),
			new Vector3f(to.x, from.y, to.z),
			new Vector3f(from.x, from.y, to.z),
			uv(tex, new Vector2f(1 - to.x, from.z)),
			uv(tex, new Vector2f(1 - from.x, from.z)),
			uv(tex, new Vector2f(1 - from.x, to.z)),
			uv(tex, new Vector2f(1 - to.x, to.z)),
			(biomeTintFlags & 32) == 32
		);

		return tris;
	}


	static int face(EnumFace face, CubeBaker cubeBaker, FaceBaker faceBaker, byte flip)
	{
		Vector3f from = new Vector3f(cubeBaker.getBox().minX, cubeBaker.getBox().minY, cubeBaker.getBox().minZ);
		Vector3f to = new Vector3f(cubeBaker.getBox().maxX, cubeBaker.getBox().maxY, cubeBaker.getBox().maxZ);

		int tris = 0;

		if (face == EnumFace.NORTH) tris += quad(
			new Vector3f(to.x, to.y, to.z),
			new Vector3f(to.x, from.y, to.z),
			new Vector3f(to.x, from.y, from.z),
			new Vector3f(to.x, to.y, from.z), faceBaker, flip);
		if (face == EnumFace.EAST) tris += quad(
			new Vector3f(from.x, to.y, to.z),
			new Vector3f(from.x, from.y, to.z),
			new Vector3f(to.x, from.y, to.z),
			new Vector3f(to.x, to.y, to.z), faceBaker, flip);
		if (face == EnumFace.SOUTH) tris += quad(
			new Vector3f(from.x, to.y, from.z),
			new Vector3f(from.x, from.y, from.z),
			new Vector3f(from.x, from.y, to.z),
			new Vector3f(from.x, to.y, to.z), faceBaker, flip);
		if (face == EnumFace.WEST) tris += quad(
			new Vector3f(to.x, to.y, from.z),
			new Vector3f(to.x, from.y, from.z),
			new Vector3f(from.x, from.y, from.z),
			new Vector3f(from.x, to.y, from.z), faceBaker, flip);
		if (face == EnumFace.UP) tris += quad(
			new Vector3f(to.x, to.y, from.z),
			new Vector3f(from.x, to.y, from.z),
			new Vector3f(from.x, to.y, to.z),
			new Vector3f(to.x, to.y, to.z), faceBaker, flip);
		if (face == EnumFace.DOWN) tris += quad(
			new Vector3f(from.x, from.y, from.z),
			new Vector3f(to.x, from.y, from.z),
			new Vector3f(to.x, from.y, to.z),
			new Vector3f(from.x, from.y, to.z), faceBaker, flip);

		return tris;
	}

	static int quad(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, FaceBaker face, byte flip)
	{
		UvBuilder uv = face.uv;

		if ((flip & 1) == 1)
		{
			Vector3f temp = v0;
			v0 = v3;
			v3 = temp;
			temp = v1;
			v1 = v2;
			v2 = temp;
			uv.flipX();
		}

		if ((flip & 2) == 2)
		{
			Vector3f temp = v0;
			v0 = v3;
			v3 = temp;
			temp = v1;
			v1 = v2;
			v2 = temp;
			uv.flipX();
		}

		if ((flip & 4) == 4)
		{
			Vector3f temp = v0;
			v0 = v3;
			v3 = temp;
			temp = v1;
			v1 = v2;
			v2 = temp;
			uv.flipX();
		}

		GeometryUtils.normal(v0, v1, v2, normal);
		Vector3f norm = new Vector3f(normal);
		int texture = BlockAtlas.getTextureId(face.getTexture());

		builder.tri(v0, v1, v2);
		builder.uv(uv(texture, uv.u0, uv.v0));
		builder.uv(uv(texture, uv.u0, uv.v1));
		builder.uv(uv(texture, uv.u1, uv.v1));
		builder.normalTri(norm);

		builder.tri(v2, v3, v0);
		builder.uv(uv(texture, uv.u1, uv.v1));
		builder.uv(uv(texture, uv.u1, uv.v0));
		builder.uv(uv(texture, uv.u0, uv.v0));
		builder.normalTri(norm);

		if (face.biomeTint)
		{
			Vector3f bt = builder.getBiomeTint();
			builder.colorTri(bt.x, bt.y, bt.z);
			builder.colorTri(bt.x, bt.y, bt.z);
		} else if (face.tint != null)
		{
			builder.colorTri(face.tint.x, face.tint.y, face.tint.z);
			builder.colorTri(face.tint.x, face.tint.y, face.tint.z);
		} else
		{
			builder.colorTri(1, 1, 1);
			builder.colorTri(1, 1, 1);
		}

		return 2;
	}

	public static int coloredQuad(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, int color)
	{
		return coloredTriangle(v0, v1, v2, color) + coloredTriangle(v2, v3, v0, color);
	}

	public static int texturedQuad(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, Vector2f uv0, Vector2f uv1, Vector2f uv2, Vector2f uv3)
	{
		return texturedQuad(v0, v1, v2, v3, uv0, uv1, uv2, uv3, false);
	}

	public static int texturedQuad(Vector3f v0, Vector3f v1, Vector3f v2, Vector3f v3, Vector2f uv0, Vector2f uv1, Vector2f uv2, Vector2f uv3, boolean biomeTint)
	{
		return texturedTriangle(v0, v1, v2, uv0, uv1, uv2, biomeTint) + texturedTriangle(v2, v3, v0, uv2, uv3, uv0, biomeTint);
	}

	public static int coloredTriangle(Vector3f v0, Vector3f v1, Vector3f v2, int color)
	{
		GeometryUtils.normal(v0, v1, v2, normal);

		builder.tri(v0, v1, v2);
		builder.uv(whiteUV);
		builder.uv(whiteUV);
		builder.uv(whiteUV);
		builder.normalTri(new Vector3f(normal));
		builder.colorTri(ColorUtil.getRed(color) / 255f, ColorUtil.getGreen(color) / 255f, ColorUtil.getBlue(color) / 255f);

		return 1;
	}

	public static int texturedTriangle(Vector3f v0, Vector3f v1, Vector3f v2, Vector2f uv0, Vector2f uv1, Vector2f uv2)
	{
		return texturedTriangle(v0, v1, v2, uv0, uv1, uv2, false);
	}

	public static int texturedTriangle(Vector3f v0, Vector3f v1, Vector3f v2, Vector2f uv0, Vector2f uv1, Vector2f uv2, boolean biomeTint)
	{
		GeometryUtils.normal(v0, v1, v2, normal);

		builder.tri(v0, v1, v2);
		builder.uv(uv0);
		builder.uv(uv1);
		builder.uv(uv2);
		builder.normalTri(new Vector3f(normal));

		if (biomeTint)
		{
			Vector3f bt = builder.getBiomeTint();
			builder.colorTri(bt.x, bt.y, bt.z);
		} else
		{
			builder.colorTri(1, 1, 1);
		}

		return 1;
	}

	public static Vector2f uv(int texture, Vector2f uv)
	{
		Rectangle r = BlockAtlas.getTexture(texture);
		float x = r.x;
		float y = r.y;
		float w = r.width;
		float h = r.height;
		uv.set((x + w * uv.x) * builder.texel, (y + h * uv.y) * builder.texel);
		return uv;
	}

	public static Vector2f uv(int texture, float u, float v)
	{
		return uv(texture, new Vector2f(u, v));
	}

	public static Vector2f uv(String texture, Vector2f uv)
	{
		return uv(BlockAtlas.getTextureId(texture), uv);
	}
}
