package com.steve6472.polyground.block.special;

import com.steve6472.polyground.BasicEvents;
import com.steve6472.polyground.EnumFace;
import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.blockdata.BlockData;
import com.steve6472.polyground.entity.EntityBase;
import com.steve6472.polyground.entity.registry.EntityRegistry;
import com.steve6472.polyground.world.SubChunk;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.09.2019
 * Project: SJP
 *
 ***********************/
public class GravelBlock extends Block
{
	public GravelBlock(File f, int id)
	{
		super(f, id);
	}

	@Override
	public void onUpdate(SubChunk subChunk, BlockData blockData, EnumFace updateFrom, int x, int y, int z)
	{
		super.onUpdate(subChunk, blockData, updateFrom, x, y, z);
		if (y <= 0) return;

		if (subChunk.getWorld().getBlock(x, y - 1, z) == Block.air)
		{
			subChunk.getWorld().setBlock(x, y, z, Block.air);
			BasicEvents.updateAll(x, y, z);

			EntityBase e = EntityRegistry.fallingBlock.createNew();
			e.setPosition(x + 0.5f, y + 0.5f, z + 0.5f);

			subChunk.getWorld().addEntity(e);
		}
	}
}
