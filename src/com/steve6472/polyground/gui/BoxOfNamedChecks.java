package com.steve6472.polyground.gui;

import com.steve6472.sge.gui.components.NamedCheckBox;
import com.steve6472.sge.main.events.Event;

import java.util.function.Supplier;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.12.2019
 * Project: SJP
 *
 ***********************/
public class BoxOfNamedChecks extends NamedCheckBox
{
	Supplier<Boolean> sup;

	@Event
	public void show(ShowEvent e)
	{
		setToggled(sup.get());
	}
}