package steve6472.polyground.registry.specialitem;

import org.json.JSONObject;
import steve6472.polyground.item.Item;

@FunctionalInterface
public interface ISpecialItemFactory<T extends Item>
{
	T create(JSONObject json, int id);
}