package steve6472.polyground.generator.cond;

import steve6472.sge.gui.components.Button;
import steve6472.sge.gui.components.dialog.YesNoDialog;
import steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 05.04.2020
 * Project: CaveGame
 *
 ***********************/
public class NewOrExistingDialog extends YesNoDialog
{
	public NewOrExistingDialog()
	{
		super("Do you want to create\nnew file or select existing?", "New or Existing");
	}

	@Override
	public void init(MainApp main)
	{
		super.init(main);
		((Button) getComponent(0)).setText("New");
		((Button) getComponent(1)).setText("Existing");
	}
}
