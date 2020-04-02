package steve6472.polyground.events;

import steve6472.polyground.gui.InGameGui;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2019
 * Project: SJP
 *
 ***********************/
public class InGameGuiEvent extends CancellableEvent
{
	private InGameGui gui;

	public InGameGuiEvent(InGameGui gui)
	{
		this.gui = gui;
	}

	public InGameGui getGui()
	{
		return gui;
	}

	public static class PreRender extends InGameGuiEvent
	{
		public PreRender(InGameGui gui)
		{
			super(gui);
		}
	}

	public static class PostRender extends InGameGuiEvent
	{
		public PostRender(InGameGui gui)
		{
			super(gui);
		}
	}


	public static class PreTick extends InGameGuiEvent
	{
		public PreTick(InGameGui gui)
		{
			super(gui);
		}
	}

	public static class PostTick extends InGameGuiEvent
	{
		public PostTick(InGameGui gui)
		{
			super(gui);
		}
	}


	public static class PreRenderCrosshair extends InGameGuiEvent
	{
		public PreRenderCrosshair(InGameGui gui)
		{
			super(gui);
		}
	}

	public static class PostRenderCrosshair extends InGameGuiEvent
	{
		public PostRenderCrosshair(InGameGui gui)
		{
			super(gui);
		}
	}
}
