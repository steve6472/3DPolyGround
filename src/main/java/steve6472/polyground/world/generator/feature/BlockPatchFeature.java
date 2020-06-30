package steve6472.polyground.world.generator.feature;

import steve6472.polyground.block.Block;
import steve6472.polyground.world.chunk.SubChunk;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.05.2020
 * Project: CaveGame
 *
 ***********************/
public class BlockPatchFeature implements IFeature
{
	private final Block blockToReplace;
	private final Block blockToPlace;
	private final double chance;
	private final int radius;
	private final boolean decayFromCenter;

	public BlockPatchFeature(Block blockToReplace, Block blockToPlace, double chance, int radius, boolean decayFromCenter)
	{
		this.blockToReplace = blockToReplace;
		this.blockToPlace = blockToPlace;
		this.chance = chance;
		this.radius = radius;
		this.decayFromCenter = decayFromCenter;
	}

	@Override
	public void generate(SubChunk sc, int x, int y, int z)
	{
		sc.setBlock(x, y, z, blockToPlace);
	}

	@Override
	public int size()
	{
		return 1;
	}

	@Override
	public boolean canGenerate(SubChunk sc, int x, int y, int z)
	{
		return sc.getBlockId(x, y, z) == blockToReplace.getId();
	}

	@Override
	public EnumFeaturePlacement getPlacement()
	{
		return EnumFeaturePlacement.IN_HEIGHT_MAP;
	}
}
