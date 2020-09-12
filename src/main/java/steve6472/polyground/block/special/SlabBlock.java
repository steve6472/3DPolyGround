package steve6472.polyground.block.special;

import org.json.JSONObject;
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
import steve6472.polyground.entity.Palette;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.world.World;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

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

	public SlabBlock(JSONObject json)
	{
		super(json);
		isFull = false;
		setDefaultState(getDefaultState().with(TYPE, EnumSlabType.BOTTOM).with(AXIS, EnumAxis.Y).get());
	}

	@Override
	public BlockState getStateForPlacement(World world, Player player, EnumFace placedOn, int x, int y, int z)
	{
		if (player == null || placedOn == null)
			return getDefaultState();

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
	public void onClick(BlockState state, World world, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (!player.canBreakBlocks())
			return;

		if (click.getAction() != KeyList.PRESS || click.getButton() == KeyList.MMB)
			return;

		if (click.getButton() == KeyList.RMB)
		{
			if (player.getHoldedPaletteItem().getBlockToPlace() != this)
				return;

			Palette palette = player.getHoldedPalette();

			// Combining

			if (state.get(AXIS) == EnumAxis.Y)
			{
				if ((state.get(TYPE) == EnumSlabType.BOTTOM && clickedOn == EnumFace.UP) || (state.get(TYPE) == EnumSlabType.TOP && clickedOn == EnumFace.DOWN))
				{
					world.setState(state.with(TYPE, EnumSlabType.DOUBLE).with(AXIS, EnumAxis.Y).get(), x, y, z);
					player.processNextBlockPlace = false;
					if (palette != null) palette.removeItem();
				}
			} else if (state.get(AXIS) == EnumAxis.X)
			{
				if ((state.get(TYPE) == EnumSlabType.BOTTOM && clickedOn == EnumFace.NORTH) || (state.get(TYPE) == EnumSlabType.TOP && clickedOn == EnumFace.SOUTH))
				{
					world.setState(state.with(TYPE, EnumSlabType.DOUBLE).with(AXIS, EnumAxis.X).get(), x, y, z);
					player.processNextBlockPlace = false;
					if (palette != null) palette.removeItem();
				}
			} else if (state.get(AXIS) == EnumAxis.Z)
			{
				if ((state.get(TYPE) == EnumSlabType.BOTTOM && clickedOn == EnumFace.WEST) || (state.get(TYPE) == EnumSlabType.TOP && clickedOn == EnumFace.EAST))
				{
					world.setState(state.with(TYPE, EnumSlabType.DOUBLE).with(AXIS, EnumAxis.Z).get(), x, y, z);
					player.processNextBlockPlace = false;
					if (palette != null) palette.removeItem();
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
				world.setState(tobeplaced = bottom, hitResult);
			} else if (hitResult.getFace() == EnumFace.DOWN)
			{
				world.setState(tobeplaced = top, hitResult);
			} else if (hitResult.getFace().isSide())
			{
				double h = Double.parseDouble("0." + ("" + hitResult.getPy()).split("\\.")[1]);

				if (h <= 0.25d)
				{
					world.setState(tobeplaced = top, x, y, z);
				} else if (h >= 0.75d)
				{
					world.setState(tobeplaced = bottom, x, y, z);
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
					world.setState(tobeplaced, x, y, z);
				}
			}
		} else
		{
			world.setBlock(hitResult, Block.air);
		}

		tobeplaced.getBlock().onPlayerBreak(state, world, player, hitResult.getFace(), hitResult.getX(), hitResult.getY(), hitResult.getZ());

		player.processNextBlockBreak = false;
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(TYPE);
		properties.add(AXIS);
	}
}
