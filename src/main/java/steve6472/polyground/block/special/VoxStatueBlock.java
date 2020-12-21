package steve6472.polyground.block.special;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.json.JSONObject;
import steve6472.polyground.CaveGame;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.gfx.Palette;
import steve6472.polyground.gfx.VoxModel;
import steve6472.polyground.registry.palette.PaletteRegistry;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.12.2020
 * Project: CaveGame
 *
 ***********************/
public class VoxStatueBlock extends CustomBlock
{
	private static final ModelBuilder modelBuilder = new ModelBuilder();

	private List<Vector3f> vert;
	private List<Vector4f> col;
	private List<Vector2f> text;
	private List<Vector3f> norm;
	private int tris;

	public VoxStatueBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public void load(JSONObject json)
	{
		Palette palette;
		VoxModel model;

		model = new VoxModel(new File("custom_models/vox/" + json.getString("path") + ".vox"));
		model.reloadModel();
		palette = PaletteRegistry.getOrRegister(json.getString("palette"));
		palette.reloadPalette();
		getDefaultState().getBlockModels()[0].createModel(CaveGame.getInstance().mainRender.buildHelper);

		load(model, palette, model.getWidth());
	}

	public void load(VoxModel model, Palette palette, int size)
	{
		tris = 0;
		vert = new ArrayList<>();
		col = new ArrayList<>();
		text = new ArrayList<>();
		norm = new ArrayList<>();

		modelBuilder.load(vert, col, text, norm);
		Bakery.tempBuilder(modelBuilder);

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
						tris += Bakery.coloredCube(i + (8 - size / 2), j, k + (8 - size / 2), 1, 1, 1, color, flags);
					}
				}
			}
		}

		modelBuilder.load(null, null, null, null);

		Bakery.worldBuilder();
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		if (modelLayer != ModelLayer.NORMAL)
			return 0;

		buildHelper.setSubChunk(world.getSubChunkFromBlockCoords(x, y, z));
		for (Vector3f v : vert)
		{
			buildHelper.getVert().add(new Vector3f(v).add(x, y, z));
		}
		buildHelper.getCol().addAll(col);
		buildHelper.getText().addAll(text);
		buildHelper.getNorm().addAll(norm);

		return tris;
	}
}
