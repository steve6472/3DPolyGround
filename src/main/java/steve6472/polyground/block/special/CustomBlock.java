package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.08.2019
 * Project: SJP
 *
 ***********************/
public class CustomBlock extends TransparentBlock
{
	public CustomBlock(JSONObject json)
	{
		super(json);
		isFull = false;
	}

	public static int model(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		int tris = 0;
		BlockModel model = state.getBlockModel(world, x, y, z);

		buildHelper.setSubChunk(world.getSubChunkFromBlockCoords(x, y, z));

		if (model.getElements() != null)
		{
			for (IElement c : model.getElements())
			{
				tris += c.build(buildHelper, modelLayer, null, state, x, y, z);
			}
		}

		return tris;
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		return model(x, y, z, world, state, buildHelper, modelLayer);
	}
}
