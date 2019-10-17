package com.steve6472.polyground.block.special;

import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.world.BuildHelper;
import com.steve6472.polyground.world.SubChunk;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.08.2019
 * Project: SJP
 *
 ***********************/
public class CustomBlock extends TransparentBlock
{
	public CustomBlock(File f, int id)
	{
		super(f, id);
	}

	@Override
	public int createModel(int x, int y, int z, SubChunk sc, BlockData blockData, BuildHelper buildHelper)
	{
		int tris = 0;

		for (Cube c : getCubes(x, y, z))
		{
			buildHelper.setCube(c);
			tris += buildHelper.topFace();
			tris += buildHelper.bottomFace();
			tris += buildHelper.positiveXFace();
			tris += buildHelper.negativeXFace();
			tris += buildHelper.positiveZFace();
			tris += buildHelper.negativeZFace();
		}

		return tris;
	}
}
