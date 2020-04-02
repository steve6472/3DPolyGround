package steve6472.polyground.world.chunk;

import steve6472.polyground.block.Block;
import steve6472.polyground.registry.BlockRegistry;

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

		updateNeighbours(x, y, z);
	}

	private void updateNeighbours(int x, int y, int z)
	{
		subChunk.getParent().updateNeighbours(subChunk, x, y, z);
	}

	/**
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
		if (x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 16)
		{
			return getBlock(x, y, z);
		} else
		{
			SubChunk sc = subChunk.getNeighbouringSubChunk(x, y, z);
			if (sc == null)
				return Block.air;
			return sc.getBlock(Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16));
		}
	}

	public void setBlock(int x, int y, int z, Block block)
	{
		setBlock(x, y, z, block.getId());
	}
}
