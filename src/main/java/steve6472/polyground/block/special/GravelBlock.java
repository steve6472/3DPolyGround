package steve6472.polyground.block.special;

import steve6472.polyground.BasicEvents;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.entity.EntityBase;
import steve6472.polyground.entity.registry.EntityRegistry;
import steve6472.polyground.world.chunk.SubChunk;

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
		if (y <= 0)
			return;

		if (subChunk.getBlockEfficiently(x, y - 1, z) == Block.air)
		{
			subChunk.setBlock(x, y, z, Block.air);
			subChunk.rebuild();
			BasicEvents.updateAll(subChunk, x + subChunk.getX() * 16, y + subChunk.getLayer() * 16, z + subChunk.getZ() * 16);

			EntityBase e = EntityRegistry.fallingBlock.createNew();
			e.setPosition(x + 0.5f + subChunk.getX() * 16, y + 0.5f + subChunk.getLayer() * 16, z + 0.5f + subChunk.getZ() * 16);

			subChunk.getWorld().addEntity(e);
		}
	}
}
