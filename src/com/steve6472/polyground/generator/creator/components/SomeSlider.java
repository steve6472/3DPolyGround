package com.steve6472.polyground.generator.creator.components;

import com.steve6472.sge.gfx.font.Font;
import com.steve6472.sge.gui.components.Slider;
import com.steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 13.10.2019
 * Project: SJP
 *
 ***********************/
public class SomeSlider extends Slider
{
	@Override
	public void init(MainApp main)
	{
		super.init(main);

		setSize(88, 12);
		setButtonSize(6, 20);
	}

	@Override
	public void render()
	{
		super.render();

		Font.render("" + getIValue(), Font.stringCenterX(getX() + getWidth() + 1, 21, "" + getIValue(), 1), getY() + 2);
	}
}


