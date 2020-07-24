package steve6472.polyground.world.chunk;

import steve6472.polyground.block.blockdata.BlockData;

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
}
