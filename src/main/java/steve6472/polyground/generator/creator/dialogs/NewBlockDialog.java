package steve6472.polyground.generator.creator.dialogs;

import steve6472.polyground.generator.creator.BlockCreatorGui;
import steve6472.polyground.generator.creator.BlockEntry;
import steve6472.sge.gui.components.Button;
import steve6472.sge.gui.components.RadioGroup;
import steve6472.sge.gui.components.TextField;
import steve6472.sge.gui.components.context.ContextMenu;
import steve6472.sge.gui.components.context.ContextMenuButton;
import steve6472.sge.gui.components.context.ContextMenuToggleButton;
import steve6472.sge.gui.components.dialog.AdvancedMoveableDialog;
import steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.10.2019
 * Project: SJP
 *
 ***********************/
public class NewBlockDialog extends AdvancedMoveableDialog
{
	private Button ok, modelType, selectParent;
	private ContextMenu modelMenu, parentMenu;
	private TextField name;
	private BlockCreatorGui creatorGui;

	public NewBlockDialog(BlockCreatorGui creatorGui)
	{
		this.creatorGui = creatorGui;
	}

	@Override
	public void init(MainApp main)
	{
		width = 304;
		height = 170;

		initCloseButton();

		/* Buttons */
		modelType = new Button("Custom Model");
		modelType.setRelativeLocation(12, 32);
		modelType.setSize(150, 25);
		modelType.addClickEvent(c ->
		{
			setContextMenu(modelMenu);
			modelMenu.show(getX() + 12, getY() + 57);
		});
		addComponent(modelType);

		selectParent = new Button("<Select Parent>");
		selectParent.setRelativeLocation(12, 67);
		selectParent.setSize(150, 25);
		selectParent.setEnabled(false);
		selectParent.addClickEvent(c ->
		{
			setContextMenu(parentMenu);
			parentMenu.show(getX() + 12, getY() + 92);
		});
		addComponent(selectParent);

		ok = new Button("Ok");
		ok.setRelativeLocation(172, getHeight() - 37);
		ok.setSize(120, 25);
		addComponent(ok);

		name = new TextField();
		name.setRelativeLocation(12, getHeight() - 37);
		name.setSize(150, 25);
		name.setText("block_name");
		name.endCarret();
		addComponent(name);

		initParentContextMenu();
		initModelContextMenu();
	}

	private void initParentContextMenu()
	{
		parentMenu = new ContextMenu();
		parentMenu.darkenGui = false;
		parentMenu.setSize(150);
		setContextMenu(modelMenu);

		boolean foundParent = false;

		for (BlockEntry be : creatorGui.blocks.values())
		{
			if (be.isParent())
			{
				ContextMenuButton button = new ContextMenuButton();
				button.setImage('>');
				button.setName(be.getName());
				button.addClickEvent(c -> selectParent.setText(be.getName()));
				button.addClickEvent(c -> parentMenu.hide());
				parentMenu.add(button);
				foundParent = true;
			}
		}

		if (!foundParent)
		{
			ContextMenuButton button = new ContextMenuButton();
			button.setImage('!');
			button.setName("No parent found!");
			button.addClickEvent(c -> selectParent.setText("<Select Parent>"));
			button.addClickEvent(c -> parentMenu.hide());
			parentMenu.add(button);
		}

	}

	private void initModelContextMenu()
	{
		modelMenu = new ContextMenu();
		modelMenu.darkenGui = false;
		modelMenu.setSize(150);
		setContextMenu(modelMenu);

		ContextMenuToggleButton modelCustom = new ContextMenuToggleButton();
		modelCustom.setName("Custom Model");
		modelCustom.setToggled(true);
		modelCustom.addClickEvent(c ->
		{
			selectParent.setEnabled(false);
			modelType.setText(c.getName());
			modelMenu.hide();
		});

		ContextMenuToggleButton fromParent = new ContextMenuToggleButton();
		fromParent.setName("Child");
		fromParent.setToggled(false);
		fromParent.addClickEvent(c ->
		{
			selectParent.setEnabled(true);
			modelType.setText(c.getName());
			modelMenu.hide();
		});

		ContextMenuToggleButton newParent = new ContextMenuToggleButton();
		newParent.setName("Parent");
		newParent.setToggled(false);
		newParent.addClickEvent(c ->
		{
			selectParent.setEnabled(false);
			modelType.setText(c.getName());
			modelMenu.hide();
		});

		RadioGroup.addContextMenuToggleButtons(modelCustom, fromParent, newParent);

		modelMenu.add(modelCustom);
		modelMenu.add(fromParent);
		modelMenu.add(newParent);
	}

	@Override
	public void tick()
	{
	}

	@Override
	public void render()
	{
		renderTitle("New Block");
	}

	@Override
	public boolean freezeGui()
	{
		return true;
	}

	@Override
	public boolean disableEvents()
	{
		return true;
	}
}
