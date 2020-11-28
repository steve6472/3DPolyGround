package steve6472.polyground.events;

import steve6472.polyground.gfx.stack.LineTess;
import steve6472.sge.main.events.AbstractEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.11.2020
 * Project: CaveGame
 *
 ***********************/
public class TessTestEvent extends AbstractEvent
{
	private final LineTess tess;

	public TessTestEvent(LineTess tess)
	{
		this.tess = tess;
	}

	public LineTess getTess()
	{
		return tess;
	}
}
