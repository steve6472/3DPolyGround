package steve6472.polyground.generator.special;

import steve6472.SSS;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.09.2019
 * Project: SJP
 *
 ***********************/
public class DoubleSlabSpecial implements ISpecial
{
	private final String top;
	private final String bottom;

	public DoubleSlabSpecial(String top, String bottom)
	{
		this.top = top;
		this.bottom = bottom;
	}

	@Override
	public void generate(SSS sss)
	{
		sss.add("top", top);
		sss.add("bottom", bottom);
	}

	@Override
	public String getName()
	{
		return "double_slab";
	}
}
