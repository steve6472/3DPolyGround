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
	private final BlockState[][][] states;

	public SubChunkBlocks()
	{
		states = new BlockState[16][16][16];
		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					states[i][j][k] = Block.AIR.getDefaultState();
				}
			}
		}
	}

	public BlockState[][][] getStates()
	{
		return states;
	}

	public void setState(BlockState state, int x, int y, int z)
	{
		states[x][y][z] = state;
	}

	public BlockState getState(int x, int y, int z)
	{
		return states[x][y][z];
	}
}
