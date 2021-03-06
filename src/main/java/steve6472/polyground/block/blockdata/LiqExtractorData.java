package steve6472.polyground.block.blockdata;

import net.querz.nbt.tag.CompoundTag;
import steve6472.polyground.registry.blockdata.BlockDataRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.10.2020
 * Project: CaveGame
 *
 ***********************/
public class LiqExtractorData extends BlockData
{
	public int amount;

	@Override
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		tag.putInt("amount", amount);
		return tag;
	}

	@Override
	public void read(CompoundTag tag)
	{
		this.amount = tag.getInt("amount");
	}

	@Override
	public String getId()
	{
		return BlockDataRegistry.liqExtractor.id();
	}
}
