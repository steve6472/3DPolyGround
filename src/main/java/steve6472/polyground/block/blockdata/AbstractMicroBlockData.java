package steve6472.polyground.block.blockdata;

import net.querz.nbt.tag.CompoundTag;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.11.2020
 * Project: CaveGame
 *
 ***********************/
public abstract class AbstractMicroBlockData extends BlockData
{
	public int[][] grid;
	public int pieceCount;

	public AbstractMicroBlockData()
	{
		// layer, 2d grid
		grid = new int[16][256];

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				grid[0][i + j * 16] = 0x303030;
				pieceCount++;
			}
		}
	}

	@Override
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		for (int i = 0; i < 16; i++)
		{
			tag.putIntArray("layer" + i, grid[i]);
		}
		tag.putInt("pieceCount", pieceCount);
		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		grid = new int[16][];
		pieceCount = tag.getInt("pieceCount");
		for (int i = 0; i < 16; i++)
		{
			int[] layer = tag.getIntArray("layer" + i);
			grid[i] = layer;
		}
	}
}
