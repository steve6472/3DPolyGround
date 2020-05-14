package steve6472.polyground;

import org.joml.AABBf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import steve6472.polyground.gfx.shaders.MainShader;
import steve6472.polyground.tessellators.BasicTessellator;
import steve6472.sge.gfx.Tessellator;
import steve6472.sge.main.Util;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.08.2019
 * Project: SJP
 *
 ***********************/
public class AABBUtil
{
	private static final float EPSILON = 0.000001f;

	public static float clipXCollide(AABBf box, AABBf block, float xa)
	{
		if (!(box.maxY > block.minY && box.minY < block.maxY))
			return xa;
		if (!(box.maxZ > block.minZ && box.minZ < block.maxZ))
			return xa;

		float max;
		if (xa > 0.0F && box.maxX <= block.minX)
		{
			max = block.minX - box.maxX - EPSILON;
			if (max < xa)
				xa = max;
		}

		if (xa < 0.0F && box.minX >= block.maxX)
		{
			max = block.maxX - box.minX + EPSILON;
			if (max > xa)
				xa = max;
		}

		return xa;
	}

	public static float clipYCollide(AABBf box, AABBf block, float ya)
	{
		if (!(box.maxX > block.minX && box.minX < block.maxX))
			return ya;
		if (!(box.maxZ > block.minZ && box.minZ < block.maxZ))
			return ya;

		float max;
		if (ya > 0.0F && box.maxY <= block.minY)
		{
			max = block.minY - box.maxY - EPSILON;
			if (max < ya)
				ya = max;
		}

		if (ya < 0.0F && box.minY >= block.maxY)
		{
			max = block.maxY - box.minY + EPSILON;
			if (max > ya)
				ya = max;
		}

		return ya;
	}

	public static float clipZCollide(AABBf box, AABBf block, float za)
	{
		if (!(box.maxX > block.minX && box.minX < block.maxX))
			return za;
		if (!(box.maxY > block.minY && box.minY < block.maxY))
			return za;

		float max;
		if (za > 0.0F && box.maxZ <= block.minZ)
		{
			max = block.minZ - box.maxZ - EPSILON;
			if (max < za)
				za = max;
		}

		if (za < 0.0F && box.minZ >= block.maxZ)
		{
			max = block.maxZ - box.minZ + EPSILON;
			if (max > za)
				za = max;
		}

		return za;
	}

