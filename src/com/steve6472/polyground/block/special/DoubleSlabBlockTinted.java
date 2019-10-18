package com.steve6472.polyground.block.special;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.08.2019
 * Project: SJP
 *
 ***********************/
public class DoubleSlabBlockTinted extends DoubleSlabBlock
{
	public DoubleSlabBlockTinted(File f, int id)
	{
		super(f, id);
	}

//	@Override
//	public int createModel(int x, int y, int z, SubChunk sc, BlockData blockData, BuildHelper buildHelper)
//	{
//		return TintedBlock.createTintedModel(getBlockModel(), buildHelper, x, y, z, sc, this);
//	}
}
