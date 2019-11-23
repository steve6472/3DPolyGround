package com.steve6472.polyground.block.special;

import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.world.BuildHelper;
import com.steve6472.polyground.world.chunk.ModelLayer;
import com.steve6472.polyground.world.chunk.SubChunk;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.08.2019
 * Project: SJP
 *
 ***********************/
public class TransparentBlock extends Block
{
	public TransparentBlock(File f, int id)
	{
		super(f, id);
	}

	@Override
	public int createModel(int x, int y, int z, SubChunk sc, BlockData blockData, BuildHelper buildHelper, ModelLayer modelLayer)
	{
		int tris = 0;

		buildHelper.setSubChunk(sc);
		for (Cube c : getCubes(x, y, z))
		{
			buildHelper.setCube(c);
			if (cull(sc, x, y + 1, z)) tris += buildHelper.topFace();
			if (cull(sc, x, y - 1, z)) tris += buildHelper.bottomFace();
			if (cull(sc, x + 1, y, z)) tris += buildHelper.positiveXFace();
			if (cull(sc, x - 1, y, z)) tris += buildHelper.negativeXFace();
			if (cull(sc, x, y, z + 1)) tris += buildHelper.positiveZFace();
			if (cull(sc, x, y, z - 1)) tris += buildHelper.negativeZFace();
		}

		return tris;
	}

	public static boolean cull(SubChunk sc, int x, int y, int z)
	{
		if (y < 0 || y >= 16)
		{
			if (y < 0 && sc.getLayer() == 0)
				return true;
			else if (y >= 16 && sc.getLayer() >= sc.getParent().getSubChunks().length - 1)
				return true;
			else if (y < 0)
				return cull(sc.getParent().getSubChunk(sc.getLayer() - 1), x, 15, z);
			else
				return cull(sc.getParent().getSubChunk(sc.getLayer() + 1), x, 0, z);
		}
		else if (x < 0 || z < 0 || z >= 16 || x >= 16)
			return true;
		else
		{
			Block b = BlockRegistry.getBlockById(sc.getIds()[x][y][z]);
			return b == Block.air || !b.isFull;
		}
	}
}
