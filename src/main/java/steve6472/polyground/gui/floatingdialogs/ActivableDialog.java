package steve6472.polyground.gui.floatingdialogs;

import steve6472.polyground.CaveGame;
import steve6472.sge.gui.floatingdialog.FloatingDialog;
import steve6472.sge.main.KeyList;
import steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 24.07.2020
 * Project: CaveGame
 *
 ***********************/
public abstract class ActivableDialog extends FloatingDialog
{
	private boolean active = false;

	public ActivableDialog(MainApp main, int width, int height)
	{
		super(main, width, height);
	}

	@Override
	public void nonactiveTick()
	{
		if (!active && getMainApp().isKeyPressed(KeyList.E) && CaveGame.getInstance().getPlayer().getPosition().distance(getPosition()) <= 2)
		{
			active = true;
		}
	}
}
