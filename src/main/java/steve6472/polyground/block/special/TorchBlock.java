package steve6472.polyground.block.special;

import org.joml.Vector3f;
import steve6472.SSS;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.model.faceProperty.LightFaceProperty;
import steve6472.polyground.block.model.registry.Cube;
import steve6472.polyground.block.model.registry.face.FaceRegistry;
import steve6472.polyground.gfx.particle.particles.LightParticle;
import steve6472.polyground.gfx.particle.particles.torch.motion.AcidFormula;
import steve6472.polyground.gfx.particle.particles.torch.motion.Formula;
import steve6472.polyground.gfx.particle.particles.torch.motion.TorchFormula;
import steve6472.polyground.gfx.particle.particles.torch.spawner.BoxSpawner;
import steve6472.polyground.gfx.particle.particles.torch.spawner.Spawner;
import steve6472.polyground.gfx.particle.particles.torch.spawner.SphereSpawner;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.polyground.world.light.EnumLightSource;
import steve6472.polyground.world.light.Light;
import steve6472.polyground.world.light.LightManager;
import steve6472.sge.main.util.ColorUtil;
import steve6472.sge.main.util.RandomUtil;

import java.io.File;
import java.util.HashMap;
import java.util.function.Supplier;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.11.2019
 * Project: SJP
 *
 ***********************/
public class TorchBlock extends Block
{
	private File f;
	private float r, g, b, a;
	private float constant, linear, quadratic;
	private EnumMotionFormula motionFormula;
	private SSS formulaData;
	private SSS spawnerData;
	private Spawner spawner;

	private static HashMap<EnumMotionFormula, Supplier<Formula>> formulas;
	private static HashMap<EnumSpawner, Supplier<Spawner>> spawners;

	static
	{
		formulas = new HashMap<>();
		formulas.put(EnumMotionFormula.TORCH, TorchFormula::new);
		formulas.put(EnumMotionFormula.ACID, AcidFormula::new);

		spawners = new HashMap<>();
		spawners.put(EnumSpawner.BOX, BoxSpawner::new);
		spawners.put(EnumSpawner.SPHERE, SphereSpawner::new);
	}

	private static Spawner getSpawner(EnumSpawner formula, SSS formulaData)
	{
		Spawner f = spawners.get(formula).get();
		f.loadSpawnData(formulaData);
		return f;
	}

	private static Formula getFormula(EnumMotionFormula formula, SSS formulaData)
	{
		Formula f = formulas.get(formula).get();
		f.loadFormulaData(formulaData);
		return f;
	}

	public TorchBlock(File f, int id)
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

			constant = sss.getFloat("constant");
			linear = sss.getFloat("linear");
			quadratic = sss.getFloat("quadratic");

			motionFormula = EnumMotionFormula.TORCH;
			if (sss.containsName("motionFormula")) motionFormula = EnumMotionFormula.valueOf(sss.getString("motionFormula"));

			if (sss.containsName("formulaData"))
			{
				String[] data = sss.getStringArray("formulaData");
				formulaData = new SSS(data);
			}

			EnumSpawner enumSpawner = null;
			if (sss.containsName("spawner")) enumSpawner = EnumSpawner.valueOf(sss.getString("spawner"));

			if (sss.containsName("spawnerData"))
			{
				String[] data = sss.getStringArray("spawnerData");
				spawnerData = new SSS(data);
			}
			if (enumSpawner != null)
				spawner = getSpawner(enumSpawner, spawnerData);
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
		if (spawner == null)
			return;

		if (!RandomUtil.decide(6))
			return;

		Light l = LightManager.replaceIdeal(EnumLightSource.PARTICLE, 0, 0, 0, 0, 0, 0, constant, linear, quadratic);

		if (l == null)
			return;

		Vector3f newPosition = new Vector3f();
		spawner.spawn(newPosition, subChunk, x, y, z);

		LightParticle p = new LightParticle(
			getFormula(motionFormula, formulaData),
			newPosition,
			RandomUtil.randomFloat(1f / 48f, 1f / 24f),
			r, g, b, a, l
			);
		subChunk.getWorld().getPg().particles.addParticle(p);
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
		TORCH, UP, BEAM, ACID
	}

	private enum EnumSpawner
	{
		BOX, SPHERE
	}
}
