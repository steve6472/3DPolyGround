package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.IntProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.World;
import steve6472.sge.main.util.Pair;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 04.09.2020
 * Project: CaveGame
 *
 ***********************/
public class LogStackBlock extends CustomBlock
{
	public static IntProperty LOGS = States.LOGS;

	public LogStackBlock(JSONObject json)
	{
		super(json);
		setDefaultState(getDefaultState().with(LOGS, 1).get());
	}

	@Override
	public Pair<BlockState, BlockState> getStateForPlacement(World world, BlockState heldState, Player player, EnumFace placedOn, int x, int y, int z)
	{
		int c = heldState.get(LOGS);
		if (c > 1)
			return new Pair<>(heldState.with(LOGS, c - 1).get(), heldState.with(LOGS, 1).get());
		else
			return super.getStateForPlacement(world, heldState, player, placedOn, x, y, z);
	}

	@Override
	public Pair<BlockState, BlockState> getStatesForPickup(World world, BlockState worldState, BlockState heldState, Player player, EnumFace clickedOn, int x, int y, int z)
	{
		if (heldState.isAir() || !(heldState.getBlock() instanceof LogStackBlock))
		{
			// When hands are empty pick on only one
			if (worldState.getBlock() instanceof LogStackBlock)
			{
				int c = worldState.get(LOGS);
				if (c > 1)
				{
					return new Pair<>(worldState.with(LOGS, 1).get(), worldState.with(LOGS, c - 1).get());
				} else
				{
					return new Pair<>(worldState, Block.air.getDefaultState());
				}
			}
			return new Pair<>(worldState, heldState);
		}

		// If hands are holding block of this type and is not full add one
		BlockState toPlace = Block.air.getDefaultState();
		if (heldState.get(LOGS) < 6)
		{
			if (worldState.get(LOGS) != 1)
			{
				toPlace = heldState.with(LOGS, worldState.get(LOGS) - 1).get();
			}
		} else
		{
			return null;
		}
		return new Pair<>(heldState.with(LOGS, heldState.get(LOGS) + 1).get(), toPlace);
	}

	@Override
	public boolean canMerge(BlockState heldState, BlockState worldState, Player player, EnumFace clickedOn, int x, int y, int z)
	{
		return worldState.getBlock() instanceof LogStackBlock && heldState.getBlock() instanceof LogStackBlock && worldState.get(LOGS) < 6;
	}

	@Override
	public Pair<BlockState, BlockState> merge(World world, BlockState worldState, BlockState heldState, Player player, EnumFace placedOn, int x, int y, int z)
	{
		int c = worldState.get(LOGS);
		int h = heldState.get(LOGS);

		if (h == 1)
		{
			return new Pair<>(Block.air.getDefaultState(), worldState.with(LOGS, c + 1).get());
		} else
		{
			return new Pair<>(heldState.with(LOGS, h - 1).get(), worldState.with(LOGS, c + 1).get());
		}
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(LOGS);
	}
}
