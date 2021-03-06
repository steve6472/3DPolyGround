package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.ISpecialRender;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.ConveyorBeltData;
import steve6472.polyground.block.blockdata.IBlockData;
import steve6472.polyground.block.properties.enums.EnumAxis;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.EntityBase;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.09.2020
 * Project: CaveGame
 *
 ***********************/
public class ConveyorBeltBlock extends DirectionalBlock implements IBlockData, ISpecialRender
{
	public ConveyorBeltBlock(JSONObject json)
	{
		super(json);
		isFull = false;
	}

	@Override
	public void render(Stack stack, World world, BlockState state, int x, int y, int z)
	{
		ConveyorBeltData data = (ConveyorBeltData) world.getData(x, y, z);
		if (data == null)
			return;

		switch (state.get(FACING))
		{
			case EAST -> stack.rotateY((float) (Math.PI * 1.5));
			case SOUTH -> stack.rotateY((float) (Math.PI));
			case WEST -> stack.rotateY((float) (Math.PI / 2f));
		}
		data.render(stack);
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
		entity.getMotion().add(f.getXOffset() / 700f, 0, f.getZOffset() / 700f);

		if (f.getAxis() == EnumAxis.X)
		{
			float d = z + 0.5f - entity.getZ();
			entity.getMotion().add(0, 0, d / 50f);
		}

		if (f.getAxis() == EnumAxis.Z)
		{
			float d = x + 0.5f - entity.getX();
			entity.getMotion().add(d / 50f, 0, 0);
		}
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		return CustomBlock.model(x, y, z, world, state, buildHelper, modelLayer);
	}

	@Override
	public BlockData createNewBlockEntity(BlockState state)
	{
		return new ConveyorBeltData();
	}
}
