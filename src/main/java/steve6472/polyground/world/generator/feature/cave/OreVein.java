package steve6472.polyground.world.generator.feature.cave;

import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.World;
import steve6472.polyground.world.generator.EnumPlacement;
import steve6472.polyground.world.generator.IFeature;
import steve6472.sge.main.Util;
import steve6472.sge.main.util.RandomUtil;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.08.2020
 * Project: CaveGame
 *
 ***********************/
public class OreVein implements IFeature
{
	private final BlockState target, ore;
	private final int minSize, maxSize;
	private final double chance;

	public OreVein(BlockState target, BlockState ore, int minSize, int maxSize, double chance)
	{
		this.target = target;
		this.ore = ore;
		this.minSize = Util.clamp(0, 30, minSize);
		this.maxSize = Util.clamp(1, 31, maxSize);
		this.chance = chance;
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

					if (X + Y + Z < 1 && world.getRandom().nextDouble() <= chance && world.getState(x + i, y + j, z + k) == target)
					{
						world.setState(ore, i + x, j + y, k + z);
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
		return world.getState(x, y, z) == target;
	}

	@Override
	public EnumPlacement getPlacement()
	{
		return EnumPlacement.IN_GROUND;
	}
}
