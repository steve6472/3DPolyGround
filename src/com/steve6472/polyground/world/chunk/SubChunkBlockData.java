package com.steve6472.polyground.world.chunk;

import com.steve6472.polyground.block.blockdata.BlockData;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 20.12.2019
 * Project: SJP
 *
 ***********************/
public class SubChunkBlockData
{
	private SubChunk subChunk;

	private BlockData[][][] blockData;

	public SubChunkBlockData(SubChunk subChunk)
	{
		this.subChunk = subChunk;
		blockData = new BlockData[16][16][16];
	}

	public BlockData getBlockData(int x, int y, int z)
	{
		return blockData[x][y][z];
	}

	public BlockData[][][] getBlockData()
	{
		return blockData;
	}

	/**
	 *
	 * Can check neighbour chunks.
	 * Should be more efficient for chunk border block data checking
	 * as it does not have to create new Chunk Key everytime
	 *
	 * @param x x coordinate of block data
	 * @param y y coordinate of block data
	 * @param z z coordinate of block data
	 * @return int Light
	 */
	public BlockData getBlockDataEfficiently(int x, int y, int z)
	{
		if (x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 16)
		{
			return getBlockData(x, y, z);
		} else
		{
			SubChunk sc = subChunk.getNeighbouringSubChunk(x, y, z);
			if (sc == null)
				return null;
			return sc.getBlockData(Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16));
		}
	}
}
