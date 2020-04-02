package steve6472.polyground.generator.special;

import steve6472.SSS;

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
	public void generate(SSS sss)
	{
	}

	@Override
	public String getName()
	{
		return name;
	}
}
