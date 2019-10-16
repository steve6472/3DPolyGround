package com.steve6472.polyground;

import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.entity.Player;
import com.steve6472.polyground.world.SubChunk;
import com.steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.09.2019
 * Project: SJP
 *
 ***********************/
public class BasicEvents
{
	/* Placing Block */
	public static void place(Block block, EnumFace face, Player player)
	{
		if (block == null) return;

		place(block, face, player, face.getXOffset(), face.getYOffset(), face.getZOffset());
	}

	public static void place(Block block, EnumFace face, Player player, int xOffset, int yOffset, int zOffset)
	{
		if (block == null) return;

		HitResult hr = CaveGame.getInstance().hitPicker.getHitResult();
		World world = CaveGame.getInstance().world;

		SubChunk subChunk = world.getSubChunkFromBlockCoords(hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);

		world.setBlock(hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset, block, block.rebuildChunkOnPlace(), Block::isReplaceable);
		BlockData blockData = subChunk.getBlockData(hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);

		block.onPlace(subChunk, blockData, player, face, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
		updateAll(hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
	}

	/* Replacing Block */
	public static void replace(Block block, EnumFace face, Player player)
	{
		if (block == null) return;

		replace(block, face, player, face.getXOffset(), face.getYOffset(), face.getZOffset());
	}

	public static void replace(Block block, EnumFace face, Player player, int xOffset, int yOffset, int zOffset)
	{
		if (block == null) return;

		HitResult hr = CaveGame.getInstance().hitPicker.getHitResult();
		World world = CaveGame.getInstance().world;

		SubChunk subChunk = world.getSubChunkFromBlockCoords(hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);

		world.setBlock(hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset, block, block.rebuildChunkOnPlace());
		BlockData blockData = subChunk.getBlockData(hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);

		block.onPlace(subChunk, blockData, player, face, hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
		updateAll(hr.getX() + xOffset, hr.getY() + yOffset, hr.getZ() + zOffset);
	}

	/* Breaking Block */
	public static void breakBlock(Player player)
	{
		HitResult hr = CaveGame.getInstance().hitPicker.getHitResult();
		World world = CaveGame.getInstance().world;

		SubChunk subChunk = world.getSubChunkFromBlockCoords(hr.getX(), hr.getY(), hr.getZ());

		BlockRegistry.getBlockById(world.getBlockId(hr.getX(), hr.getY(), hr.getZ())).onBreak(subChunk, subChunk.getBlockData(hr.getX(), hr.getY(), hr.getZ()), player, hr.getFace(), hr.getX(), hr.getY(), hr.getZ());
		world.setBlock(hr.getX(), hr.getY(), hr.getZ(), Block.air.getId());
		updateAll(hr.getX(), hr.getY(), hr.getZ());
	}

	/* Updating Block */
	public static void updateAll(int x, int y, int z)
	{
		update(EnumFace.NONE, x, y, z);
		for (EnumFace face : EnumFace.getFaces())
		{
			update(face, x + face.getXOffset(), y + face.getYOffset(), z + face.getZOffset());
		}
	}

	public static void update(EnumFace updateFrom, int x, int y, int z)
	{
		World world = CaveGame.getInstance().world;
		SubChunk subChunk = world.getSubChunkFromBlockCoords(x, y, z);

		if (subChunk == null) return;

		Block block = world.getBlock(x, y, z);

		BlockData blockData = subChunk.getBlockData(x, y, z);

		block.onUpdate(subChunk, blockData, updateFrom, x, y, z);
	}
}
