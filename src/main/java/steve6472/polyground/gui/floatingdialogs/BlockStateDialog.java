package steve6472.polyground.gui.floatingdialogs;

import steve6472.polyground.CaveGame;
import steve6472.sge.gfx.font.CustomChar;
import steve6472.sge.gui.components.Button;
import steve6472.sge.gui.floatingdialog.FloatingDialog;
import steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 21.07.2020
 * Project: CaveGame
 *
 ***********************/
public class BlockStateDialog extends FloatingDialog
{
	private boolean shouldBeRemoved;

	public BlockStateDialog(MainApp main)
	{
		super(main, 16 * 20, 9 * 20);
		setVisible(true);
	}

	@Override
	public void createGUI()
	{
		DialogUtil.createDialogBackground(this);

		Button close = new Button(CustomChar.CROSS);
		close.setLocation(getWidth() - 20, 0);
		close.setSize(20, 20);
		close.addClickEvent(c -> shouldBeRemoved = true);
		close.getScheme().setHoveredFontColor(0.8f, 0.1f, 0.1f);
		addComponent(close);
	}

	@Override
	public void guiTick()
	{
	}

	@Override
	public void render()
	{
		if(isActive())
			DialogUtil.renderCursor(getMainApp());
	}

	@Override
	public float getSizeX()
	{
		return 1 / 9f;
	}

	@Override
	public float getSizeY()
	{
		return 1 / 16f;
	}

	@Override
	public float getScaleModifier()
	{
		return 5f;
	}

	@Override
	public boolean repaint()
	{
		return true;
	}

	@Override
	public boolean isActive()
	{
		return CaveGame.getInstance().getPlayer().getPosition().distance(getPosition()) < 2f;
	}

	@Override
	public boolean shouldBeRemoved()
	{
		return shouldBeRemoved;
	}
}
