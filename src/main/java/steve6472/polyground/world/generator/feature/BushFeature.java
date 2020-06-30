package steve6472.polyground.world.generator.feature;

import steve6472.polyground.block.Block;
import steve6472.polyground.world.chunk.SubChunk;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.05.2020
 * Project: CaveGame
 *
 ***********************/
public class BushFeature implements IFeature
{
	private final Block blockUnder, log, leaves;

	public BushFeature(Block blockUnder, Block log, Block leaves)
	{
		this.blockUnder = blockUnder;
		this.log = log;
		this.leaves = leaves;
	}

	@Override
	public void generate(SubChunk sc, int x, int y, int z)
	{
		sc.setBlockEfficiently(x, y + 1, z, log);
		sc.setBlockEfficiently(x, y + 2, z, log);
		sc.setBlockEfficiently(x, y + 3, z, leaves);
		sc.setBlockEfficiently(x + 1, y + 2, z, leaves);
		sc.setBlockEfficiently(x - 1, y + 2, z, leaves);
		sc.setBlockEfficiently(x, y + 2, z + 1, leaves);
		sc.setBlockEfficiently(x, y + 2, z - 1, leaves);
	}

	@Override
	public int size()
	{
		return 1;
	}

	@Override
	public boolean canGenerate(SubChunk sc, int x, int y, int z)
	{
		return sc.getBlockId(x, y, z) == blockUnder.getId();
	}

	@Override
	public EnumFeaturePlacement getPlacement()
	{
		return EnumFeaturePlacement.IN_HEIGHT_MAP;
	}
}
