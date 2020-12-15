package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.gfx.Palette;
import steve6472.polyground.gfx.VoxModel;
import steve6472.polyground.registry.palette.PaletteRegistry;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.12.2020
 * Project: CaveGame
 *
 ***********************/
public class VoxStatueBlock extends CustomBlock
{
	private int size;
	private VoxModel model;
	private Palette palette;

	public VoxStatueBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public void load(JSONObject json)
	{
		model = new VoxModel(new File("custom_models/vox/" + json.getString("path") + ".vox"));
		model.reloadModel();
		size = model.getWidth();
		this.palette = PaletteRegistry.getOrRegister(json.getString("palette"));
		this.palette.reloadPalette();
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		if (modelLayer != ModelLayer.NORMAL)
			return 0;

		int tris = 0;
		buildHelper.setSubChunk(world.getSubChunkFromBlockCoords(x, y, z));

		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				for (int k = 0; k < size; k++)
				{
					if (model.getModel()[j][i + k * size] != -128)
					{
						int flags = Bakery.createFaceFlags(
							i != (size - 1) && model.getModel()[j][(i + 1) + k * size] != -128,
							k != (size - 1) && model.getModel()[j][i + (k + 1) * size] != -128,
							i != 0 && model.getModel()[j][(i - 1) + k * size] != -128,
							k != 0 && model.getModel()[j][i + (k - 1) * size] != -128,
							j != (size - 1) && model.getModel()[j + 1][i + k * size] != -128,
							j != 0 && model.getModel()[j - 1][i + k * size] != -128
						);
						int color = palette.getColors()[model.getModel()[j][i + k * size] + 128];
						tris += Bakery.coloredCube(i + getOffsetX(), j + getOffsetY(), k + getOffsetZ(), 1, 1, 1, color, flags);
					}
				}
			}
		}

		return tris;
	}

	protected int getOffsetX()
	{
		return 8 - size / 2;
	}

	protected int getOffsetY()
	{
		return 0;
	}

	protected int getOffsetZ()
	{
		return 8 - size / 2;
	}
}
