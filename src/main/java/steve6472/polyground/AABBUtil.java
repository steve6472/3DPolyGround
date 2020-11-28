package steve6472.polyground;

import org.joml.AABBf;
import org.joml.Vector3f;
import steve6472.polyground.tessellators.BasicTessellator;
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

	public static Vector3f getCenter(AABBf box)
	{
		return new Vector3f((box.maxX + box.minX) / 2.0f, (box.maxY + box.minY) / 2.0f, (box.maxZ + box.minZ) / 2.0f);
	}
}
