package steve6472.polyground.generator.special;

import steve6472.SSS;
import steve6472.ValueHolder;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.07.2020
 * Project: CaveGame
 *
 ***********************/
public class SpecialBuilder implements ISpecial
{
	private final String name;
	private final List<ValueHolder> values;

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
		values = new ArrayList<>();
	}

	public SpecialBuilder addValue(String name, Object value)
	{
		values.add(new ValueHolder(name, value));
		return this;
	}

	@Override
	public void generate(SSS sss)
	{
		for (ValueHolder v : values)
		{
			sss.add(v.name, v.value);
		}
	}

	@Override
	public String getName()
	{
		return name;
	}
}
