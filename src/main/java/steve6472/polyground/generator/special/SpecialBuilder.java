package steve6472.polyground.generator.special;

import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.07.2020
 * Project: CaveGame
 *
 ***********************/
public class SpecialBuilder implements ISpecial
{
	private final String name;
	private final JSONObject json;

	public static SpecialBuilder create(String name)
	{
		return new SpecialBuilder(name);
	}

	public SpecialBuilder build()
	{
		return this;
	}

	private SpecialBuilder(String name)
	{
		this.name = name;
		json = new JSONObject();
	}

	public SpecialBuilder addValue(String name, Object value)
	{
		json.put(name, value);
		return this;
	}

	@Override
	public JSONObject generate()
	{
		return json.put("name", name);
	}

	@Override
	public String getName()
	{
		return name;
	}
}
