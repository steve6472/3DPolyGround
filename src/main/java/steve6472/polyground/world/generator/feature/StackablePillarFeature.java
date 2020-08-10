package steve6472.polyground.world.generator.feature;


import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.World;
import steve6472.polyground.world.generator.EnumPlacement;
import steve6472.polyground.world.generator.IFeature;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.08.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class StackablePillarFeature implements IFeature
{
	private final BlockState blockUnder;
	private final BlockState blockToPlace;
	private final double chanceForNextTile;
	private final int maxHeight;

	public StackablePillarFeature(BlockState blockUnder, BlockState blockToPlace, double chanceForNextTile, int maxHeight)
	{
		this.blockUnder = blockUnder;
		this.blockToPlace = blockToPlace;
		this.chanceForNextTile = chanceForNextTile;
		this.maxHeight = maxHeight;
	}

	@Override
	public void generate(World world, int x, int y, int z)
	{
		world.setState(blockToPlace, x, y + 1, z);

		for (int i = y + 1; i < y + maxHeight; i++)
		{
			if (world.getRandom().nextDouble() <= chanceForNextTile)
			{
				world.setState(blockToPlace, x, i, z);
			} else
			{
				return;
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
		return 0;
	}

	@Override
	public boolean canGenerate(World world, int x, int y, int z)
	{
		return world.getState(x, y - 1, z) == blockUnder;
	}

	@Override
	public EnumPlacement getPlacement()
	{
		return EnumPlacement.ON_HEIGHT_MAP;
	}
}
