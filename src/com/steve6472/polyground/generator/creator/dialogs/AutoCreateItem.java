package com.steve6472.polyground.generator.creator.dialogs;

import com.steve6472.sge.gui.components.dialog.OkDialog;
import com.steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2019
 * Project: SJP
 *
 ***********************/
public class AutoCreateItem extends OkDialog
{
	public AutoCreateItem()
	{
		super(" ", "Quick Item Craft");
		width = 300;
		height = 300;
	}

	@Override
	public void init(MainApp main)
	{
		super.init(main);
		initCloseButton();


	}
}
