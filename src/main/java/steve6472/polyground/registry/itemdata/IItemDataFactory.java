package steve6472.polyground.registry.itemdata;

import steve6472.polyground.item.itemdata.ItemData;

@FunctionalInterface
public interface IItemDataFactory<T extends ItemData>
{
	T create();
}