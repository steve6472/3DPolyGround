package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.Tags;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12.08.2020
 * Project: CaveGame
 *
 ***********************/
public class SpreadBlock extends Block
{
	/**
	 * Turn what
	 */
	private BlockState target;
	/**
	 * Turn into
	 */
	private BlockState targetSet;

	public SpreadBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public boolean randomTickable()
	{
		return true;
	}

	@Override
	public void load(JSONObject json)
	{
		if (json.has("target_state"))
		{
			target = Blocks.getStateByName(json.getString("target"), json.getString("target_state"));
		} else
		{
			target = Blocks.getDefaultState(json.getString("target"));
		}
		if (json.has("target_set_state"))
		{
			targetSet = Blocks.getStateByName(json.getString("target_set"), json.getString("target_set_state"));
		} else
		{
			targetSet = Blocks.getDefaultState(json.getString("target_set"));
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
