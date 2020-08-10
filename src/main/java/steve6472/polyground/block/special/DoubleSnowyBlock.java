package steve6472.polyground.block.special;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.enums.EnumHalfSnowy;
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
public class DoubleSnowyBlock extends CustomBlock
{
	public static final EnumProperty<EnumHalfSnowy> HALF = States.HALF_SNOWY;

	public DoubleSnowyBlock(File f)
	{
		super(f);
		setDefaultState(getDefaultState().with(HALF, EnumHalfSnowy.BOTTOM).get());
	}

	@Override
	public BlockState getStateForPlacement(World world, BlockState state, int x, int y, int z)
	{
		return super.getStateForPlacement(world, state, x, y, z);
	}

	@Override
	public void onUpdate(World world, BlockState state, EnumFace updateFrom, int x, int y, int z)
	{
		super.onUpdate(world, state, updateFrom, x, y, z);

		if (state.get(HALF) == EnumHalfSnowy.TOP)
		{
			BlockState stateUnder = world.getState(x, y - 1, z);
			if (stateUnder.getBlock() != this || stateUnder.get(HALF) != EnumHalfSnowy.BOTTOM)
			{
				SnapBlock.activate(state, world, x, y, z);
				world.setBlock(Block.air, x, y, z);
			}
		}
	}

	@Override
	public void onPlace(World world, BlockState state, Player player, EnumFace placedOn, int x, int y, int z)
	{
		super.onPlace(world, state, player, placedOn, x, y, z);

		// Not enough space
		if (world.getBlock(x, y + 1, z) != Block.air)
			world.setBlock(Block.air, x, y, z);
		else
		{
			world.setState(getDefaultState().with(HALF, EnumHalfSnowy.TOP).get(), x, y + 1, z);
		}
	}

	@Override
	public void onBreak(World world, BlockState state, Player player, EnumFace breakedFrom, int x, int y, int z)
	{
		super.onBreak(world, state, player, breakedFrom, x, y, z);

		int Y = (state.get(HALF) == EnumHalfSnowy.BOTTOM ? 1 : -1);
		SnapBlock.activate(world.getState(x, y + Y, z), world, x, y + Y, z);
		world.setBlock(Block.air, x, y + Y, z);
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(HALF);
	}
}
