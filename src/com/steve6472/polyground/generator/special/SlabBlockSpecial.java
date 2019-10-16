package com.steve6472.polyground.generator.special;

import com.steve6472.sss2.SSS;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 25.09.2019
 * Project: SJP
 *
 ***********************/
public class SlabBlockSpecial implements ISpecial
{
	private final String type;

	public SlabBlockSpecial(String type)
	{
		this.type = type;
	}

	@Override
	public void generate(SSS sss)
	{
		sss.add("type", type);
	}

	@Override
	public String getName()
	{
		return "slab";
	}
}
