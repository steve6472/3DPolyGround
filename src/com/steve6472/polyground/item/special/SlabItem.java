package com.steve6472.polyground.item.special;

import com.steve6472.polyground.*;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.block.special.SlabBlock;
import com.steve6472.polyground.entity.Player;
import com.steve6472.polyground.item.Item;
import com.steve6472.polyground.world.SubChunk;
import com.steve6472.polyground.world.World;
import com.steve6472.sge.main.KeyList;
import com.steve6472.sge.main.events.MouseEvent;
import com.steve6472.sss2.SSS;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 24.09.2019
 * Project: SJP
 *
 ***********************/
public class SlabItem extends Item
{
	private Block bottom, top, both;

	public SlabItem(File f, int id)
	{
		super(f, id);
		if (f.isFile())
		{
			SSS sss = new SSS(f);

			try
			{
				bottom = BlockRegistry.getBlockByName(sss.getString("bottom"));
				top = BlockRegistry.getBlockByName(sss.getString("top"));
				both = BlockRegistry.getBlockByName(sss.getString("both"));
			} catch (Exception ex)
			{
				System.err.println(getName());
				ex.printStackTrace();
				System.exit(1);
			}
		}
	}

	@Override
	public void onClick(SubChunk subChunk, BlockData blockData, Player player, EnumFace clickedOn, MouseEvent click, int x, int y, int z)
	{
		if (!player.processNextBlockPlace) return;

		if (!(click.getAction() == KeyList.PRESS && click.getButton() == KeyList.RMB)) return;

		if (!(CaveGame.itemInHand.getBlockToPlace() instanceof SlabBlock))
			return;

		World world = CaveGame.getInstance().world;

		HitResult hitResult = CaveGame.getInstance().hitPicker.getHitResult();

		Block placed;

		/* Combining the slabs */
		if ((placed = world.getBlock(hitResult.getX(), hitResult.getY(), hitResult.getZ())) instanceof SlabBlock && !hitResult.getFace().isSide())
		{
			if (!baseName(placed).equals(baseName(CaveGame.itemInHand.getBlockToPlace()))) return;

			Block toPlace = both;

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

		placed = getPlacedBlock(hitResult);

		if (placed instanceof SlabBlock)
			addToSlab(player, hitResult, placed);
		else if (placed.isReplaceable())
			placeSlab(player, hitResult);

		player.processNextBlockPlace = false;
	}

	private void addToSlab(Player player, HitResult hitResult, Block placedBlock)
	{
		if (!baseName(placedBlock).equals(baseName(CaveGame.itemInHand.getBlockToPlace()))) return;

		Block doubleSlab = both;
		BasicEvents.replace(doubleSlab, hitResult.getFace(), player);
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

	private enum EnumSlabType
	{
		TOP, BOTTOM, DOUBLE, OPPOSITE
	}

	private String baseName(Block block)
	{
		String inHand = block.getName();

		if (inHand.endsWith("_top")) return inHand.substring(0, inHand.length() - 4);
		if (inHand.endsWith("_bottom")) return inHand.substring(0, inHand.length() - 7);
		if (inHand.startsWith("double_")) return inHand.substring(7);

		return inHand;
	}

//	private Block dynamicSlab(EnumSlabType type, Block block)
//	{
//		String inHand = block.getName();
//
//		String base = baseName(block);
//
//		if (type == EnumSlabType.TOP)
//		{
//			return BlockRegistry.getBlockByName(base + "_top");
//		}
//		if (type == EnumSlabType.BOTTOM)
//		{
//			return BlockRegistry.getBlockByName(base + "_bottom");
//		}
//		if (type == EnumSlabType.DOUBLE)
//		{
//			return BlockRegistry.getBlockByName("double_" + base );
//		}
//		if (type == EnumSlabType.OPPOSITE)
//		{
//			if (inHand.endsWith("_top")) return BlockRegistry.getBlockByName(base + "_bottom");
//			if (inHand.endsWith("_bottom")) return BlockRegistry.getBlockByName(base + "_top");
//			if (inHand.startsWith("double_")) return block;
//		}
//
//		return Block.air;
//	}

	private Block getPlacedBlock(HitResult hitResult)
	{
		World world = CaveGame.getInstance().world;
		return switch (hitResult.getFace())
			{
				case UP ->    world.getBlock(hitResult.getX(), hitResult.getY() + 1, hitResult.getZ());
				case DOWN ->  world.getBlock(hitResult.getX(), hitResult.getY() - 1, hitResult.getZ());
				case NORTH -> world.getBlock(hitResult.getX() + 1, hitResult.getY(), hitResult.getZ());
				case SOUTH -> world.getBlock(hitResult.getX() - 1, hitResult.getY(), hitResult.getZ());
				case EAST ->  world.getBlock(hitResult.getX(), hitResult.getY(), hitResult.getZ() + 1);
				case WEST ->  world.getBlock(hitResult.getX(), hitResult.getY(), hitResult.getZ() - 1);
				case NONE -> Block.air;
			};
	}
}
