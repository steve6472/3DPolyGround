package com.steve6472.polyground.world;

import com.steve6472.polyground.HitResult;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.world.chunk.Chunk;

import java.util.function.Function;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.09.2019
 * Project: SJP
 *
 ***********************/
public interface IBlockProvider extends IChunkProvider
{
	default int getBlock(int x, int y, int z)
	{
		Chunk c = getChunkFromBlockPos(x, z);
		if (c == null)
			return Block.air.getId();
		else
			return c.getBlock(Math.floorMod(x, 16), y, Math.floorMod(z, 16));
	}

	default void setBlock(int x, int y, int z, int id)
	{
		Chunk c = getChunkFromBlockPos(x, z);
		if (c != null)
			c.setBlock(Math.floorMod(x, 16), y, Math.floorMod(z, 16), id);
	}

	/**
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param z z coordinate
	 * @param id Id of block to be set
	 * @param canSet Type is id of block at xyz
	 */
	default void setBlock(int x, int y, int z, int id, Function<Integer, Boolean> canSet)
	{
		Chunk c = getChunkFromBlockPos(x, z);
		if (c != null)
			if (canSet.apply(c.getBlock(Math.floorMod(x, 16), y, Math.floorMod(z, 16))))
				c.setBlock(Math.floorMod(x, 16), y, Math.floorMod(z, 16), id);
	}

	default void setBlock(int x, int y, int z, Block block)
	{
		setBlock(x, y, z, block.getId());
	}

	default void setBlock(HitResult hitResult, Block block)
	{
		setBlock(hitResult.getX(), hitResult.getY(), hitResult.getZ(), block.getId());
	}
}
