package steve6472.polyground.block.special;

import steve6472.SSS;
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
public class TagBelowBlock extends CustomBlock
{
	private String[] tags;
	private File f;
	private boolean and = false;

	public TagBelowBlock(File f)
	{
		super(f);
		this.f = f;
	}

	@Override
	public void postLoad()
	{
		SSS sss = new SSS(f);
		tags = sss.getStringArray("tags");
		and = sss.getString("operation").equals("and");
		f = null;
	}

	@Override
	public void neighbourChange(BlockState state, World world, EnumFace updateFrom, int x, int y, int z)
	{
		if (!isValidPosition(state, world, x, y, z))
		{
			spawnLoot(state, world, x, y, z);
			world.setBlock(Block.air, x, y, z);
		}
	}

	@Override
	public boolean isValidPosition(BlockState state, World world, int x, int y, int z)
	{
		BlockState st = world.getState(x, y - 1, z);
		if (and)
		{
			for (String s : tags)
			{
				if (!st.hasTag(s))
					return false;
			}
			return super.isValidPosition(state, world, x, y, z);
		} else
		{
			for (String s : tags)
			{
				if (st.hasTag(s))
					return super.isValidPosition(state, world, x, y, z);
			}
			return false;
		}
	}
}
