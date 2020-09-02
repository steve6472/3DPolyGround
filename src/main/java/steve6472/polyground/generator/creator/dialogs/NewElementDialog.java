package steve6472.polyground.generator.creator.dialogs;

import steve6472.polyground.block.model.elements.CubeElement;
import steve6472.polyground.block.model.elements.PlaneElement;
import steve6472.polyground.block.model.elements.TriangleElement;
import steve6472.polyground.generator.creator.BlockCreatorGui;
import steve6472.sge.gfx.font.Font;
import steve6472.sge.gui.components.Button;
import steve6472.sge.gui.components.RadioGroup;
import steve6472.sge.gui.components.TextField;
import steve6472.sge.gui.components.context.ContextMenu;
import steve6472.sge.gui.components.context.ContextMenuToggleButton;
import steve6472.sge.gui.components.dialog.AdvancedMoveableDialog;
import steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 27.10.2019
 * Project: SJP
 *
 ***********************/
public class NewElementDialog extends AdvancedMoveableDialog
{
	private Button ok, special;
	private ContextMenu elementMenu;
	private ContextMenuToggleButton cube, plane, triangle;
	private TextField name;
	private BlockCreatorGui creatorGui;

	public NewElementDialog(BlockCreatorGui creatorGui)
	{
		this.creatorGui = creatorGui;
	}

	@Override
	public void init(MainApp main)
	{
		width = 304;
		height = 104;

		initCloseButton();

		/* Buttons */
		special = new Button("Cube");
		special.setRelativeLocation(132, 32);
		special.setSize(160, 25);
		special.addClickEvent(c ->
		{
			setContextMenu(elementMenu);
			elementMenu.show(getX() + 122, getY() + 57);
		});
		addComponent(special);

		ok = new Button("Ok");
		ok.setRelativeLocation(172, getHeight() - 37);
		ok.setSize(120, 25);
		ok.addClickEvent(c -> {
			if (cube.isToggled())
			{
				CubeElement e = new CubeElement(name.getText());
				creatorGui.elements.addElement(e.creator());
				e.getCreatorData().canMove = true;
			}
			if (plane.isToggled())
			{
				PlaneElement e = new PlaneElement(name.getText());
				creatorGui.elements.addElement(e.creator());
				e.getCreatorData().canMove = true;
			}
			if (triangle.isToggled())
			{
				TriangleElement e = new TriangleElement(name.getText());
				creatorGui.elements.addElement(e.creator());
				e.getCreatorData().canMove = true;
			}
			close();
		});
		addComponent(ok);

		name = new TextField();
		name.setRelativeLocation(12, getHeight() - 37);
		name.setSize(150, 25);
		name.setText("element_name");
		name.endCarret();
		name.addTypeEvent((t, c) -> {
			t.setText(t.getText().toLowerCase());
		});
		addComponent(name);

		initModelContextMenu();
	}

	private void initModelContextMenu()
	{
		elementMenu = new ContextMenu();
		elementMenu.darkenGui = false;
		elementMenu.setSize(150);
		setContextMenu(elementMenu);

		ContextMenuToggleButton[] buttons = new ContextMenuToggleButton[3];

		cube = new ContextMenuToggleButton();
		cube.setName("Cube");
		cube.setToggled(true);
		cube.addClickEvent(c ->
		{
			special.setText(c.getName());
			elementMenu.hide();
		});
		elementMenu.add(cube);
		buttons[0] = cube;

		plane = new ContextMenuToggleButton();
		plane.setName("Plane");
		plane.addClickEvent(c ->
		{
			special.setText(c.getName());
			elementMenu.hide();
		});
		elementMenu.add(plane);
		buttons[1] = plane;

		triangle = new ContextMenuToggleButton();
		triangle.setName("Triangle");
		triangle.addClickEvent(c ->
		{
			special.setText(c.getName());
			elementMenu.hide();
		});
		elementMenu.add(triangle);
		buttons[2] = triangle;

		RadioGroup.addContextMenuToggleButtons(buttons);
	}

	@Override
	public void tick()
	{
	}

	@Override
	public void render()
	{
		renderTitle("New Element");
		Font.render("Element: ", getX() + 11, getY() + 35, 2);
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
