package steve6472.polyground.events;

import steve6472.sge.main.events.AbstractEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.10.2019
 * Project: SJP
 *
 ***********************/
public abstract class CancellableEvent extends AbstractEvent
{
	private boolean isCancelled;

	public void cancel()
	{
		isCancelled = true;
	}

	public boolean isCancelled()
	{
		return isCancelled;
	}
}
