package steve6472.polyground.world.generator.feature;


import org.json.JSONObject;
import steve6472.polyground.block.Block;
import steve6472.polyground.world.World;
import steve6472.polyground.world.generator.EnumPlacement;
import steve6472.polyground.world.generator.Feature;
import steve6472.polyground.world.generator.feature.components.match.IBlockMatch;
import steve6472.polyground.world.generator.feature.components.match.Match;
import steve6472.polyground.world.generator.feature.components.provider.IBlockProvider;
import steve6472.polyground.world.generator.feature.components.provider.Provider;
import steve6472.sge.main.Util;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.08.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class TopSnowFeature extends Feature
{
	private IBlockMatch target;
	private IBlockProvider block;
//	private BlockState snow;
//	private BlockState target;
	private int heightStart, heightMax;

//	public TopSnowFeature(BlockState target, BlockState snow, int heightStart, int heightMax)
//	{
//		this.target = target;
//		this.snow = snow;
//		this.heightStart = heightStart;
//		this.heightMax = heightMax;
//	}

	@Override
	public void load(JSONObject json)
	{
		block = Provider.provide(json.getJSONObject("snow"));
		target = Match.match(json.getJSONObject("target"));
//		snow = Blocks.loadStateFromJSON(json.getJSONObject("snow"));
//		target = Blocks.loadStateFromJSON(json.getJSONObject("target"));
		heightStart = json.getInt("height_start");
		heightMax = json.getInt("height_max");
	}

	@Override
	public void generate(World world, int x, int y, int z)
	{
		if (world.getBlock(x, y, z) == Block.AIR)
		{
			if (y >= heightStart && y < heightMax)
			{
				if (world.getRandom().nextDouble() <= smoothstep(heightStart, heightMax, y))
					world.setState(block.getState(world, x, y, z), x, y, z, 5);
			} else if (y >= heightMax)
			{
				world.setState(block.getState(world, x, y, z), x, y, z, 5);
			}
		}
	}

	private static double smoothstep(double edge0, double edge1, double n)
	{
		n = Util.clamp(0.0, 1.0, (n - edge0) / (edge1 - edge0));
		return n * n * (3.0 - 2.0 * n);
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
		return 0;
	}

	@Override
	public boolean canGenerate(World world, int x, int y, int z)
	{
		return target.matches(world.getState(x, y - 1, z));
	}

	@Override
	public EnumPlacement getPlacement()
	{
		return EnumPlacement.ON_HEIGHT_MAP;
	}
}
