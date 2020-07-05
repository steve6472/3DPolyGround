package steve6472.polyground.block.special;

import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.HitResult;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.enums.EnumAxis;
import steve6472.polyground.block.properties.enums.EnumSlabType;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.Player;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.09.2019
 * Project: SJP
 *
 ***********************/
public class SlabBlock extends Block
{
	public static final EnumProperty<EnumSlabType> TYPE = States.SLAB_TYPE;
	public static final EnumProperty<EnumAxis> AXIS = States.AXIS;

	public SlabBlock(File f, int id)
	{
		super(f, id);
		isFull = false;
		setDefaultState(getDefaultState().with(TYPE, EnumSlabType.BOTTOM).with(AXIS, EnumAxis.Y).get());
	}

	@Override
	public BlockState getStateForPlacement(SubChunk subChunk, Player player, EnumFace placedOn, int x, int y, int z)
	{
		if (placedOn == EnumFace.DOWN)
			return getDefaultState().with(TYPE, EnumSlabType.TOP).with(AXIS, EnumAxis.Y).get();

		if (placedOn == EnumFace.UP)
			return getDefaultState().with(TYPE, EnumSlabType.BOTTOM).with(AXIS, EnumAxis.Y).get();

		double h = Double.parseDouble("0." + ("" + player.getHitResult().getPy()).split("\\.")[1]);

		if (h <= 0.25d)
		{
			return getDefaultState().with(TYPE, EnumSlabType.BOTTOM).with(AXIS, EnumAxis.Y).get();
		} else if (h >= 0.75d)
		{
			return getDefaultState().with(TYPE, EnumSlabType.TOP).with(AXIS, EnumAxis.Y).get();
		} else
		{
			return switch (placedOn)
				{
					case NORTH -> getDefaultState().with(SlabBlock.TYPE, EnumSlabType.BOTTOM).with(SlabBlock.AXIS, EnumAxis.X).get();
					case EAST -> getDefaultState().with(SlabBlock.TYPE, EnumSlabType.TOP).with(SlabBlock.AXIS, EnumAxis.Z).get();
					case SOUTH -> getDefaultState().with(SlabBlock.TYPE, EnumSlabType.TOP).with(SlabBlock.AXIS, EnumAxis.X).get();
					case WEST -> getDefaultState().with(SlabBlock.TYPE, EnumSlabType.BOTTOM).with(SlabBlock.AXIS, EnumAxis.Z).get();
					default -> getDefaultState();
				};
		}
	}

	@Override
	public void onClick(SubChunk subChunk, BlockState state, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getAction() != KeyList.PRESS)
			return;

		if (click.getButton() == KeyList.RMB)
		{
			if (CaveGame.itemInHand.getBlockToPlace() != this)
				return;

			// Combining

			if (state.get(AXIS) == EnumAxis.Y)
			{
				if ((state.get(TYPE) == EnumSlabType.BOTTOM && clickedOn == EnumFace.UP) || (state.get(TYPE) == EnumSlabType.TOP && clickedOn == EnumFace.DOWN))
				{
					subChunk.setState(state.with(TYPE, EnumSlabType.DOUBLE).with(AXIS, EnumAxis.Y).get(), x, y, z);

					subChunk.rebuildAllLayers();
					player.processNextBlockPlace = false;
				}
			} else if (state.get(AXIS) == EnumAxis.X)
			{
				if ((state.get(TYPE) == EnumSlabType.BOTTOM && clickedOn == EnumFace.NORTH) || (state.get(TYPE) == EnumSlabType.TOP && clickedOn == EnumFace.SOUTH))
				{
					subChunk.setState(state.with(TYPE, EnumSlabType.DOUBLE).with(AXIS, EnumAxis.X).get(), x, y, z);
					subChunk.rebuildAllLayers();
					player.processNextBlockPlace = false;
				}
			} else if (state.get(AXIS) == EnumAxis.Z)
			{
				if ((state.get(TYPE) == EnumSlabType.BOTTOM && clickedOn == EnumFace.WEST) || (state.get(TYPE) == EnumSlabType.TOP && clickedOn == EnumFace.EAST))
				{
					subChunk.setState(state.with(TYPE, EnumSlabType.DOUBLE).with(AXIS, EnumAxis.Z).get(), x, y, z);
					subChunk.rebuildAllLayers();
					player.processNextBlockPlace = false;
				}
			}
			return;
		}

		HitResult hitResult = CaveGame.getInstance().hitPicker.getHitResult();

		BlockState tobeplaced = state;

		final BlockState bottom = state.with(TYPE, EnumSlabType.BOTTOM).with(AXIS, EnumAxis.Y).get();
		final BlockState top = state.with(TYPE, EnumSlabType.TOP).with(AXIS, EnumAxis.Y).get();
		final BlockState north = state.with(TYPE, EnumSlabType.BOTTOM).with(AXIS, EnumAxis.X).get();
		final BlockState east = state.with(TYPE, EnumSlabType.TOP).with(AXIS, EnumAxis.Z).get();
		final BlockState south = state.with(TYPE, EnumSlabType.TOP).with(AXIS, EnumAxis.X).get();
		final BlockState west = state.with(TYPE, EnumSlabType.BOTTOM).with(AXIS, EnumAxis.Z).get();

		if (state.get(TYPE) == EnumSlabType.DOUBLE)
		{
			if (hitResult.getFace() == EnumFace.UP)
			{
				subChunk.getWorld().setState(hitResult, tobeplaced = bottom);
			} else if (hitResult.getFace() == EnumFace.DOWN)
			{
				subChunk.getWorld().setState(hitResult, tobeplaced = top);
			} else if (hitResult.getFace().isSide())
			{
				double h = Double.parseDouble("0." + ("" + hitResult.getPy()).split("\\.")[1]);

				if (h <= 0.25d)
				{
					subChunk.setState(tobeplaced = top, x, y, z);
				} else if (h >= 0.75d)
				{
					subChunk.setState(tobeplaced = bottom, x, y, z);
				} else
				{
					tobeplaced = switch (hitResult.getFace())
						{
							case NORTH -> north;
							case EAST -> east;
							case SOUTH -> south;
							case WEST -> west;
							default -> bottom;
						};
					subChunk.setState(tobeplaced, x, y, z);
				}

				subChunk.rebuildAllLayers();
			}
		} else
		{
			subChunk.getWorld().setBlock(hitResult, Block.air);
		}

		tobeplaced.getBlock().onBreak(subChunk, state, player, hitResult.getFace(), hitResult.getX(), hitResult.getY(), hitResult.getZ());

		player.processNextBlockBreak = false;
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(TYPE);
		properties.add(AXIS);
	}
}
