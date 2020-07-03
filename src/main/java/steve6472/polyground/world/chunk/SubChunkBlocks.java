package steve6472.polyground.world.chunk;

import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.11.2019
 * Project: SJP
 *
 ***********************/
public class SubChunkBlocks
{
	private final SubChunk subChunk;
	private final BlockState[][][] states;

	public SubChunkBlocks(SubChunk subChunk)
	{
		this.subChunk = subChunk;
		states = new BlockState[16][16][16];
		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					states[i][j][k] = Block.air.getDefaultState();
				}
			}
		}
	}

	public BlockState[][][] getStates()
	{
		return states;
	}

	public void setStateNC(BlockState state, int x, int y, int z)
	{
		states[x][y][z] = state;
	}

	public BlockState getStateNC(int x, int y, int z)
	{
		return states[x][y][z];
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
	public BlockState getState(int x, int y, int z)
	{
		if (x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 16)
		{
			return getStateNC(x, y, z);
		} else
		{
			SubChunk sc = subChunk.getNeighbouringSubChunk(x, y, z);
			if (sc == null)
				return Block.air.getDefaultState();
			return sc.getState(Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16));
		}
	}

	public void setState(BlockState state, int x, int y, int z)
	{
		if (x >= 0 && x < 16 && z >= 0 && z < 16 && y >= 0 && y < 16)
		{
			setStateNC(state, x, y, z);
		} else
		{
			SubChunk sc = subChunk.getNeighbouringSubChunk(x, y, z);
			if (sc == null)
				return;
			sc.setState(state, Math.floorMod(x, 16), Math.floorMod(y, 16), Math.floorMod(z, 16));
		}
	}
}
