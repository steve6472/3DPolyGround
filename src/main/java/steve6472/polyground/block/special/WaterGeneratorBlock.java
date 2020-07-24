package steve6472.polyground.block.special;

import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.World;

import java.io.File;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.04.2020
 * Project: SJP
 *
 ***********************/
public class WaterGeneratorBlock extends Block
{
	public WaterGeneratorBlock(File f)
	{
		super(f);
		isFull = false;
	}

	@Override
	public void tick(World world, BlockState state, int x, int y, int z)
	{
//		subChunk.setLiquidVolumeEfficiently(x, y + 1, z, subChunk.getLiquidVolumeEfficiently(x, y + 1, z) + 1000.0 / 60.0);
		world.setLiquidVolume(x, y + 1, z, world.getLiquidVolume(x, y + 1, z) + 10000.0);
//		subChunk.setLiquidVolumeEfficiently(x, y + 1, z, 100000);
	}

	@Override
	public boolean isTickable()
	{
		return true;
	}
}
