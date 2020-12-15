package steve6472.polyground.block.blockdata.micro;

import net.querz.nbt.tag.CompoundTag;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.gfx.Palette;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.11.2020
 * Project: CaveGame
 *
 ***********************/
public abstract class AbstractIndexedMicroBlockData extends BlockData
{
	public byte[][] grid;
	public int pieceCount;

	public AbstractIndexedMicroBlockData()
	{
		// layer, 2d grid
		grid = new byte[getSize()][getSize() * getSize()];

		for (int i = 0; i < getSize(); i++)
		{
			for (int j = 0; j < getSize() * getSize(); j++)
			{
				if (i == 0)
				{
					grid[0][j] = -127;
					pieceCount++;
				} else
					grid[i][j] = -128;
			}
		}
	}

	protected int getSize()
	{
		return 16;
	}

	public abstract Palette getPalette();

	@Override
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		for (int i = 0; i < getSize(); i++)
		{
			tag.putByteArray("layer" + i, grid[i]);
		}
		tag.putInt("pieceCount", pieceCount);
		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		grid = new byte[getSize()][];
		pieceCount = tag.getInt("pieceCount");
		for (int i = 0; i < getSize(); i++)
		{
			byte[] layer = tag.getByteArray("layer" + i);
			grid[i] = layer;
		}
	}
}
