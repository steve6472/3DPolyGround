package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.properties.enums.EnumAxis;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.EntityBase;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.09.2020
 * Project: CaveGame
 *
 ***********************/
public class ConveyorBeltBlock extends DirectionalBlock
{
	public ConveyorBeltBlock(JSONObject json)
	{
		super(json);
		isFull = false;
	}

	@Override
	public BlockState getStateForPlacement(World world, Player player, EnumFace placedOn, int x, int y, int z)
	{
		if (placedOn == null)
			return getDefaultState();

		if (placedOn.isSide())
			return getDefaultState().with(FACING, placedOn.getOpposite()).get();

		//TODO: Change to radians
		double yaw = Math.toDegrees(player.getCamera().getYaw());
		if (yaw < 45 || yaw > 315)
			return getDefaultState().with(FACING, EnumFace.WEST).get();
		else if (yaw < 315 && yaw > 225)
			return getDefaultState().with(FACING, EnumFace.NORTH).get();
		else if (yaw < 225 && yaw > 135)
			return getDefaultState().with(FACING, EnumFace.EAST).get();
		else
			return getDefaultState().with(FACING, EnumFace.SOUTH).get();
	}

	@Override
	public void entityOnBlockCollision(EntityBase entity, World world, BlockState state, int x, int y, int z)
	{
		EnumFace f = state.get(FACING);
		entity.addPosition(f.getXOffset() / 60f, 0, f.getZOffset() / 60f);

		if (f.getAxis() == EnumAxis.X) entity.setZ(z + 0.5f);
		if (f.getAxis() == EnumAxis.Z) entity.setX(x + 0.5f);
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		return CustomBlock.model(x, y, z, world, state, buildHelper, modelLayer);
	}
}
