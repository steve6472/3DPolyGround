package steve6472.polyground.registry.itemdata;

import steve6472.polyground.item.itemdata.ItemData;

public record ItemDataEntry<T extends ItemData>(IItemDataFactory<T> factory, String id)
{
	public T createNew()
	{
		return factory.create();
	}

	public String getId()
	{
		return id;
	}
}