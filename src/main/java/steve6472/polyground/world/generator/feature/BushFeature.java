package steve6472.polyground.world.generator.feature;

import steve6472.polyground.block.Block;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.05.2020
 * Project: CaveGame
 *
 ***********************/
public class BushFeature implements IFeature
{
	private final Block blockUnder, log, leaves;
	private final boolean undergroundBush;

	public BushFeature(Block blockUnder, Block log, Block leaves)
	{
		this.blockUnder = blockUnder;
		this.log = log;
		this.leaves = leaves;
		this.undergroundBush = false;
	}

	public BushFeature(Block blockUnder, Block log, Block leaves, boolean undergroundBush)
	{
		this.blockUnder = blockUnder;
		this.log = log;
		this.leaves = leaves;
		this.undergroundBush = undergroundBush;
	}

	@Override
	public void generate(World world, int x, int y, int z)
	{
		world.setBlock(log, x, y + 1, z);
		world.setBlock(log, x, y + 2, z);
		world.setBlock(leaves, x, y + 3, z);
		world.setBlock(leaves, x + 1, y + 2, z, (b) -> b == Block.air);
		world.setBlock(leaves, x - 1, y + 2, z, (b) -> b == Block.air);
		world.setBlock(leaves, x, y + 2, z + 1, (b) -> b == Block.air);
		world.setBlock(leaves, x, y + 2, z - 1, (b) -> b == Block.air);
	}

	@Override
	public int size()
	{
		return undergroundBush ? 0 : 1;
	}

	@Override
	public boolean canGenerate(World world, int x, int y, int z)
	{
		if (undergroundBush)
		{
			// Don't generate on border!
			if (Math.abs(x) % 16 == 0 || Math.abs(x) % 16 == 15 || Math.abs(z) % 16 == 0 || Math.abs(z) % 16 == 15)
				return false;
			return world.getBlock(x, y, z) == blockUnder && world.getBlock(x, y + 1, z) == Block.air && world.getBlock(x, y + 2, z) == Block.air && world.getBlock(x, y + 3, z) == Block.air;
		} else
		{
			return world.getBlock(x, y, z) == blockUnder;
		}
	}

	@Override
	public EnumFeaturePlacement getPlacement()
	{
		return undergroundBush ? EnumFeaturePlacement.IN_GROUND : EnumFeaturePlacement.IN_HEIGHT_MAP;
	}

	@Override
	public String toString()
	{
		return "BushFeature{" + "blockUnder=" + blockUnder + ", log=" + log + ", leaves=" + leaves + ", generateOnBorder=" + undergroundBush + '}';
	}
}
