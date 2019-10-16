package com.steve6472.polyground.world;

import com.steve6472.polyground.HitResult;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.registry.BlockRegistry;

import java.util.function.Function;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.09.2019
 * Project: SJP
 *
 ***********************/
public interface IBlockProvider extends IChunkProvider
{
	default int getBlockId(int x, int y, int z)
	{
		Chunk c = getChunkFromBlockPos(x, z);
		if (c == null)
			return Block.air.getId();
		else
		{
			return c.getBlockId(Math.floorMod(x, 16), y, Math.floorMod(z, 16));
		}
	}

	default Block getBlock(int x, int y, int z)
	{
		return BlockRegistry.getBlockById(getBlockId(x, y, z));
	}

	default void setBlock(int x, int y, int z, Block block, boolean rebuild)
	{
		Chunk c = getChunkFromBlockPos(x, z);
		if (c != null)
		{
			c.setBlock(Math.floorMod(x, 16), y, Math.floorMod(z, 16), block.getId(), rebuild);
		}
	}

	/**
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 * @param rebuild if true chunk will rebuild its geometry
	 * @param id Id of block to be set
	 * @param canSet Type is id of block at xyz
	 */
	default void setBlock(int x, int y, int z, int id, boolean rebuild, Function<Block, Boolean> canSet)
	{
		Chunk c = getChunkFromBlockPos(x, z);
		if (c != null)
		{
			if (canSet.apply(c.getBlock(Math.floorMod(x, 16), y, Math.floorMod(z, 16))))
				c.setBlock(Math.floorMod(x, 16), y, Math.floorMod(z, 16), id, rebuild);
		}
	}

	/**
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 * @param id Id of block to be set
	 * @param canSet Type is id of block at xyz
	 */
	default void setBlock(int x, int y, int z, int id, Function<Block, Boolean> canSet)
	{
		Chunk c = getChunkFromBlockPos(x, z);
		if (c != null)
		{
			if (canSet.apply(c.getBlock(Math.floorMod(x, 16), y, Math.floorMod(z, 16))))
				c.setBlock(Math.floorMod(x, 16), y, Math.floorMod(z, 16), id, true);
		}
	}

	/**
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 * @param block Block to be set
	 * @param canSet Type is id of block at xyz
	 */
	default void setBlock(int x, int y, int z, Block block, Function<Block, Boolean> canSet)
	{
		setBlock(x, y, z, block.getId(), canSet);
	}

	/**
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 * @param block Block to be set
	 * @param rebuild if true chunk will rebuild its geometry
	 * @param canSet Type is id of block at xyz
	 */
	default void setBlock(int x, int y, int z, Block block, boolean rebuild, Function<Block, Boolean> canSet)
	{
		setBlock(x, y, z, block.getId(), rebuild, canSet);
	}

	default void setBlock(int x, int y, int z, int id)
	{
		Chunk c = getChunkFromBlockPos(x, z);
		if (c != null)
		{
			c.setBlock(Math.floorMod(x, 16), y, Math.floorMod(z, 16), id, true);
		}
	}

	default void setBlock(HitResult hitResult, int id)
	{
		setBlock(hitResult.getX(), hitResult.getY(), hitResult.getZ(), id);
	}

	default void setBlock(int x, int y, int z, Block block)
	{
		setBlock(x, y, z, block.getId());
	}

	default void setBlock(HitResult hitResult, Block block)
	{
		setBlock(hitResult.getX(), hitResult.getY(), hitResult.getZ(), block.getId());
	}

	default void setBlock(int x, int y, int z, int id, boolean rebuild)
	{
		Chunk c = getChunkFromBlockPos(x, z);
		if (c != null)
		{
			c.setBlock(Math.floorMod(x, 16), y, Math.floorMod(z, 16), id, rebuild);
		}
	}
}
