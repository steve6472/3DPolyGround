package com.steve6472.polyground.block.special;

import com.steve6472.polyground.CaveGame;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.block.blockdata.IBlockData;
import com.steve6472.polyground.block.blockdata.ScanBlockData;
import com.steve6472.polyground.world.chunk.SubChunk;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.09.2019
 * Project: SJP
 *
 ***********************/
public class ScanBlock extends Block implements IBlockData
{
	public ScanBlock(File f, int id)
	{
		super(f, id);
	}

	@Override
	public BlockData createNewBlockEntity()
	{
		return new ScanBlockData();
	}

	@Override
	public void tick(SubChunk subChunk, BlockData blockData, int x, int y, int z)
	{
		if (blockData instanceof ScanBlockData)
		{
			ScanBlockData scanBlockEntity = (ScanBlockData) blockData;
			scanBlockEntity.addTime();
			CaveGame.getInstance().particles.addBasicTickParticle(x + 0.5f, y + 1.5f, z + 0.5f, scanBlockEntity.getTime() / 2f, 0, 2, 2, 1);
		}
	}

	@Override
	public boolean isTickable()
	{
		return true;
	}
}
