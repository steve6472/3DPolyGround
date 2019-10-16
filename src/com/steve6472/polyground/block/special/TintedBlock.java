package com.steve6472.polyground.block.special;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.block.model.BlockModel;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.model.registry.TintedCube;
import com.steve6472.polyground.world.BuildHelper;
import com.steve6472.polyground.world.Cull;
import com.steve6472.polyground.world.SubChunk;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.08.2019
 * Project: SJP
 *
 ***********************/
public class TintedBlock extends Block
{
	public TintedBlock(File f, int id)
	{
		super(f, id);
	}

	@Override
	public int createModel(int x, int y, int z, SubChunk sc, BlockData blockData, BuildHelper buildHelper)
	{
		return createTintedModel(getBlockModel(), buildHelper, x, y, z, sc, this);
	}

	public static int createTintedModel(BlockModel blockModel, BuildHelper buildHelper, int x, int y, int z, SubChunk sc, Block thisBlock)
	{
		int tris = 0;

		for (Cube c : blockModel.getCubes())
		{
			buildHelper.setCube(c);

			for (EnumFace face : EnumFace.getFaces())
			{
				if (Cull.renderFace(x, y, z, face, thisBlock, sc))
				{
					tris += buildHelper.face(face);

					if (c instanceof TintedCube)
					{
						TintedCube tc = (TintedCube) c;
						if (tc.getFace(face) != null)
							recolor(buildHelper, tc.getFace(face).getShade(), tc);
					}
				}
			}
		}

		return tris;
	}

	private static void recolor(BuildHelper buildHelper, float shade, TintedCube cube)
	{
		for (int j = 0; j < 24; j++)
		{
			buildHelper.getCol().remove(buildHelper.getCol().size() - 1);
		}
		for (int j = 0; j < 6; j++)
		{
			shade(buildHelper, cube.red, cube.green, cube.blue, shade);
		}
	}

	private static void shade(BuildHelper buildHelper, float r, float g, float b, float shade)
	{
		buildHelper.getCol().add(r * shade);
		buildHelper.getCol().add(g * shade);
		buildHelper.getCol().add(b * shade);
		buildHelper.getCol().add(1.0f);
	}
}
