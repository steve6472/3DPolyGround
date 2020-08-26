package steve6472.polyground.block.special;

import steve6472.SSS;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.Tags;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.World;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12.08.2020
 * Project: CaveGame
 *
 ***********************/
public class SpreadBlock extends Block
{
	private final File file;
	/**
	 * Turn what
	 */
	private BlockState target;
	/**
	 * Turn into
	 */
	private BlockState targetSet;

	public SpreadBlock(File f)
	{
		super(f);
		this.file = f;
	}

	@Override
	public boolean randomTickable()
	{
		return true;
	}

	@Override
	public void postLoad()
	{
		if (file.isFile())
		{
			SSS sss = new SSS(file);
			if (sss.containsName("target_state"))
			{
				target = Blocks.getStateByName(sss.getString("target"), sss.getString("target_state"));
			} else
			{
				target = Blocks.getDefaultState(sss.getString("target"));
			}
			if (sss.containsName("target_set_state"))
			{
				targetSet = Blocks.getStateByName(sss.getString("target_set"), sss.getString("target_set_state"));
			} else
			{
				targetSet = Blocks.getDefaultState(sss.getString("target_set"));
			}
		}
	}

	@Override
	public void randomTick(BlockState state, World world, int x, int y, int z)
	{
		// Die
		BlockState up = world.getState(x, y + 1, z);

		if (shouldDie(up))
		{
			world.setState(target, x, y, z);
			return;
		}

		int i = world.getRandom().nextInt(5) - 2;
		int j = world.getRandom().nextInt(6) - 2;
		int k = world.getRandom().nextInt(5) - 2;

		BlockState stateAtPos = world.getState(x + i, y + j, z + k);
		if (stateAtPos == target)
		{
			up = world.getState(x + i, y + j + 1, z + k);
			if (!shouldDie(up))
			{
				world.setState(targetSet, x + i, y + j, z + k);
			}
		}
	}

	private boolean shouldDie(BlockState up)
	{
		return up.hasTag(Tags.KILL_SPREAD_BOTTOM) || (up.getBlock().isFull && !up.hasTag(Tags.TRANSPARENT));
	}
}
