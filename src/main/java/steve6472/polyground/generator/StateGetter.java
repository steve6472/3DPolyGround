package steve6472.polyground.generator;

import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.generator.state.PropertyBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * This version of StateGetter is not sort dependent.
 * TODO: Maybe replace StateFinder with this?
 */
class StateGetter
{
	private final DataBuilder dataBuilder;
	private final List<PropertyBuilder> propertyBuilders;

	StateGetter(DataBuilder dataBuilder)
	{
		this.dataBuilder = dataBuilder;
		propertyBuilders = new ArrayList<>();
		for (int i = 0; i < dataBuilder.blockState.getModels().size(); i++)
		{
			propertyBuilders.add(dataBuilder.blockState.getModels().get(i).getA());
		}
		System.out.println(propertyBuilders);
	}

	public <T extends Comparable<T>, V extends T> StateGetter with(IProperty<T> property, V value)
	{
		propertyBuilders.removeIf(pb -> pb.get(property) != value);
		return this;
	}

	public StateGetter addTag(String tag)
	{
		propertyBuilders.get(0).tag(tag);
		return this;
	}

	public StateGetter addTags(String... tags)
	{
		propertyBuilders.get(0).tags(tags);
		return this;
	}

	public DataBuilder finish()
	{
		return dataBuilder;
	}

	public DataBuilder finish(String tag)
	{
		return addTag(tag).finish();
	}

	public DataBuilder finish(String... tags)
	{
		return addTags(tags).finish();
	}
}