package steve6472.polyground.world.generator.feature.cave;

import steve6472.polyground.block.Block;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.polyground.world.generator.feature.EnumFeaturePlacement;
import steve6472.polyground.world.generator.feature.IFeature;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.06.2020
 * Project: CaveGame
 *
 ***********************/
public class CrystalFeature implements IFeature
{
	private final Block crystal;

	public CrystalFeature(Block crystal)
	{
		this.crystal = crystal;
	}

	@Override
	public void generate(SubChunk sc, int x, int y, int z)
	{
		if (sc.getBlockEfficiently(x, y - 1, z) == Block.air)
			sc.setBlockEfficiently(x, y - 1, z, crystal);
		else
			sc.setBlockEfficiently(x, y + 1, z, crystal);
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
		return 1;
	}

	@Override
	public boolean canGenerate(SubChunk sc, int x, int y, int z)
	{
		return sc.getBlock(x, y, z) != Block.air && (sc.getBlockEfficiently(x, y - 1, z) == Block.air || sc.getBlockEfficiently(x, y + 1, z) == Block.air);
	}

	@Override
	public EnumFeaturePlacement getPlacement()
	{
		return EnumFeaturePlacement.IN_GROUND;
	}
}