	public static void renderAABB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float lineWidth, BasicTessellator tess, MainShader mainShader)
	{
		mainShader.bind();
		tess.begin(24);

		GL11.glLineWidth(lineWidth);

		tess.pos(minX, minY, minZ).color(1f, 0f, 0f, 1f).endVertex();
		tess.pos(maxX, minY, minZ).color(1f, 0f, 0f, 1f).endVertex();
		tess.pos(minX, minY, minZ).color(0f, 0f, 1f, 1f).endVertex();
		tess.pos(minX, minY, maxZ).color(0f, 0f, 1f, 1f).endVertex();
		tess.pos(minX, minY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, minY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, minY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, minY, maxZ).color(1f, 1f, 1f, 1f).endVertex();

		tess.pos(minX, minY, minZ).color(0f, 1f, 0f, 1f).endVertex();
		tess.pos(minX, maxY, minZ).color(0f, 1f, 0f, 1f).endVertex();
		tess.pos(maxX, minY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, maxY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(minX, minY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(minX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, minY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();

		tess.pos(minX, maxY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, maxY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(minX, maxY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(minX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(minX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, maxY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();

		tess.loadPos(0);
		tess.loadColor(1);
		tess.draw(Tessellator.LINES);
		tess.disable(0, 1);
		GL11.glLineWidth(1f);
	}

	public static void addAABB(AABBf box, BasicTessellator tess)
	{
		addAABB(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, tess);
	}

	public static void addAABB(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, BasicTessellator tess)
	{
		tess.pos(minX, minY, minZ).color(1f, 0f, 0f, 1f).endVertex();
		tess.pos(maxX, minY, minZ).color(1f, 0f, 0f, 1f).endVertex();
		tess.pos(minX, minY, minZ).color(0f, 0f, 1f, 1f).endVertex();
		tess.pos(minX, minY, maxZ).color(0f, 0f, 1f, 1f).endVertex();
		tess.pos(minX, minY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, minY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, minY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, minY, maxZ).color(1f, 1f, 1f, 1f).endVertex();

		tess.pos(minX, minY, minZ).color(0f, 1f, 0f, 1f).endVertex();
		tess.pos(minX, maxY, minZ).color(0f, 1f, 0f, 1f).endVertex();
		tess.pos(maxX, minY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, maxY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(minX, minY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(minX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, minY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();

		tess.pos(minX, maxY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, maxY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(minX, maxY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(minX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(minX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, maxY, minZ).color(1f, 1f, 1f, 1f).endVertex();
		tess.pos(maxX, maxY, maxZ).color(1f, 1f, 1f, 1f).endVertex();
	}

	public static void addWater(float x, float y, float z, float height, BasicTessellator tess)
	{
		float density = 1;
		if (height > 1)
			density = Util.clamp(0, 1f, 1f - (float) Math.log10(height));

		height = Util.clamp(0.0005f, 1f, height);

		float r = 0.5f;
		float g = 0.75f;
		float b = 0.9f;
		float a = 1f;

//		tess.color((0.5f) * density, 0.75f * density, 0.9f * density, 0.5f);
		tess.color(r * density, g * density, b * density, a);

		// Top
		tess.pos(+1 + x, +1 * height + y, +0 + z).endVertex();
		tess.pos(+0 + x, +1 * height + y, +0 + z).endVertex();
		tess.pos(+0 + x, +1 * height + y, +1 + z).endVertex();

		tess.pos(+0 + x, +1 * height + y, +1 + z).endVertex();
		tess.pos(+1 + x, +1 * height + y, +1 + z).endVertex();
		tess.pos(+1 + x, +1 * height + y, +0 + z).endVertex();

//		tess.color(0.5f * 0.8f * density, 0.75f * 0.8f * density, 0.9f * 0.8f * density, 0.5f);
		tess.color(r * 0.8f * density, g * 0.8f * density, b * 0.8f * density, a);

		// East
		tess.pos(+0 + x, +1 * height + y, +0 + z).endVertex();
		tess.pos(+0 + x, +0 * height + y, +0 + z).endVertex();
		tess.pos(+0 + x, +0 * height + y, +1 + z).endVertex();

		tess.pos(+0 + x, +0 * height + y, +1 + z).endVertex();
		tess.pos(+0 + x, +1 * height + y, +1 + z).endVertex();
		tess.pos(+0 + x, +1 * height + y, +0 + z).endVertex();

//		tess.color(0.5f * 0.5f * density, 0.75f * 0.5f * density, 0.9f * 0.5f * density, 0.5f);
		tess.color(r * 0.5f * density, g * 0.5f * density, b * 0.5f * density, a);

		// North
		tess.pos(+0 + x, +1 * height + y, +1 + z).endVertex();
		tess.pos(+0 + x, +0 * height + y, +1 + z).endVertex();
		tess.pos(+1 + x, +0 * height + y, +1 + z).endVertex();

		tess.pos(+1 + x, +0 * height + y, +1 + z).endVertex();
		tess.pos(+1 + x, +1 * height + y, +1 + z).endVertex();
		tess.pos(+0 + x, +1 * height + y, +1 + z).endVertex();
//		tess.color(0.5f * 0.8f * density, 0.75f * 0.8f * density, 0.9f * 0.8f * density, 0.5f);
		tess.color(r * 0.8f * density, g * 0.8f * density, b * 0.8f * density, a);

		// West
		tess.pos(+1 + x, +1 * height + y, +1 + z).endVertex();
		tess.pos(+1 + x, +0 * height + y, +1 + z).endVertex();
		tess.pos(+1 + x, +0 * height + y, +0 + z).endVertex();

		tess.pos(+1 + x, +0 * height + y, +0 + z).endVertex();
		tess.pos(+1 + x, +1 * height + y, +0 + z).endVertex();
		tess.pos(+1 + x, +1 * height + y, +1 + z).endVertex();

//		tess.color(0.5f * 0.5f * density, 0.75f * 0.5f * density, 0.9f * 0.5f * density, 0.5f);
		tess.color(r * 0.5f * density, g * 0.5f * density, b * 0.5f * density, a);

		// South
		tess.pos(+1 + x, +1 * height + y, +0 + z).endVertex();
		tess.pos(+1 + x, +0 * height + y, +0 + z).endVertex();
		tess.pos(+0 + x, +0 * height + y, +0 + z).endVertex();

		tess.pos(+0 + x, +0 * height + y, +0 + z).endVertex();
		tess.pos(+0 + x, +1 * height + y, +0 + z).endVertex();
		tess.pos(+1 + x, +1 * height + y, +0 + z).endVertex();

//		tess.color(0.5f * 0.2f * density, 0.75f * 0.2f * density, 0.9f * 0.2f * density, 0.5f);
		tess.color(r * 0.2f * density, g * 0.2f * density, b * 0.2f * density, a);

		// Bottom
		tess.pos(+0 + x, +0 + y, +1 + z).endVertex();
		tess.pos(+0 + x, +0 + y, +0 + z).endVertex();
		tess.pos(+1 + x, +0 + y, +0 + z).endVertex();

		tess.pos(+1 + x, +0 + y, +0 + z).endVertex();
		tess.pos(+1 + x, +0 + y, +1 + z).endVertex();
		tess.pos(+0 + x, +0 + y, +1 + z).endVertex();
	}

	/*
	public static void renderBox(float width, float height, float depth, EntityCube ec, PolyGround pg)
	{
		renderBox(-width / 2f, -height / 2f, -depth / 2f, width / 2f, height / 2f, depth / 2f, ec, pg);
	}

	public static void renderBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, EntityCube ec, PolyGround pg)
	{
		EntityTessellator tess = pg.entityTessellator;
		tess.begin(36);

		EntityCubeFace face = ec.getFace(EnumFace.BACK);
		tess.color(face.getShade(), face.getShade(), face.getShade(), 1.0f);

		tess.pos(minX, maxY, minZ).texture(face.getMinU(), face.getMinV()).endVertex();
		tess.pos(minX, minY, minZ).texture(face.getMinU(), face.getMaxV()).endVertex();
		tess.pos(minX, minY, maxZ).texture(face.getMaxU(), face.getMaxV()).endVertex();

		tess.pos(minX, minY, maxZ).texture(face.getMaxU(), face.getMaxV()).endVertex();
		tess.pos(minX, maxY, maxZ).texture(face.getMaxU(), face.getMinV()).endVertex();
		tess.pos(minX, maxY, minZ).texture(face.getMinU(), face.getMinV()).endVertex();

		face = ec.getFace(EnumFace.FRONT);
		tess.color(face.getShade(), face.getShade(), face.getShade(), 1.0f);

		tess.pos(maxX, maxY, maxZ).texture(face.getMinU(), face.getMinV()).endVertex();
		tess.pos(maxX, minY, maxZ).texture(face.getMinU(), face.getMaxV()).endVertex();
		tess.pos(maxX, minY, minZ).texture(face.getMaxU(), face.getMaxV()).endVertex();

		tess.pos(maxX, minY, minZ).texture(face.getMaxU(), face.getMaxV()).endVertex();
		tess.pos(maxX, maxY, minZ).texture(face.getMaxU(), face.getMinV()).endVertex();
		tess.pos(maxX, maxY, maxZ).texture(face.getMinU(), face.getMinV()).endVertex();

		face = ec.getFace(EnumFace.TOP);
		tess.color(face.getShade(), face.getShade(), face.getShade(), 1.0f);

		tess.pos(maxX, maxY, minZ).texture(face.getMinU(), face.getMinV()).endVertex();
		tess.pos(minX, maxY, minZ).texture(face.getMinU(), face.getMaxV()).endVertex();
		tess.pos(minX, maxY, maxZ).texture(face.getMaxU(), face.getMaxV()).endVertex();

		tess.pos(minX, maxY, maxZ).texture(face.getMaxU(), face.getMaxV()).endVertex();
		tess.pos(maxX, maxY, maxZ).texture(face.getMaxU(), face.getMinV()).endVertex();
		tess.pos(maxX, maxY, minZ).texture(face.getMinU(), face.getMinV()).endVertex();

		face = ec.getFace(EnumFace.BOTTOM);
		tess.color(face.getShade(), face.getShade(), face.getShade(), 1.0f);

		tess.pos(minX, minY, maxZ).texture(face.getMaxU(), face.getMinV()).endVertex();
		tess.pos(minX, minY, minZ).texture(face.getMinU(), face.getMinV()).endVertex();
		tess.pos(maxX, minY, minZ).texture(face.getMinU(), face.getMaxV()).endVertex();

		tess.pos(maxX, minY, minZ).texture(face.getMinU(), face.getMaxV()).endVertex();
		tess.pos(maxX, minY, maxZ).texture(face.getMaxU(), face.getMaxV()).endVertex();
		tess.pos(minX, minY, maxZ).texture(face.getMaxU(), face.getMinV()).endVertex();

		face = ec.getFace(EnumFace.LEFT);
		tess.color(face.getShade(), face.getShade(), face.getShade(), 1.0f);


		tess.pos(maxX, maxY, minZ).texture(face.getMinU(), face.getMinV()).endVertex();
		tess.pos(maxX, minY, minZ).texture(face.getMinU(), face.getMaxV()).endVertex();
		tess.pos(minX, minY, minZ).texture(face.getMaxU(), face.getMaxV()).endVertex();

		tess.pos(minX, minY, minZ).texture(face.getMaxU(), face.getMaxV()).endVertex();
		tess.pos(minX, maxY, minZ).texture(face.getMaxU(), face.getMinV()).endVertex();
		tess.pos(maxX, maxY, minZ).texture(face.getMinU(), face.getMinV()).endVertex();

		face = ec.getFace(EnumFace.RIGHT);
		tess.color(face.getShade(), face.getShade(), face.getShade(), 1.0f);

		tess.pos(minX, maxY, maxZ).texture(face.getMinU(), face.getMinV()).endVertex();
		tess.pos(minX, minY, maxZ).texture(face.getMinU(), face.getMaxV()).endVertex();
		tess.pos(maxX, minY, maxZ).texture(face.getMaxU(), face.getMaxV()).endVertex();

		tess.pos(maxX, minY, maxZ).texture(face.getMaxU(), face.getMaxV()).endVertex();
		tess.pos(maxX, maxY, maxZ).texture(face.getMaxU(), face.getMinV()).endVertex();
		tess.pos(minX, maxY, maxZ).texture(face.getMinU(), face.getMinV()).endVertex();

		tess.loadPos(0);
		tess.loadColor(1);
		tess.loadTexture(2);
		tess.draw(Tessellator.TRIANGLES);
		tess.disable(0, 1, 2);
	}*/

	public static void renderAABBf(AABBf box, BasicTessellator tess, float lineWidth, MainShader mainShader)
	{
		renderAABB(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, lineWidth, tess, mainShader);
	}

	public static Vector3f getCenter(AABBf box)
	{
		return new Vector3f((box.maxX + box.minX) / 2.0f, (box.maxY + box.minY) / 2.0f, (box.maxZ + box.minZ) / 2.0f);
	}
}
