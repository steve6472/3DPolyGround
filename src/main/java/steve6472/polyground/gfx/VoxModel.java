package steve6472.polyground.gfx;

import java.io.File;
import java.io.IOException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 11.12.2020
 * Project: CaveGame
 *
 ***********************/
public class VoxModel
{
	private byte[][] model;
	private int width, height, depth;
	private final File path;

	public VoxModel(File path)
	{
		this.path = path;
	}

	public void reloadModel()
	{
		VoxelFileReader voxReader = new VoxelFileReader();
		try
		{
			voxReader.read(path, (w, h, d) ->
			{
				width = w;
				height = d;
				depth = h;
//				System.out.printf("W: %d, H: %d, D: %d, Path: %s\n", w, h, d, path.getAbsolutePath());
				model = new byte[d][w * h];
				for (int i = 0; i < height; i++)
				{
					for (int j = 0; j < width * depth; j++)
					{
						model[i][j] = -128;
					}
				}
			}, (x, y, z, i) ->
			{
				model[z][x + (depth - 1 - y) * width] = (byte) (i - 1);
			});
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public byte[][] createCopy()
	{
		byte[][] modelCopy = new byte[height][width * depth];
		for (int i = 0; i < height; i++)
		{
			System.arraycopy(model[i], 0, modelCopy[i], 0, depth * width);
		}

		return modelCopy;
	}

	public byte[][] getModel()
	{
		return model;
	}

	public void insert(byte[][] target, int targetWidth, int targetDepth, int x, int y, int z)
	{
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				for (int k = 0; k < depth; k++)
				{
					target[i + y][(j + x) + (k + z) * targetDepth] = model[i][k + j * depth];
				}
			}
		}
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public int getDepth()
	{
		return depth;
	}
}
