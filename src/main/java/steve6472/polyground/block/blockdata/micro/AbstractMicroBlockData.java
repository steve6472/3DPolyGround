package steve6472.polyground.block.blockdata.micro;

import net.querz.nbt.tag.CompoundTag;
import steve6472.polyground.block.blockdata.BlockData;

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
		grid = new int[getSize()][getSize() * getSize()];

		for (int i = 0; i < getSize(); i++)
		{
			for (int j = 0; j < getSize(); j++)
			{
				grid[0][i + j * getSize()] = 0x303030;
				pieceCount++;
			}
		}
	}

	protected int getSize()
	{
		return 16;
	}

	@Override
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		for (int i = 0; i < getSize(); i++)
		{
			tag.putIntArray("layer" + i, grid[i]);
		}
		tag.putInt("pieceCount", pieceCount);
		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		grid = new int[getSize()][];
		pieceCount = tag.getInt("pieceCount");
		for (int i = 0; i < getSize(); i++)
		{
			int[] layer = tag.getIntArray("layer" + i);
			grid[i] = layer;
		}
	}
}
