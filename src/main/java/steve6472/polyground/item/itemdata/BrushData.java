package steve6472.polyground.item.itemdata;

import net.querz.nbt.tag.CompoundTag;
import steve6472.polyground.registry.itemdata.ItemDataRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.10.2020
 * Project: CaveGame
 *
 ***********************/
public class BrushData extends ItemData
{
	public int color;

	@Override
	public CompoundTag write()
	{
		CompoundTag tag = new CompoundTag();
		tag.putInt("color", color);
		return null;
	}

	@Override
	public void read(CompoundTag tag)
	{
		this.color = tag.getInt("color");
	}

	@Override
	public String getId()
	{
		return ItemDataRegistry.brushData.getId();
	}
}
