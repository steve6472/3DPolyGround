package steve6472.polyground.block.blockdata;

import net.querz.nbt.tag.CompoundTag;
import org.joml.Matrix4f;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.gfx.DynamicEntityModel;
import steve6472.polyground.gfx.IModel;
import steve6472.polyground.registry.blockdata.BlockDataRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2020
 * Project: CaveGame
 *
 ***********************/
public class ChiselBlockData extends BlockData implements IModel
{
	public int[][] grid;
	public int pieceCount;
	public DynamicEntityModel model;

	public ChiselBlockData()
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

		model = new DynamicEntityModel();
		updateModel();
	}

	public void updateModel()
	{
		model.load((modelBuilder) -> {

			Bakery.tempBuilder(modelBuilder);

			int tris = 0;

			for (int i = 0; i < 16; i++)
			{
				for (int j = 0; j < 16; j++)
				{
					for (int k = 0; k < 16; k++)
					{
						int flags = Bakery.createFaceFlags(
							i != 15 && grid[j][(i + 1) + k * 16] != 0,
							k != 15 && grid[j][i + (k + 1) * 16] != 0,
							i != 0 && grid[j][(i - 1) + k * 16] != 0,
							k != 0 && grid[j][i + (k - 1) * 16] != 0,
							j != 15 && grid[j + 1][i + k * 16] != 0,
							j != 0 && grid[j - 1][i + k * 16] != 0
						);
						if (grid[j][i + k * 16] != 0)
						{
							tris += Bakery.coloredCube_1x1(i, j, k, grid[j][i + k * 16], flags);
						}
					}
				}
			}

			Bakery.worldBuilder();

			return tris;
		});
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

		updateModel();
	}

	@Override
	public String getId()
	{
		return BlockDataRegistry.chisel.id();
	}

	@Override
	public void render(Matrix4f viewMatrix, Matrix4f mat)
	{
		model.render(viewMatrix, mat);
	}
}
