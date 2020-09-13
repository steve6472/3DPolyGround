package steve6472.polyground.entity.item;

import steve6472.polyground.item.Item;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.09.2020
 * Project: CaveGame
 *
 ***********************/
public class ItemStack
{
	private final Item item;
	private int count;

	public ItemStack(Item item)
	{
		this.item = item;
	}
}
