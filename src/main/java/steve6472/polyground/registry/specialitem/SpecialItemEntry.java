package steve6472.polyground.registry.specialitem;

import org.json.JSONObject;
import steve6472.polyground.item.Item;

public class SpecialItemEntry<T extends Item>
{
	private ISpecialItemFactory<T> factory;

	SpecialItemEntry(ISpecialItemFactory<T> factory)
	{
		this.factory = factory;
	}

	public T createNew(JSONObject json, int id)
	{
		return factory.create(json, id);
	}
}