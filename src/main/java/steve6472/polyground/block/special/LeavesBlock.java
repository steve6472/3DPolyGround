package steve6472.polyground.block.special;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.Tags;
import steve6472.polyground.block.properties.BooleanProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.IntProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.Player;
import steve6472.polyground.world.World;

import java.io.File;
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
	public static IntProperty DISTANCE = States.DISTANCE;

	public LeavesBlock(File f)
	{
		super(f);
		isFull = false;
		setDefaultState(getDefaultState().with(PERSISTENT, false).with(DISTANCE, 1).get());
	}

	@Override
	public BlockState getStateForPlacement(World world, Player player, EnumFace placedOn, int x, int y, int z)
	{
		return getDefaultState().with(PERSISTENT, true).with(DISTANCE, updateDistance(getDefaultState(), world, x, y, z).get(DISTANCE)).get();
	}

	@Override
	public void update(BlockState state, World world, EnumFace updateFrom, int x, int y, int z)
	{
		int distance;
		BlockState newState = state.with(PERSISTENT, state.get(PERSISTENT)).with(DISTANCE, distance = updateDistance(state, world, x, y, z).get(DISTANCE)).get();

		if (state.get(DISTANCE) != distance)
		{
			world.setState(newState, x, y, z);
		}
	}

	private BlockState updateDistance(BlockState state, World world, int x, int y, int z)
	{
		int m = 7;

		for (EnumFace f : EnumFace.getFaces())
		{
			m = Math.min(m, getDistance(world.getState(x + f.getXOffset(), y + f.getYOffset(), z + f.getZOffset())));
			if (m == 1)
				break;
		}

		return state.with(PERSISTENT, false).with(DISTANCE, m).get();
	}

	private static int getDistance(BlockState state)
	{
		if (state.hasTag(Tags.LOG))
			return 1;
		else
			return state.getBlock() instanceof LeavesBlock ? state.get(DISTANCE) + 1 : 8;
	}

	@Override
	public void tick(BlockState state, World world, int x, int y, int z)
	{
		if (!state.get(PERSISTENT) && state.get(DISTANCE) == 7 && world.getRandom().nextDouble() <= 0.1 / 120d)
		{
			SnapBlock.activate(state, world, x, y, z);
			world.setBlock(Block.air, x, y, z);
		}
	}

	@Override
	public boolean isTickable()
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
