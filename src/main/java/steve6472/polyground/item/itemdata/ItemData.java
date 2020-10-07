package steve6472.polyground.item.itemdata;

import net.querz.nbt.tag.CompoundTag;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.10.2020
 * Project: CaveGame
 *
 ***********************/
public abstract class ItemData
{
	public abstract CompoundTag write();

	public abstract void read(CompoundTag tag);

	public abstract String getId();
}
