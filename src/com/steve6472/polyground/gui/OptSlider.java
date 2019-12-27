package com.steve6472.polyground.gui;

import com.steve6472.sge.gui.components.Slider;
import com.steve6472.sge.main.events.Event;

import java.util.function.Supplier;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.12.2019
 * Project: SJP
 *
 ***********************/
public class OptSlider extends Slider
{
	Supplier<Float> sup;

	@Event
	public void show(ShowEvent e)
	{
		setValue(sup.get());
	}
}
