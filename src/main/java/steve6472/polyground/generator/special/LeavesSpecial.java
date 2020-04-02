package steve6472.polyground.generator.special;

import steve6472.SSS;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.09.2019
 * Project: SJP
 *
 ***********************/
public class LeavesSpecial implements ISpecial
{
	private final float r, g, b;

	public LeavesSpecial(float r, float g, float b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
	}

	@Override
	public void generate(SSS sss)
	{
		sss.add("red", r);
		sss.add("green", g);
		sss.add("blue", b);
	}

	@Override
	public String getName()
	{
		return "leaves";
	}
}
