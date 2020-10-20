package steve6472.polyground.block.blockdata;

import net.querz.nbt.tag.CompoundTag;
import steve6472.polyground.registry.blockdata.BlockDataRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.10.2020
 * Project: CaveGame
 *
 ***********************/
public class PipeData extends BlockData
{
	@Override
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{

	}

	@Override
	public String getId()
	{
		return BlockDataRegistry.pipeData.id();
	}
}
