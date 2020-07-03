package steve6472.polyground.item.special;

import steve6472.polyground.BasicEvents;
import steve6472.polyground.CaveGame;
import steve6472.polyground.EnumFace;
import steve6472.polyground.HitResult;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.properties.enums.EnumSlabType;
import steve6472.polyground.block.special.SlabBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.entity.Player;
import steve6472.polyground.item.Item;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.events.MouseEvent;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 24.09.2019
 * Project: SJP
 *
 ***********************/
public class SlabItem extends Item
{
	private BlockState top, bottom, both;

	public SlabItem(File f, int id)
	{
		super(f, id);
		try
		{
			Block block = getBlockToPlace();

			top = block.getDefaultState().with(SlabBlock.TYPE, EnumSlabType.TOP).get();
			bottom = block.getDefaultState().with(SlabBlock.TYPE, EnumSlabType.BOTTOM).get();
			both = block.getDefaultState().with(SlabBlock.TYPE, EnumSlabType.DOUBLE).get();
		} catch (Exception ex)
		{
			System.err.println(getName());
			ex.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void onClick(SubChunk subChunk, BlockState state, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (click.getAction() == KeyList.PRESS && click.getButton() == KeyList.RMB)
			place(player);
	}

	private void place(Player player)
	{
		if (!player.processNextBlockPlace)
			return;

		if (!(CaveGame.itemInHand.getBlockToPlace() instanceof SlabBlock))
			return;

		World world = CaveGame.getInstance().world;

		HitResult hitResult = CaveGame.getInstance().hitPicker.getHitResult();

		BlockState placed;

		/* Combining the slabs */
		if ((placed = world.getState(hitResult.getX(), hitResult.getY(), hitResult.getZ())).getBlock() instanceof SlabBlock && !hitResult.getFace().isSide())
		{
			if (placed.getBlock() != CaveGame.itemInHand.getBlockToPlace())
				return;

			BlockState toPlace = both;

			if (hitResult.getFace() == EnumFace.UP)
			{
				if (bottom == placed)
				{
					BasicEvents.replace(toPlace, EnumFace.UP, player, 0, 0, 0);
				} else if (world.getBlock(hitResult.getX(), hitResult.getY() + 1, hitResult.getZ()).isReplaceable())
				{
					BasicEvents.place(bottom, EnumFace.UP, player);
				} else if (top == placed)
				{
					BasicEvents.replace(toPlace, EnumFace.UP, player, 0, 1, 0);
				}

				player.processNextBlockPlace = false;
				return;
			}

			if (hitResult.getFace() == EnumFace.DOWN)
			{
				if (top == placed)
				{
					BasicEvents.replace(toPlace, EnumFace.DOWN, player, 0, 0, 0);
				} else if (world.getBlock(hitResult.getX(), hitResult.getY() - 1, hitResult.getZ()).isReplaceable())
				{
					BasicEvents.place(top, EnumFace.DOWN, player);
				} else if (bottom == placed)
				{
					BasicEvents.replace(toPlace, EnumFace.DOWN, player, 0, -1, 0);
				}

				player.processNextBlockPlace = false;
				return;
			}
		}

		placed = getPlacedBlock(hitResult).getDefaultState();

		if (placed.getBlock() instanceof SlabBlock)
			addToSlab(player, hitResult, placed);
		else if (placed.getBlock().isReplaceable())
			placeSlab(player, hitResult);

		player.processNextBlockPlace = false;
	}

	private void addToSlab(Player player, HitResult hitResult, BlockState placedBlock)
	{
		if (placedBlock.getBlock() != CaveGame.itemInHand.getBlockToPlace())
			return;

		BasicEvents.replace(both, hitResult.getFace(), player);
	}

	private void placeSlab(Player player, HitResult hitResult)
	{
		if (hitResult.getFace() == EnumFace.DOWN)
		{
			BasicEvents.place(top, EnumFace.DOWN, player);
			return;
		}

		if (hitResult.getFace() == EnumFace.UP)
		{
			BasicEvents.place(bottom, EnumFace.UP, player);
			return;
		}

		if (Double.parseDouble("0." + ("" + hitResult.getPy()).split("\\.")[1]) >= 0.5f)
		{
			BasicEvents.place(top, hitResult.getFace(), player);
		} else
		{
			BasicEvents.place(bottom, hitResult.getFace(), player);
		}
	}

	private Block getPlacedBlock(HitResult hitResult)
	{
		World world = CaveGame.getInstance().world;
		return switch (hitResult.getFace())
			{
				case UP -> world.getBlock(hitResult.getX(), hitResult.getY() + 1, hitResult.getZ());
				case DOWN -> world.getBlock(hitResult.getX(), hitResult.getY() - 1, hitResult.getZ());
				case NORTH -> world.getBlock(hitResult.getX() + 1, hitResult.getY(), hitResult.getZ());
				case SOUTH -> world.getBlock(hitResult.getX() - 1, hitResult.getY(), hitResult.getZ());
				case EAST -> world.getBlock(hitResult.getX(), hitResult.getY(), hitResult.getZ() + 1);
				case WEST -> world.getBlock(hitResult.getX(), hitResult.getY(), hitResult.getZ() - 1);
				case NONE -> Block.air;
			};
	}
}
