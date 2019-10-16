package com.steve6472.polyground.generator.special;

import com.steve6472.sss2.SSS;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.09.2019
 * Project: SJP
 *
 ***********************/
public class SlabSpecial implements ISpecial
{
	private final String top;
	private final String bottom;
	private final String both;

	public SlabSpecial(String top, String bottom, String both)
	{
		this.top = top;
		this.bottom = bottom;
		this.both = both;
	}

	@Override
	public void generate(SSS sss)
	{
		sss.add("top", top);
		sss.add("bottom", bottom);
		sss.add("both", both);
	}

	@Override
	public String getName()
	{
		return "slab";
	}
}
