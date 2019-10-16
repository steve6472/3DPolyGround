package com.steve6472.polyground.block.special;

import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.block.model.registry.Cube;
import com.steve6472.polyground.world.BuildHelper;
import com.steve6472.polyground.world.Cull;
import com.steve6472.polyground.world.SubChunk;
import com.steve6472.sss2.SSS;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.09.2019
 * Project: SJP
 *
 ***********************/
public class SlabBlock extends Block
{
	private File f;

	public EnumSlabType slabType;

	public SlabBlock(File f, int id)
	{
		super(f, id);
		this.f = f;
		isFull = false;
	}

	@Override
	public void postLoad()
	{
		if (f.isFile())
		{
			SSS sss = new SSS(f);
			slabType = switch (sss.getString("type"))
				{
					case "top" -> EnumSlabType.TOP;
					case "bottom" -> EnumSlabType.BOTTOM;
					default -> throw new IllegalStateException("Unexpected value: " + sss.getString("type"));
				};
		}
	}

	@Override
	public int createModel(int x, int y, int z, SubChunk sc, BlockData blockData, BuildHelper buildHelper)
	{
		int tris = 0;

		for (Cube c : blockModel.getCubes())
		{
			buildHelper.setCube(c);
			for (EnumFace face : EnumFace.getFaces())
			{
				if (Cull.renderFace(x, y, z, face, this, sc))
				{
					tris += buildHelper.face(face);
				}
			}
		}

		return tris;
	}

	public enum EnumSlabType
	{
		TOP, BOTTOM
	}
}
