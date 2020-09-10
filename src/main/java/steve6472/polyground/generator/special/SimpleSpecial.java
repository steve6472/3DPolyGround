package steve6472.polyground.generator.special;

import org.json.JSONObject;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.09.2019
 * Project: SJP
 *
 ***********************/
public class SimpleSpecial implements ISpecial
{
	String name;

	public SimpleSpecial(String name)
	{
		this.name = name;
	}

	@Override
	public JSONObject generate()
	{
		return new JSONObject();
	}

	@Override
	public String getName()
	{
		return name;
	}
}
