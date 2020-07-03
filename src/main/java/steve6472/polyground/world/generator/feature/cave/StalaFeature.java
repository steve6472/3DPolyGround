package steve6472.polyground.world.generator.feature.cave;

import steve6472.polyground.block.Block;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.polyground.world.generator.feature.EnumFeaturePlacement;
import steve6472.polyground.world.generator.feature.IFeature;
import steve6472.sge.main.util.RandomUtil;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.06.2020
 * Project: CaveGame
 *
 ***********************/
public class StalaFeature implements IFeature
{
	private static final int[][] SIZES_ALTS =
		{
			{0, 1},
			{1, 1},
			{2, 1},
			{3, 2},
			{5, 2},
			{7, 2},
			{9, 1},
		};

	private static final int[][] SIZES =
		{
			{0},
			{2, 0},
			{4, 2, 0},
			{5, 4, 3, 1},
			{5, 4, 2, 0},
			{5, 4, 3, 2, 1},
			{5, 4, 3, 2, 1},
			{6, 5, 4, 3, 2, 1},
			{6, 5, 4, 2, 1, 0},
			{6, 5, 4, 3, 2, 1, 0},
		};

	/**
	 * {a, b}
	 * a = index
	 * b = size of array
	 */
	private static final int[][] SIZES_BOTH_ALTS = {
		{0, 2},
		{2, 3},
		{5, 3},
		{8, 4},
		{12, 5},
		{17, 4},
		{21, 4}};

	private static final int [][] SIZES_BOTH =
		{
			{0},
			{1},
			{0, 0},
			{0, 1},
			{1, 0},
			{2, 0, 2},
			{1, 0, 2},
			{2, 0, 1},
			{4, 2, 0, 1},
			{1, 0, 3, 4},
			{3, 2, 1, 1},
			{1, 0, 2, 4},
			{5, 3, 2, 0, 1},
			{5, 4, 2, 0, 1},
			{4, 2, 0, 2, 4},
			{3, 2, 0, 1, 2},
			{4, 3, 1, 2, 5},
			{5, 4, 2, 0, 1, 3},
			{5, 3, 2, 1, 1, 2},
			{3, 1, 2, 3, 4, 5},
			{3, 2, 1, 0, 2, 4},
			{6, 4, 2, 0, 2, 4, 6},
			{5, 4, 2, 1, 0, 1, 2},
			{3, 1, 0, 1, 2, 3, 4},
			{3, 2, 1, 0, 2, 4, 6}
		};

	private final Block baseBlock;
	private final Block[] stalas;
	private final float doubleChance;

	public StalaFeature(Block baseBlock, float doubleChance)
	{
		this.baseBlock = baseBlock;
		this.doubleChance = doubleChance;
		stalas = new Block[7];
		for (int i = 0; i < 7; i++)
		{
			stalas[i] = BlockRegistry.getBlockByName("stala_" + ((i + 1) * 2));
		}
	}

	@Override
	public void generate(SubChunk sc, int x, int y, int z)
	{
		gen(sc, x, y, z, sc.getBlock(x, y + 1, z) == Block.air);
	}

	private void gen(SubChunk sc, int x, int y, int z, boolean stalagmite)
	{
		boolean canBeBoth = false;
		int max = -1;
		for (int i = 0; i <= 6; i++)
		{
			if (sc.getBlock(x, y(y, i, stalagmite), z) == baseBlock)
			{
				max = i;
				if (sc.getWorld().getRandom().nextFloat() <= doubleChance)
				{
					canBeBoth = true;
				}
				else
				{
					if (max >= 3 && !RandomUtil.decide(30))
						max -= 2;
				}
				break;
			}
		}

		// Might be in air, select random height
		if (max == -1)
		{
			max = RandomUtil.randomInt(1, 6);
		}

		if (canBeBoth)
		{
			int type = RandomUtil.randomInt(0, SIZES_BOTH_ALTS[max][1] - 1);
			int start = SIZES_BOTH_ALTS[max][0];
			for (int i = 0; i < max; i++)
			{
				sc.setBlock(stalas[SIZES_BOTH[start + type][i]], x, y(y, i, stalagmite), z);
			}
		} else
		{
			int type = RandomUtil.randomInt(0, SIZES_ALTS[max][1] - 1);
			int start = SIZES_ALTS[max][0];
			for (int i = 0; i <= max; i++)
			{
				sc.setBlock(stalas[SIZES[start + type][i]], x, y(y, i, stalagmite), z);
			}
		}
	}

	private int y(int y, int i, boolean stalagmite)
	{
		return stalagmite ? y + 1 + i : y - 1 - i;
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
	public boolean canGenerate(SubChunk sc, int x, int y, int z)
	{
		return sc.getBlock(x, y, z) == baseBlock && (sc.getBlock(x, y - 1, z) == Block.air || sc.getBlock(x, y + 1, z) == Block.air);
	}

	@Override
	public EnumFeaturePlacement getPlacement()
	{
		return EnumFeaturePlacement.IN_GROUND;
	}
}
