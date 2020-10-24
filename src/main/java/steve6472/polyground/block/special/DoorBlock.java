package steve6472.polyground.block.special;

import org.joml.AABBf;
import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.ISpecialRender;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.DoorData;
import steve6472.polyground.block.blockdata.IBlockData;
import steve6472.polyground.block.model.CubeHitbox;
import steve6472.polyground.block.properties.BooleanProperty;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.enums.EnumHalf;
import steve6472.polyground.block.properties.enums.EnumLR;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 24.10.2020
 * Project: CaveGame
 *
 ***********************/
public class DoorBlock extends Block implements IBlockData, ISpecialRender
{
	public static final EnumProperty<EnumHalf> HALF = States.HALF;
	public static final BooleanProperty OPEN = States.OPEN;
	public static final EnumProperty<EnumFace> FACING = States.FACING;
	public static final EnumProperty<EnumLR> HINGE = States.HINGE;

	private static final CubeHitbox[] NORTH_BOTTOM = new CubeHitbox[] {new CubeHitbox(new AABBf(13, 0, 0, 16, 32, 16)).div(16)};
	private static final CubeHitbox[] EAST_BOTTOM = new CubeHitbox[] {new CubeHitbox(new AABBf(0, 0, 13, 16, 32, 16)).div(16)};
	private static final CubeHitbox[] SOUTH_BOTTOM = new CubeHitbox[] {new CubeHitbox(new AABBf(0, 0, 0, 3, 32, 16)).div(16)};
	private static final CubeHitbox[] WEST_BOTTOM = new CubeHitbox[] {new CubeHitbox(new AABBf(0, 0, 0, 16, 32, 3)).div(16)};

	private static final CubeHitbox[] NORTH_TOP = new CubeHitbox[] {new CubeHitbox(new AABBf(13, -16, 0, 16, 16, 16)).div(16)};
	private static final CubeHitbox[] EAST_TOP = new CubeHitbox[] {new CubeHitbox(new AABBf(0, -16, 13, 16, 16, 16)).div(16)};
	private static final CubeHitbox[] SOUTH_TOP = new CubeHitbox[] {new CubeHitbox(new AABBf(0, -16, 0, 3, 16, 16)).div(16)};
	private static final CubeHitbox[] WEST_TOP = new CubeHitbox[] {new CubeHitbox(new AABBf(0, -16, 0, 16, 16, 3)).div(16)};

	private static final CubeHitbox[] NONE = new CubeHitbox[] {new CubeHitbox(new AABBf(0, 0, 0, 16, 16, 16)).div(16)};

	public DoorBlock(JSONObject json)
	{
		super(json);
		isFull = false;
	}

