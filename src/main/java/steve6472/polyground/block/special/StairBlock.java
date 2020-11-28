package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.enums.EnumHalf;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;

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
	public static final EnumProperty<EnumHalf> HALF = States.HALF;

	public StairBlock(JSONObject json)
	{
		super(json);
		isFull = false;
		setDefaultState(getDefaultState().with(FACING, EnumFace.NORTH).with(HALF, EnumHalf.BOTTOM));
	}

	@Override
	public BlockState getStateForPlacement(World world, Player player, EnumFace placedOn, int x, int y, int z)
	{
		if (player == null)
			return getDefaultState();

		double h = Double.parseDouble("0." + ("" + player.getHitResult().getPy()).split("\\.")[1]);
		EnumHalf half;
		if (placedOn == EnumFace.UP)
			half = EnumHalf.BOTTOM;
		else if (placedOn == EnumFace.DOWN)
			half = EnumHalf.TOP;
		else
			half = h > 0.5 ? EnumHalf.TOP : EnumHalf.BOTTOM;

		//TODO: Change to radians
		double yaw = Math.toDegrees(player.getCamera().getYaw());
		if (yaw < 45 || yaw > 315)
			return getDefaultState().with(FACING, EnumFace.EAST).with(HALF, half).get();
		else if (yaw < 315 && yaw > 225)
			return getDefaultState().with(FACING, EnumFace.SOUTH).with(HALF, half).get();
		else if (yaw < 225 && yaw > 135)
			return getDefaultState().with(FACING, EnumFace.WEST).with(HALF, half).get();
		else
			return getDefaultState().with(FACING, EnumFace.NORTH).with(HALF, half).get();
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		return CustomBlock.model(x, y, z, world, state, buildHelper, modelLayer);
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(FACING);
		properties.add(HALF);
	}
}
