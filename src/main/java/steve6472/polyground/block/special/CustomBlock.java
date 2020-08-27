package steve6472.polyground.block.special;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.model.BlockModel;
import steve6472.polyground.block.model.Cube;
import steve6472.polyground.block.model.IElement;
import steve6472.polyground.block.model.faceProperty.LayerFaceProperty;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.08.2019
 * Project: SJP
 *
 ***********************/
public class CustomBlock extends TransparentBlock
{
	public CustomBlock(File f)
	{
		super(f);
		isFull = false;
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		int tris = 0;
		BlockModel model = state.getBlockModel(world, x, y, z);

		buildHelper.setSubChunk(world.getSubChunkFromBlockCoords(x, y, z));
		for (Cube c : model.getCubes())
		{
			buildHelper.setCube(c);

			for (EnumFace face : EnumFace.getFaces())
			{
				if (LayerFaceProperty.getModelLayer(c.getFace(face)) == modelLayer)
					if (c.getFace(face) != null)
						tris += buildHelper.face(face);
			}
		}

		if (model.getElements() != null)
		{
			for (IElement c : model.getElements())
			{
				tris += c.build(buildHelper, modelLayer, world, state, x, y, z);
			}
		}

		return tris;
	}
}
