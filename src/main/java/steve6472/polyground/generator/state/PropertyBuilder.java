package steve6472.polyground.generator.state;

import org.json.JSONObject;
import steve6472.polyground.block.properties.IProperty;

import java.util.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 03.07.2020
 * Project: CaveGame
 *
 ***********************/
public class PropertyBuilder
{
	Map<IProperty<?>, Comparable<?>> map;
	private int rotation;
	private final List<String> tags;

	public static PropertyBuilder create()
	{
		return new PropertyBuilder();
	}

	private PropertyBuilder()
	{
		map = new HashMap<>();
		tags = new ArrayList<>();
	}

	public <T extends Comparable<T>> T get(IProperty<T> property)
	{
		return (T) map.get(property);
	}

	public <T extends Comparable<T>> PropertyBuilder addProperty(IProperty<T> property, Comparable<T> value)
	{
		map.put(property, value);
		return this;
	}

	public PropertyBuilder rotation(int rotation)
	{
		this.rotation = rotation;
		return this;
	}

	public PropertyBuilder tag(String tag)
	{
		this.tags.add(tag);
		return this;
	}

	public PropertyBuilder tags(String... tags)
	{
		Collections.addAll(this.tags, tags);
		return this;
	}

	public List<String> getTags()
	{
		return tags;
	}

	public int getRotation()
	{
		return rotation;
	}

	public JSONObject build()
	{
		JSONObject state = new JSONObject();
		for (IProperty<?> property : map.keySet())
		{
			state.put(property.getName(), map.get(property));
		}
		return state;
	}

	@Override
	public String toString()
	{
		return "PropertyBuilder{" + "map=" + map + ", rotation=" + rotation + ", tags=" + tags + '}';
	}
}
