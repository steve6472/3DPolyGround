package com.steve6472.polyground.world.chunk;

import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.11.2019
 * Project: SJP
 *
 ***********************/
public class SubChunkBlocks
{
	private SubChunk subChunk;

	private int[][][] ids;

	public SubChunkBlocks(SubChunk subChunk)
	{
		this.subChunk = subChunk;
		ids = new int[16][16][16];
	}

	public int[][][] getIds()
	{
		return ids;
	}

	public int getBlockId(int x, int y, int z)
	{
		return getIds()[x][y][z];
	}

	public Block getBlock(int x, int y, int z)
	{
		return BlockRegistry.getBlockById(getBlockId(x, y, z));
	}

	public void setBlock(int x, int y, int z, int id)
	{
		getIds()[x][y][z] = id;
		subChunk.updateAllLayers();

		updateNeighbours(x, y, z);
	}

	private void updateNeighbours(int x, int y, int z)
	{
		if (x == 15)
		{
			SubChunk sc = subChunk.getWorld().getSubChunk(subChunk.getX() + 1, subChunk.getLayer(), subChunk.getZ());
			if (sc != null) sc.updateAllLayers();
		} else if (x == 0)
		{
			SubChunk sc = subChunk.getWorld().getSubChunk(subChunk.getX() - 1, subChunk.getLayer(), subChunk.getZ());
			if (sc != null) sc.updateAllLayers();
		}

		if (z == 15)
		{
			SubChunk sc = subChunk.getWorld().getSubChunk(subChunk.getX(), subChunk.getLayer(), subChunk.getZ() + 1);
			if (sc != null) sc.updateAllLayers();
		} else if (z == 0)
		{
			SubChunk sc = subChunk.getWorld().getSubChunk(subChunk.getX(), subChunk.getLayer(), subChunk.getZ() - 1);
			if (sc != null) sc.updateAllLayers();
		}

		if (y == 0 && subChunk.getLayer() > 0)
		{
			subChunk.getParent().getSubChunks()[subChunk.getLayer() - 1].updateAllLayers();
		} else if (y == 16 && subChunk.getLayer() + 1 < subChunk.getParent().getSubChunks().length)
		{
			subChunk.getParent().getSubChunks()[subChunk.getLayer() + 1].updateAllLayers();
		}
	}

	/**
	 *
	 * Can check neighbour chunks.
	 * Should be more efficient for chunk border block checking
	 * as it does not have to create new Chunk Key everytime
	 *
	 * @param x x coordinate of block
	 * @param y y coordinate of block
	 * @param z z coordinate of block
	 * @return Block
	 */
	public Block getBlockEfficiently(int x, int y, int z)
	{
		int maxLayer = subChunk.getParent().getSubChunks().length;

		World world = subChunk.getWorld();

		if (x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 16)
		{
			return getBlock(x, y, z);
		} else
		{
			if (x == 16)
			{
				SubChunk sc = world.getSubChunk(subChunk.getX() + 1, subChunk.getLayer(), subChunk.getZ());
				if (sc == null)
					return Block.air;
				return sc.getBlockEfficiently(0, y, z);
			} else if (x == -1)
			{
				SubChunk sc = world.getSubChunk(subChunk.getX() - 1, subChunk.getLayer(), subChunk.getZ());
				if (sc == null)
					return Block.air;
				return sc.getBlockEfficiently(15, y, z);
			}

			if (z == 16)
			{
				SubChunk sc = world.getSubChunk(subChunk.getX(), subChunk.getLayer(), subChunk.getZ() + 1);
				if (sc == null)
					return Block.air;
				return sc.getBlockEfficiently(x, y, 0);
			} else if (z == -1)
			{
				SubChunk sc = world.getSubChunk(subChunk.getX(), subChunk.getLayer(), subChunk.getZ() - 1);
				if (sc == null)
					return Block.air;
				return sc.getBlockEfficiently(x, y, 15);
			}

			if (y == -1 && subChunk.getLayer() > 0)
			{
				return subChunk.getParent().getSubChunks()[subChunk.getLayer() - 1].getBlockEfficiently(x, 15, z);
			} else if (y == 16 && subChunk.getLayer() + 1 < maxLayer)
			{
				return subChunk.getParent().getSubChunks()[subChunk.getLayer() + 1].getBlockEfficiently(x, 0, z);
			} else
			{
				return Block.air;
			}
		}
	}

	public void setBlock(int x, int y, int z, Block block)
	{
		setBlock(x, y, z, block.getId());
	}
}
