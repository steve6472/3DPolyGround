package steve6472.polyground.registry.block;

import org.json.JSONObject;
import steve6472.polyground.block.Block;

public class SpecialBlockEntry<T extends Block>
{
	private ISpecialBlockFactory<T> factory;

	SpecialBlockEntry(ISpecialBlockFactory<T> factory)
	{
		this.factory = factory;
	}

	public T createNew(JSONObject json)
	{
		return factory.create(json);
	}
}