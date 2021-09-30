package steve6472.polyground.world.generator.feature.cave;

import org.json.JSONObject;
import steve6472.polyground.world.World;
import steve6472.polyground.world.generator.EnumPlacement;
import steve6472.polyground.world.generator.Feature;
import steve6472.polyground.world.generator.feature.components.match.IBlockMatch;
import steve6472.polyground.world.generator.feature.components.match.Match;
import steve6472.polyground.world.generator.feature.components.provider.IBlockProvider;
import steve6472.polyground.world.generator.feature.components.provider.Provider;
import steve6472.sge.main.util.MathUtil;
import steve6472.sge.main.util.RandomUtil;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.08.2020
 * Project: CaveGame
 *
 ***********************/
public class OreVein extends Feature
{
	private IBlockMatch target;
	private IBlockProvider ore;
//	private BlockState target, ore;
	private int minSize, maxSize;
	private double chance;

	public OreVein()
	{
	}

	public OreVein(IBlockMatch target, IBlockProvider ore, int minSize, int maxSize, double chance)
	{
		this.target = target;
		this.ore = ore;
		this.minSize = MathUtil.clamp(0, 30, minSize);
		this.maxSize = MathUtil.clamp(1, 31, maxSize);
		this.chance = chance;
	}

	@Override
	public void load(JSONObject json)
	{
		target = Match.match(json.getJSONObject("target"));
		ore = Provider.provide(json.getJSONObject("ore"));
//		target = Blocks.loadStateFromJSON(json.getJSONObject("target"));
//		ore = Blocks.loadStateFromJSON(json.getJSONObject("ore"));
		minSize = MathUtil.clamp(0, 30, json.getInt("min_size"));
		maxSize = MathUtil.clamp(1, 31, json.getInt("max_size"));
		chance = json.getDouble("chance");
	}

	@Override
	public void generate(World world, int x, int y, int z)
	{
		int xRadius = RandomUtil.randomInt(minSize, maxSize);
		int yRadius = RandomUtil.randomInt(minSize, maxSize);
		int zRadius = RandomUtil.randomInt(minSize, maxSize);

		for (int i = -xRadius; i < xRadius; i++)
		{
			for (int j = -yRadius; j < yRadius; j++)
			{
				for (int k = -zRadius; k < zRadius; k++)
				{
					double X = Math.pow(((i + 0.5) / (float) xRadius), 2);
					double Y = Math.pow(((j + 0.5) / (float) yRadius), 2);
					double Z = Math.pow(((k + 0.5) / (float) zRadius), 2);

					if (X + Y + Z < 1 && world.getRandom().nextDouble() <= chance && target.matches(world.getState(x + i, y + j, z + k)))
					{
						world.setState(ore.getState(world, i + x, j + y, k + z), i + x, j + y, k + z);
					}
				}
			}
		}
	}

	/**
	 * Specifies how many chunks in square radius have to be present for this feature to generate
	 * max is 3
	 * 0 is self
	 *
	 * @return size
	 */
	@Override
	public int size()
	{
		return 2;
	}

	@Override
	public boolean canGenerate(World world, int x, int y, int z)
	{
		return target.matches(world.getState(x, y, z));
	}

	@Override
	public EnumPlacement getPlacement()
	{
		return EnumPlacement.IN_GROUND;
	}
}
