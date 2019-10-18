package com.steve6472.polyground.block.special;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 15.09.2019
 * Project: SJP
 *
 ***********************/
public class SlabBlockTinted extends SlabBlock
{
	public SlabBlockTinted(File f, int id)
	{
		super(f, id);
		isFull = false;
	}

//	@Override
//	public int createModel(int x, int y, int z, SubChunk sc, BlockData blockData, BuildHelper buildHelper)
//	{
//		return TintedBlock.createTintedModel(getBlockModel(), buildHelper, x, y, z, sc, this);
//	}
}
