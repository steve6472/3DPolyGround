package steve6472.polyground.block.special;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.Tags;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.enums.EnumHalf;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.Player;
import steve6472.polyground.world.World;

import java.io.File;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 10.08.2020
 * Project: CaveGame
 *
 ***********************/
public class DoubleBlock extends CustomBlock
{
	public static final EnumProperty<EnumHalf> HALF = States.HALF;

	public DoubleBlock(File f)
	{
		super(f);
		setDefaultState(getDefaultState().with(HALF, EnumHalf.BOTTOM).get());
	}

	@Override
	public void neighbourChange(BlockState state, World world, EnumFace updateFrom, int x, int y, int z)
	{
		boolean isValid = isValidPosition(state, world, x, y, z);
		if (!isValid)
		{
			if (state.get(HALF) == EnumHalf.BOTTOM)
				spawnLoot(state, world, x, y, z);
			world.setBlock(Block.air, x, y, z);
		}
	}

	@Override
	public boolean isValidPosition(BlockState state, World world, int x, int y, int z)
	{
		if (state.get(HALF) == EnumHalf.BOTTOM)
		{
			BlockState up = world.getState(x, y + 1, z);
			BlockState down = world.getState(x, y - 1, z);
//			return super.isValidPosition(state, world, x, y, z) && (down.getBlock() != Block.air) && (up.getBlock() == this || up.getBlock() == Block.air);
			return super.isValidPosition(state, world, x, y, z) && (down.hasTag(Tags.FLOWER_TOP)) && (up.getBlock() == this || up.getBlock() == Block.air);
		} else
		{
			BlockState stateUnder = world.getState(x, y - 1, z);
			return stateUnder.getBlock() == this && stateUnder.get(HALF) == EnumHalf.BOTTOM;
		}
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockState oldState, int x, int y, int z)
	{
		super.onBlockAdded(state, world, oldState, x, y, z);

		if (state.get(HALF) == EnumHalf.BOTTOM)
			world.setState(getDefaultState().with(HALF, EnumHalf.TOP).get(), x, y + 1, z);
	}

	//TODO: Check if block below/above is this block
	@Override
	public void onPlayerBreak(BlockState state, World world, Player player, EnumFace breakedFrom, int x, int y, int z)
	{
		super.onPlayerBreak(state, world, player, breakedFrom, x, y, z);

		int Y = (state.get(HALF) == EnumHalf.BOTTOM ? 1 : -1);
		world.setBlock(Block.air, x, y + Y, z);
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(HALF);
	}
}
