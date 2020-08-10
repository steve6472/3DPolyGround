package steve6472.polyground.world.generator.feature;


import steve6472.polyground.block.Block;
import steve6472.polyground.block.special.SnowLayerBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.World;
import steve6472.polyground.world.generator.EnumPlacement;
import steve6472.polyground.world.generator.IFeature;
import steve6472.sge.main.Util;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.08.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class TopSnowFeature implements IFeature
{
	private final Block snow;
	private final BlockState target;
	private final int heightStart, heightMax;

	public TopSnowFeature(BlockState target, Block snow, int heightStart, int heightMax)
	{
		this.target = target;
		this.snow = snow;
		this.heightStart = heightStart;
		this.heightMax = heightMax;
	}

	@Override
	public void generate(World world, int x, int y, int z)
	{
		if (world.getBlock(x, y, z) == Block.air)
		{
			if (y >= heightStart && y < heightMax)
			{
				if (world.getRandom().nextDouble() <= smoothstep(heightStart, heightMax, y))
					world.setState(snow.getDefaultState().with(SnowLayerBlock.SNOW_LAYER, world.getRandom().nextInt(4) + 1).get(), x, y, z);
			} else if (y >= heightMax)
			{
				world.setState(snow.getDefaultState().with(SnowLayerBlock.SNOW_LAYER, world.getRandom().nextInt(4) + 1).get(), x, y, z);
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
		return world.getState(x, y - 1, z) == target;
	}

	@Override
	public EnumPlacement getPlacement()
	{
		return EnumPlacement.ON_HEIGHT_MAP;
	}
}
