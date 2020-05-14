package steve6472.polyground.block.special;

import steve6472.polyground.block.Block;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.world.chunk.SubChunk;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.04.2020
 * Project: SJP
 *
 ***********************/
public class WaterGeneratorBlock extends Block
{
	public WaterGeneratorBlock(File f, int id)
	{
		super(f, id);
		isFull = false;
	}

	@Override
	public void tick(SubChunk subChunk, BlockData blockData, int x, int y, int z)
	{
//		subChunk.setLiquidVolumeEfficiently(x, y + 1, z, subChunk.getLiquidVolumeEfficiently(x, y + 1, z) + 1000.0 / 60.0);
		subChunk.setLiquidVolumeEfficiently(x, y + 1, z, subChunk.getLiquidVolumeEfficiently(x, y + 1, z) + 10000.0);
//		subChunk.setLiquidVolumeEfficiently(x, y + 1, z, 100000);
	}

	@Override
	public boolean isTickable()
	{
		return true;
	}
}
