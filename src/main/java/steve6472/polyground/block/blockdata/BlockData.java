package steve6472.polyground.block.blockdata;

import net.querz.nbt.tag.CompoundTag;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.09.2019
 * Project: SJP
 *
 ***********************/
public abstract class BlockData
{
	public abstract CompoundTag write();

	public abstract void read(CompoundTag tag);

	public abstract String getId();
}
