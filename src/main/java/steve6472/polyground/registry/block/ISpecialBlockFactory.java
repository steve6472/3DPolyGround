package steve6472.polyground.registry.block;

import org.json.JSONObject;
import steve6472.polyground.block.Block;

@FunctionalInterface
public interface ISpecialBlockFactory<T extends Block>
{
	T create(JSONObject json);
}