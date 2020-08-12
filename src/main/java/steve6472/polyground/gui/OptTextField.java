package steve6472.polyground.gui;

import steve6472.sge.gui.components.TextField;
import steve6472.sge.main.events.Event;

import java.util.function.Supplier;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.12.2019
 * Project: SJP
 *
 ***********************/
public class OptTextField extends TextField
{
	Supplier<String> sup;

	@Event
	public void show(ShowEvent e)
	{
		setText(sup.get());
	}
}