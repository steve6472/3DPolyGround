package steve6472.polyground.block.special;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.World;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 10.08.2020
 * Project: CaveGame
 *
 ***********************/
public class FlowerBlock extends CustomBlock
{
	private final String tag;

	public FlowerBlock(File f, String tag)
	{
		super(f);
		this.tag = tag;
	}

	@Override
	public void neighbourChange(BlockState state, World world, EnumFace updateFrom, int x, int y, int z)
	{
		if (!isValidPosition(state, world, x, y, z))
		{
			SnapBlock.activate(state, world, x, y, z, 1);
			world.setBlock(Block.air, x, y, z);
		}
	}

	@Override
	public boolean isValidPosition(BlockState state, World world, int x, int y, int z)
	{
		return super.isValidPosition(state, world, x, y, z) && world.getState(x, y - 1, z).hasTag(tag);
	}
}