	@Override
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getAction() != KeyList.PRESS || click.getButton() != KeyList.RMB)
			return;

		player.processNextBlockPlace = false;

		EnumFace facing = state.get(FACING);
		EnumLR hinge = state.get(HINGE);
		boolean open = state.get(OPEN);

		EnumFace offset = EnumFace.NONE;

		switch (facing)
		{
			case NORTH -> offset = hinge == EnumLR.LEFT ? EnumFace.WEST : EnumFace.EAST;
			case EAST  -> offset = hinge == EnumLR.LEFT ? EnumFace.NORTH : EnumFace.SOUTH;
			case SOUTH -> offset = hinge == EnumLR.LEFT ? EnumFace.EAST : EnumFace.WEST;
			case WEST  -> offset = hinge == EnumLR.LEFT ? EnumFace.SOUTH : EnumFace.NORTH;
		}

		BlockState s = world.getState(x + offset.getXOffset(), y, z + offset.getZOffset());
		if (s.getBlock() instanceof DoorBlock)
		{
			EnumFace f = s.get(FACING);
			EnumLR h = s.get(HINGE);
			boolean o = s.get(OPEN);

			if (f == facing && o == open && h != hinge)
			{
				toggle(world, s, x + offset.getXOffset(), y, z + offset.getZOffset());
			}
		}

		toggle(world, state, x, y, z);
	}

	private void toggle(World world, BlockState state, int x, int y, int z)
	{
		EnumFace facing = state.get(FACING);
		EnumLR hinge = state.get(HINGE);
		boolean open = state.get(OPEN);
		int dataOffset = 0;

		if (state.get(HALF) == EnumHalf.TOP)
		{
			world.setState(getDefaultState().with(HALF, EnumHalf.BOTTOM).with(OPEN, !open).with(FACING, facing).with(HINGE, hinge), x, y - 1, z, 9);
			world.setState(getDefaultState().with(HALF, EnumHalf.TOP).with(OPEN, !open).with(FACING, facing).with(HINGE, hinge), x, y, z, 9);
			dataOffset = -1;
		} else
		{
			world.setState(getDefaultState().with(HALF, EnumHalf.BOTTOM).with(OPEN, !open).with(FACING, facing).with(HINGE, hinge), x, y, z, 9);
			world.setState(getDefaultState().with(HALF, EnumHalf.TOP).with(OPEN, !open).with(FACING, facing).with(HINGE, hinge), x, y + 1, z, 9);
		}

		DoorData data = ((DoorData) world.getData(x, y + dataOffset, z));

		if (open)
			data.close();
		else
			data.open();
	}

	@Override
	public CubeHitbox[] getHitbox(World world, BlockState state, int x, int y, int z)
	{
		EnumFace facing = state.get(FACING);
		boolean hingeOnRight = state.get(HINGE) == EnumLR.RIGHT;
		boolean isOpen = state.get(OPEN);

		return switch (state.get(HALF))
		{
			case TOP -> switch (facing)
				{
					case NORTH -> isOpen ? (hingeOnRight ? WEST_TOP : EAST_TOP) : NORTH_TOP;
					case EAST -> isOpen ? (hingeOnRight ? NORTH_TOP : SOUTH_TOP) : EAST_TOP;
					case SOUTH -> isOpen ? (hingeOnRight ? EAST_TOP : WEST_TOP) : SOUTH_TOP;
					case WEST -> isOpen ? (hingeOnRight ? SOUTH_TOP : NORTH_TOP) : WEST_TOP;
					default -> NONE;
				};
			case BOTTOM -> switch (facing)
				{
					case NORTH -> isOpen ? (hingeOnRight ? WEST_BOTTOM : EAST_BOTTOM) : NORTH_BOTTOM;
					case EAST -> isOpen ? (hingeOnRight ? NORTH_BOTTOM : SOUTH_BOTTOM) : EAST_BOTTOM;
					case SOUTH -> isOpen ? (hingeOnRight ? EAST_BOTTOM : WEST_BOTTOM) : SOUTH_BOTTOM;
					case WEST -> isOpen ? (hingeOnRight ? SOUTH_BOTTOM : NORTH_BOTTOM) : WEST_BOTTOM;
					default -> NONE;
				};
		};
	}


	@Override
	public void render(Stack stack, World world, BlockState state, int x, int y, int z)
	{
		if (state.get(HALF) == EnumHalf.TOP)
			return;

		DoorData data = ((DoorData) world.getData(x, y, z));
		data.render(stack, state);
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(HALF);
		properties.add(OPEN);
		properties.add(FACING);
		properties.add(HINGE);
	}

	@Override
	public boolean isValidPosition(BlockState state, World world, int x, int y, int z)
	{
		if (state.get(HALF) == EnumHalf.BOTTOM)
		{
			BlockState s = world.getState(x, y, z);
			BlockState up = world.getState(x, y + 1, z);
			return (s.getBlock().isReplaceable(s) || state == s) && (up.getBlock() == this || up.getBlock() == Block.air);
		} else
		{
			BlockState stateUnder = world.getState(x, y - 1, z);
			return stateUnder.getBlock() == this && stateUnder.get(HALF) == EnumHalf.BOTTOM;
		}
	}

	@Override
	public BlockState getStateForPlacement(World world, Player player, EnumFace placedOn, int x, int y, int z)
	{
		if (player == null)
			return getDefaultState();

		boolean zNeg = player.getHitResult().getPz() - Math.floor(player.getHitResult().getPz()) < 0.5;
		boolean xNeg = player.getHitResult().getPx() - Math.floor(player.getHitResult().getPx()) < 0.5;

		//TODO: Change to radians
		double yaw = Math.toDegrees(player.getCamera().getYaw());
		if (yaw < 45 || yaw > 315)
			return getDefaultState().with(FACING, EnumFace.EAST).with(OPEN, false).with(HALF, EnumHalf.BOTTOM).with(HINGE, !xNeg ? EnumLR.RIGHT : EnumLR.LEFT).get();
		else if (yaw < 315 && yaw > 225)
			return getDefaultState().with(FACING, EnumFace.SOUTH).with(OPEN, false).with(HALF, EnumHalf.BOTTOM).with(HINGE, !zNeg ? EnumLR.RIGHT : EnumLR.LEFT).get();
		else if (yaw < 225 && yaw > 135)
			return getDefaultState().with(FACING, EnumFace.WEST).with(OPEN, false).with(HALF, EnumHalf.BOTTOM).with(HINGE, xNeg ? EnumLR.RIGHT : EnumLR.LEFT).get();
		else
			return getDefaultState().with(FACING, EnumFace.NORTH).with(OPEN, false).with(HALF, EnumHalf.BOTTOM).with(HINGE, zNeg ? EnumLR.RIGHT : EnumLR.LEFT).get();
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockState oldState, int x, int y, int z)
	{
		super.onBlockAdded(state, world, oldState, x, y, z);

		if (state.get(HALF) == EnumHalf.BOTTOM)
			world.setState(getDefaultState().with(HALF, EnumHalf.TOP).with(OPEN, false).with(HINGE, state.get(HINGE)).with(FACING, state.get(FACING)), x, y + 1, z);
	}

	@Override
	public void onPlayerBreak(BlockState state, World world, Player player, EnumFace breakedFrom, int x, int y, int z)
	{
		super.onPlayerBreak(state, world, player, breakedFrom, x, y, z);

		int Y = (state.get(HALF) == EnumHalf.BOTTOM ? 1 : -1);
		world.setBlock(Block.air, x, y + Y, z);
	}

	@Override
	public BlockData createNewBlockEntity(BlockState state)
	{
		return state.get(HALF) == EnumHalf.BOTTOM ? new DoorData() : null;
	}
}
