package steve6472.polyground.generator.state;

import org.json.JSONObject;
import steve6472.polyground.block.properties.IProperty;

import java.util.HashMap;
import java.util.Map;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 03.07.2020
 * Project: CaveGame
 *
 ***********************/
public class PropertyBuilder
{
	Map<IProperty<?>, Comparable<?>> map;

	public static PropertyBuilder create()
	{
		return new PropertyBuilder();
	}

	private PropertyBuilder()
	{
		map = new HashMap<>();
	}

	public <T extends Comparable<T>> PropertyBuilder addProperty(IProperty<T> property, Comparable<T> value)
	{
		map.put(property, value);
		return this;
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
}
