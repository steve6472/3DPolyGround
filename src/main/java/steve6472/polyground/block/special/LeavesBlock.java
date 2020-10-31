package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.Tags;
import steve6472.polyground.block.properties.BooleanProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.IntProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.World;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.08.2019
 * Project: SJP
 *
 ***********************/
public class LeavesBlock extends Block
{
	public static BooleanProperty PERSISTENT = States.PERSISTENT;
	public static IntProperty DISTANCE = States.DISTANCE_1_7;

	public LeavesBlock(JSONObject json)
	{
		super(json);
		isFull = false;
		setDefaultState(getDefaultState().with(PERSISTENT, false).with(DISTANCE, 1).get());
	}

	@Override
	public BlockState getStateForPlacement(World world, Player player, EnumFace placedOn, int x, int y, int z)
	{
		return getDefaultState().with(PERSISTENT, true).with(DISTANCE, updateDistance(getDefaultState(), world, x, y, z).get(DISTANCE)).get();
	}

	@Override
	public void neighbourChange(BlockState state, World world, EnumFace updateFrom, int x, int y, int z)
	{
		BlockState facingState = world.getState(x + updateFrom.getXOffset(), y + updateFrom.getYOffset(), z + updateFrom.getZOffset());
		int distance = Math.min(getDistance(facingState) + 1, 7);
		if (distance != 1 || state.get(DISTANCE) != distance)
		{
			world.scheduleUpdate(state, updateFrom, x, y, z, 3);
		}
	}

	@Override
	public void scheduledUpdate(BlockState state, World world, EnumFace updateFrom, int x, int y, int z)
	{
		world.setState(updateDistance(state, world, x, y, z), x, y, z);
	}

	private BlockState updateDistance(BlockState state, World world, int x, int y, int z)
	{
		int m = 7;

		for (EnumFace f : EnumFace.getFaces())
		{
			m = Math.min(m, getDistance(world.getState(x + f.getXOffset(), y + f.getYOffset(), z + f.getZOffset())) + 1);
			if (m == 1)
				break;
		}

		return state.with(PERSISTENT, state.get(PERSISTENT)).with(DISTANCE, m).get();
	}

	private static int getDistance(BlockState state)
	{
		if (state.hasTag(Tags.LOG) || state.getBlock() instanceof BranchBlock)
			return 0;
		else
			return state.getBlock() instanceof LeavesBlock ? state.get(DISTANCE): 7;
	}

	@Override
	public void randomTick(BlockState state, World world, int x, int y, int z)
	{
		if (!state.get(PERSISTENT) && state.get(DISTANCE) == 7)
		{
			world.setBlock(Block.AIR, x, y, z);
		}
	}

	@Override
	public boolean randomTickable()
	{
		return true;
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(PERSISTENT);
		properties.add(DISTANCE);
	}
}
