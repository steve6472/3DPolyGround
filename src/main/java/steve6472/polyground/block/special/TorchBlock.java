package steve6472.polyground.block.special;

import org.joml.Vector3f;
import org.json.JSONObject;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.gfx.light.EnumLightSource;
import steve6472.polyground.gfx.light.Light;
import steve6472.polyground.gfx.light.LightManager;
import steve6472.polyground.gfx.particle.particles.LightParticle;
import steve6472.polyground.gfx.particle.particles.torch.motion.AcidFormula;
import steve6472.polyground.gfx.particle.particles.torch.motion.Formula;
import steve6472.polyground.gfx.particle.particles.torch.motion.TorchFormula;
import steve6472.polyground.gfx.particle.particles.torch.spawner.BoxSpawner;
import steve6472.polyground.gfx.particle.particles.torch.spawner.Spawner;
import steve6472.polyground.gfx.particle.particles.torch.spawner.SphereSpawner;
import steve6472.polyground.world.World;
import steve6472.sge.main.util.ColorUtil;
import steve6472.sge.main.util.RandomUtil;

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
	private float r, g, b, a;
	private float constant, linear, quadratic;
	private EnumMotionFormula motionFormula;
	private JSONObject formulaData;
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

	private static Spawner getSpawner(EnumSpawner formula, JSONObject formulaData)
	{
		Spawner f = spawners.get(formula).get();
		f.loadSpawnData(formulaData);
		return f;
	}

	private static Formula getFormula(EnumMotionFormula formula, JSONObject formulaData)
	{
		Formula f = formulas.get(formula).get();
		f.loadFormulaData(formulaData);
		return f;
	}

	public TorchBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public void load(JSONObject json)
	{
		int color;

		if (json.has("color"))
			color = (int) Long.parseLong(json.getString("color"), 16);
		else
			color = 0xffffff;

		float[] col = ColorUtil.getColors(color);
		r = col[0];
		g = col[1];
		b = col[2];
		a = col[3];

		constant = json.getFloat("constant");
		linear = json.getFloat("linear");
		quadratic = json.getFloat("quadratic");

		motionFormula = EnumMotionFormula.TORCH;
		if (json.has("motion_formula")) motionFormula = EnumMotionFormula.valueOf(json.getString("motion_formula"));

		if (json.has("data_formula"))
		{
			formulaData = json.getJSONObject("data_formula");
		}

		EnumSpawner enumSpawner = null;
		if (json.has("spawner")) enumSpawner = EnumSpawner.valueOf(json.getString("spawner"));

		if (json.has("data_spawner"))
		{
			JSONObject arr = json.getJSONObject("data_spawner");

			if (enumSpawner != null)
				spawner = getSpawner(enumSpawner, arr);
		}
	}

	@Override
	public void tick(BlockState state, World world, int x, int y, int z)
	{
		if (spawner == null)
			return;

		if (!RandomUtil.decide(6))
			return;

		Light l = LightManager.replaceIdeal(EnumLightSource.PARTICLE, 0, 0, 0, 0, 0, 0, constant, linear, quadratic, 0, 0, 0, 0);

		if (l == null)
			return;

		Vector3f newPosition = new Vector3f();
		spawner.spawn(newPosition, world, x, y, z);

		LightParticle p = new LightParticle(
			getFormula(motionFormula, formulaData),
			newPosition,
			RandomUtil.randomFloat(1f / 48f, 1f / 24f),
			r, g, b, a, l
			);
		world.getGame().mainRender.particles.addParticle(p);
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
