package steve6472.polyground.generator.creator.components;

import steve6472.polyground.generator.creator.BlockCreatorGui;
import steve6472.polyground.generator.creator.dialogs.EditCubeDialog;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.gui.Component;
import steve6472.sge.gui.components.Button;
import steve6472.sge.gui.components.ToggleButton;
import steve6472.sge.gui.components.dialog.YesNoDialog;
import steve6472.sge.main.MainApp;
import steve6472.sge.main.events.Event;
import steve6472.sge.main.events.ScrollEvent;
import steve6472.sge.test.Fex;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.12.2019
 * Project: SJP
 *
 ***********************/
public class CubeList extends Component
{
	private List<String> items;
	private int scroll;
	private int selected;
	private boolean clickFlag;

	private Button addCube, copyCube, settings, deleteCube;
	private ToggleButton hasHitbox, hasCollisionBox;

	private BlockCreatorGui creatorGui;

	public CubeList(BlockCreatorGui creatorGui)
	{
		this.creatorGui = creatorGui;
		this.items = new ArrayList<>();
		selected = -1;
	}

	@Override
	public void init(MainApp main)
	{
		addCube = new ImageButton(EnumSizeType.BIG, 0, 0, 0, 1);
		addCube.setRelativeLocation(0, getHeight() + 10);
		addCube.addClickEvent(this::addCubeClick);
		addComponent(addCube);

		copyCube = new ImageButton(EnumSizeType.BIG, 1, 0, 1, 1);
		copyCube.setRelativeLocation(0, getHeight() + 55);
//		copyCube.addClickEvent(this::copyCubeClick);
		addComponent(copyCube);

		hasHitbox = new ImageToggleButton(EnumSizeType.BIG, 1, 2, 1, 3);
		hasHitbox.setRelativeLocation(45, getHeight() + 10);
		hasHitbox.addChangeEvent(this::hasHitboxChange);
		addComponent(hasHitbox);

		hasCollisionBox = new ImageToggleButton(EnumSizeType.BIG, 2, 2, 2, 3);
		hasCollisionBox.setRelativeLocation(45, getHeight() + 55);
		hasCollisionBox.addChangeEvent(this::hasCollisionBoxChange);
		addComponent(hasCollisionBox);

		settings = new ImageButton(EnumSizeType.BIG, 0, 4, 0, 5);
		settings.setRelativeLocation(90, getHeight() + 10);
		settings.addClickEvent(this::settingsClick);
		addComponent(settings);

		deleteCube = new ImageButton(EnumSizeType.BIG, 2, 0, 2, 1);
		deleteCube.setRelativeLocation(90, getHeight() + 55);
		deleteCube.addClickEvent(this::deleteCubeClick);
		addComponent(deleteCube);
	}

	@Override
	public void tick()
	{
		if (!clickFlag)
		{
			for (int i = 0; i < items.size(); i++)
			{
				if (isCursorInComponent(x + 4, y + 2 + i * 14 + scroll * 14, getWidth() - 4, 13))
				{
					if (isLMBHolded())
					{
						clickFlag = true;
						if (selected == i)
						{
							selected = -1;
						} else
						{
							selected = i;
						}
						updateButtons();
					}
				}
			}
		}

		if (clickFlag && !isLMBHolded())
		{
			clickFlag = false;
		}
	}

	@Override
	public void render()
	{
		SpriteRender.renderSingleBorderComponent(this, 0, 0, 0, 1, Fex.H80, Fex.H80, Fex.H80, Fex.Hff);

		for (int i = 0; i < items.size(); i++)
		{
			if (isCursorInComponent(x + 2, y + 4 + i * 14 + scroll * 14, getWidth() - 4, 13))
			{
				SpriteRender.fillRect(x + 2, y + 4 + i * 14 + scroll * 14, getWidth() - 4, 13, 0, 0, 0, 0.2f);
			}
		}

		for (int i = 0; i < items.size(); i++)
		{
			if (selected == i)
				SpriteRender.fillRect(x + 2, y + 4 + i * 14 + scroll * 14, getWidth() - 4, 13, 0, 0, 0, 0.2f);

			Font.render(items.get(i), x + 5, y + 7 + i * 14 + scroll * 14);
		}
	}

	@Event
	public void scroll(ScrollEvent e)
	{
		if (!isCursorInComponent())
			return;
		scroll -= (int) e.getyOffset();

		for (Component c : getComponents())
		{
			if (c instanceof Button)
			{
				((Button) c).setEnabled(true);
			}
		}

		if (scroll < 0)
			scroll = 0;
	}

	public void clear()
	{
		items.clear();
	}

	public void addItem(String name)
	{
		items.add(name);
	}

	public int getSelectedIndex()
	{
		return selected;
	}

	public void updateButtons()
	{
		boolean isCubeSelected = selected != -1;
		boolean isBlockSelected = false;

		addCube.setEnabled(isBlockSelected);

		copyCube.setEnabled(isCubeSelected);
		settings.setEnabled(isCubeSelected);
		hasHitbox.setEnabled(isCubeSelected);
		deleteCube.setEnabled(isCubeSelected);
		hasCollisionBox.setEnabled(isCubeSelected);

//		if (isCubeSelected)
//		{
//			Cube selectedCube = getSelectedCube();
//
//			hasHitbox.setToggled(selectedCube.isHitbox());
//			hasCollisionBox.setToggled(selectedCube.isCollisionBox());
//		}
	}

	public void refreshList()
	{
//		BlockEntry entry = creatorGui.blocks.get(creatorGui.getSelectedBlock().getName());
//
//		clear();
//
//		for (int i = 0; i < entry.getModel().getCubes().length; i++)
//		{
//			addItem(((ICreatorCube) entry.getModel().getCube(i)).getName());
//		}
	}

	/* Button Click Event */

	private void hasHitboxChange(ToggleButton b)
	{
//		getSelectedCube().setHitbox(b.isToggled());
	}

	private void hasCollisionBoxChange(ToggleButton b)
	{
//		getSelectedCube().setCollisionBox(b.isToggled());
	}

	private void settingsClick(Button b)
	{
		EditCubeDialog editCube;
		getMain().showDialog(editCube = new EditCubeDialog(creatorGui));

//		editCube.addOkClickEvent(a -> creatorGui.autoUV());

		editCube.center();
	}

	private void addCubeClick(Button b)
	{
//		BlockEntry entry = creatorGui.getSelectedBlock();
//		entry.getModel().addCube(BlockEntry.createEmptyCube());

		refreshList();
		selected = items.size() - 1;
		updateButtons();
	}

	private void deleteCubeClick(Button b)
	{
		YesNoDialog yn = new YesNoDialog("Are you sure ?", "Delete Cube");
		getMain().showDialog(yn).center();
		yn.addYesClickEvent(y ->
		{
			items.remove(selected);
//			creatorGui.getSelectedBlock().getModel().removeCube(getSelectedCube());
			selected = -1;
			updateButtons();
		});

		updateButtons();
	}
}
