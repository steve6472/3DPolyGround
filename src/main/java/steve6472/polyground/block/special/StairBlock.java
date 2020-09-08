package steve6472.polyground.block.special;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.World;

import java.io.File;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 21.09.2019
 * Project: SJP
 *
 ***********************/
public class StairBlock extends Block
{
	public static final EnumProperty<EnumFace> FACING = States.FACING_BLOCK;

	public StairBlock(File f)
	{
		super(f);
		isFull = false;
		setDefaultState(getDefaultState().with(FACING, EnumFace.NORTH).get());
	}

	@Override
	public BlockState getStateForPlacement(World world, Player player, EnumFace placedOn, int x, int y, int z)
	{
		if (player == null)
			return getDefaultState();

		//TODO: Change to radians
		double yaw = Math.toDegrees(player.getCamera().getYaw());
		if (yaw < 45 || yaw > 315)
			return getDefaultState().with(FACING, EnumFace.EAST).get();
		else if (yaw < 315 && yaw > 225)
			return getDefaultState().with(FACING, EnumFace.SOUTH).get();
		else if (yaw < 225 && yaw > 135)
			return getDefaultState().with(FACING, EnumFace.WEST).get();
		else
			return getDefaultState().with(FACING, EnumFace.NORTH).get();
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(FACING);
	}
}
