package steve6472.polyground.block.special;

import org.joml.Vector3f;
import steve6472.SSS;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.model.faceProperty.LightFaceProperty;
import steve6472.polyground.block.model.registry.Cube;
import steve6472.polyground.block.model.registry.face.FaceRegistry;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.polyground.world.light.EnumLightSource;
import steve6472.polyground.world.light.Light;
import steve6472.polyground.world.light.LightManager;
import steve6472.sge.main.util.ColorUtil;
import steve6472.sge.main.util.RandomUtil;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.11.2019
 * Project: SJP
 *
 ***********************/
public class OldTorchBlock extends Block
{
	private File f;
	private float r, g, b, a;
	private float constant, linear, quadratic;
	private float xOffset, yOffset, zOffset;
	private float spawnRadiusX, spawnRadiusY, spawnRadiusZ, spawnRadius;
	private boolean sphereRadius;

	public OldTorchBlock(File f, int id)
	{
		super(f, id);
		this.f = f;
	}

	@Override
	public void postLoad()
	{
		if (f.isFile())
		{
			int color;

			SSS sss = new SSS(f);
			if (sss.hasValue("color"))
				color = sss.getHexInt("color");
			else
				color = 0xffffffff;

			float[] col = ColorUtil.getColors(color);
			r = col[0];
			g = col[1];
			b = col[2];
			a = col[3];

			spawnRadiusX = spawnRadiusY = spawnRadiusZ = spawnRadius = 1f / 16f + 1f / 32f;

			constant = sss.getFloat("constant");
			linear = sss.getFloat("linear");
			quadratic = sss.getFloat("quadratic");

			if (sss.containsName("lightXOffset")) xOffset = sss.getFloat("lightXOffset") / 16f;
			if (sss.containsName("lightYOffset")) yOffset = sss.getFloat("lightYOffset") / 16f;
			if (sss.containsName("lightZOffset")) zOffset = sss.getFloat("lightZOffset") / 16f;

			if (sss.containsName("spawnRadiusX")) spawnRadiusX = sss.getFloat("spawnRadiusX") / 16f;
			if (sss.containsName("spawnRadiusY")) spawnRadiusY = sss.getFloat("spawnRadiusY") / 16f;
			if (sss.containsName("spawnRadiusZ")) spawnRadiusZ = sss.getFloat("spawnRadiusZ") / 16f;
			if (sss.containsName("spawnRadius")) spawnRadius = sss.getFloat("spawnRadius") / 16f;

			if (sss.containsName("sphereRadius")) sphereRadius = sss.getBoolean("sphereRadius");
		}

		for (Cube cube : getBlockModel().getCubes())
		{
			for (CubeFace face : cube.getFaces())
			{
				if (face == null) continue;
				if (face.hasProperty(FaceRegistry.light))
				{
					System.err.println("Replacing existing light property!");
				}
				face.removeProperty(FaceRegistry.light);

				face.addProperty(new LightFaceProperty(r, g, b));
			}
		}

		f = null;
	}

	@Override
	public void tick(SubChunk subChunk, BlockData blockData, int x, int y, int z)
	{
		if (!RandomUtil.decide(6))
			return;

		Light l = LightManager.replaceIdeal(EnumLightSource.PARTICLE, 0, 0, 0, 0, 0, 0, constant, linear, quadratic);

		if (l == null)
			return;

		float posX = x + 0.5f + xOffset;
		float posY = y + 0.5f + yOffset;
		float posZ = z + 0.5f + zOffset;

		float dx;
		float dz;

		if (sphereRadius)
		{
			Vector3f pos = getRandomSpherePos(spawnRadius);
			dx = pos.x();
			dz = pos.z();

			posY += pos.y();
		} else
		{
			dx = RandomUtil.randomFloat(-spawnRadiusX, spawnRadiusX);
			dz = RandomUtil.randomFloat(-spawnRadiusZ, spawnRadiusZ);

			posY += RandomUtil.randomFloat(spawnRadiusY, spawnRadiusY);
		}
		posX += dx;
		posZ += dz;

		posX += subChunk.getX() * 16;
		posY += subChunk.getLayer() * 16;
		posZ += subChunk.getZ() * 16;
/*
		LightParticle p = new LightParticle(
			new Vector3f(-dx / 30f, RandomUtil.randomFloat(0.015f, 0.025f), -dz / 30f),
			new Vector3f(posX, posY, posZ),
			RandomUtil.randomFloat(1f / 48f, 1f / 24f),
			r, g, b, a, l
			);
		subChunk.getWorld().getPg().particles.addParticle(p);*/
	}

	/**
	 * @author https://karthikkaranth.me/blog/generating-random-points-in-a-sphere/
	 */
	private Vector3f getRandomSpherePos(float radius)
	{
		double u = Math.random();
		float x1 = RandomUtil.randomFloat(0f, radius);
		float x2 = RandomUtil.randomFloat(0f, radius);
		float x3 = RandomUtil.randomFloat(0f, radius);

		float mag = (float) Math.sqrt(x1*x1 + x2*x2 + x3*x3);
		x1 /= mag; x2 /= mag; x3 /= mag;

		// Math.cbrt is cube root
		float c = (float) Math.cbrt(u);

		return new Vector3f(x1 * c, x2 * c, x3 * c);
	}

	@Override
	public boolean isTickable()
	{
		return true;
	}

	private enum EnumMotionFormula
	{
		TORCH, UP, BEAM
	}
}
