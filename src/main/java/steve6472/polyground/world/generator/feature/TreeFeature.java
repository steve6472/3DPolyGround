package steve6472.polyground.world.generator.feature;

import steve6472.polyground.block.Block;
import steve6472.polyground.world.chunk.SubChunk;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.05.2020
 * Project: CaveGame
 *
 ***********************/
public class TreeFeature implements IFeature
{
	private final Block blockUnderMatch, log, leaves, blockUnder;

	public TreeFeature(Block blockUnderMatch, Block log, Block leaves, Block blockUnder)
	{
		this.blockUnderMatch = blockUnderMatch;
		this.log = log;
		this.leaves = leaves;
		this.blockUnder = blockUnder;
	}

	@Override
	public void generate(SubChunk sc, int x, int y, int z)
	{
		int h = sc.getWorld().getRandom().nextInt(2) + 5;

		for (int i = 0; i < h; i++)
		{
			sc.setBlock(log, x, y + i + 1, z);
		}
		sc.setBlock(blockUnder, x, y, z);
		for (int i = -2; i < 3; i++)
		{
			for (int j = -2; j < 3; j++)
			{
				for (int k = 0; k < 2; k++)
				{
					if (Math.abs(i) % 3 != 2 || Math.abs(j) % 3 != 2)
					{
						if (sc.getBlock(x + i, y + h - 2 + k, z + j) == Block.air)
							sc.setBlock(leaves, x + i, y + h - 2 + k, z + j);
					}
				}
			}
		}
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				for (int k = 0; k < 2; k++)
				{
					if (Math.abs(i) % 2 != 1 || Math.abs(j) % 2 != 1)
					{
						if (sc.getBlock(x + i, y + h + k, z + j) == Block.air)
							sc.setBlock(leaves, x + i, y + h + k, z + j);
					}
				}
			}
		}
	}

	@Override
	public int size()
	{
		return 1;
	}

	@Override
	public boolean canGenerate(SubChunk sc, int x, int y, int z)
	{
		if (sc.getBlock(x, y, z) != blockUnderMatch)
			return false;
		for (int i = -2; i < 3; i++)
		{
			for (int j = -2; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					if (Math.abs(i) % 3 != 2 || Math.abs(j) % 3 != 2)
					{
						if (sc.getBlock(x + i, y + 5 - 2 + k, z + j) != Block.air)
							return false;
					}
				}
			}
		}
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					if (Math.abs(i) % 2 != 1 || Math.abs(j) % 2 != 1)
					{
						if (sc.getBlock(x + i, y + 5 + k, z + j) != Block.air)
							return false;
					}
				}
			}
		}

		return true;
	}

	@Override
	public EnumFeaturePlacement getPlacement()
	{
		return EnumFeaturePlacement.IN_HEIGHT_MAP;
	}
}