package com.steve6472.polyground.world.chunk;

import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.world.World;

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
	 * @param x x coordinate of light
	 * @param y y coordinate of light
	 * @param z z coordinate of light
	 * @return int Light
	 */
	public BlockData getBlockDataEfficiently(int x, int y, int z)
	{
		int maxLayer = subChunk.getParent().getSubChunks().length;

		World world = subChunk.getWorld();

		if (x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 16)
		{
			return getBlockData(x, y, z);
		} else
		{
			if (x == 16)
			{
				SubChunk sc = world.getSubChunk(subChunk.getX() + 1, subChunk.getLayer(), subChunk.getZ());
				if (sc == null)
					return null;
				return sc.getBlockDataEfficiently(0, y, z);
			} else if (x == -1)
			{
				SubChunk sc = world.getSubChunk(subChunk.getX() - 1, subChunk.getLayer(), subChunk.getZ());
				if (sc == null)
					return null;
				return sc.getBlockDataEfficiently(15, y, z);
			}

			if (z == 16)
			{
				SubChunk sc = world.getSubChunk(subChunk.getX(), subChunk.getLayer(), subChunk.getZ() + 1);
				if (sc == null)
					return null;
				return sc.getBlockDataEfficiently(x, y, 0);
			} else if (z == -1)
			{
				SubChunk sc = world.getSubChunk(subChunk.getX(), subChunk.getLayer(), subChunk.getZ() - 1);
				if (sc == null)
					return null;
				return sc.getBlockDataEfficiently(x, y, 15);
			}

			if (y == -1 && subChunk.getLayer() > 0)
			{
				return subChunk.getParent().getSubChunks()[subChunk.getLayer() - 1].getBlockDataEfficiently(x, 15, z);
			} else if (y == 16 && subChunk.getLayer() + 1 < maxLayer)
			{
				return subChunk.getParent().getSubChunks()[subChunk.getLayer() + 1].getBlockDataEfficiently(x, 0, z);
			} else
			{
				return null;
			}
		}
	}
}
