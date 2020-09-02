package steve6472.polyground.generator.creator.dialogs;

import steve6472.polyground.block.model.elements.TriangleElement;
import steve6472.sge.gui.components.Button;
import steve6472.sge.gui.components.dialog.AdvancedMoveableDialog;
import steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.08.2020
 * Project: CaveGame
 *
 ***********************/
public class TriangleDialog extends AdvancedMoveableDialog
{
	private final TriangleElement element;

	public TriangleDialog(TriangleElement element)
	{
		this.element = element;
	}

	@Override
	public void init(MainApp mainApp)
	{
		setSize(200, 100);

		Button b = new Button("A");
		b.setRelativeLocation(10, 10);
		b.setSize(100, 20);
		b.addClickEvent(c -> {
			element.v0.set(0, 1, 0);
			element.v1.set(0, 0, 0);
			element.v2.set(0, 0, 1);
			System.out.println(element.v0 + " " + element.v1 + " " + element.v2);
			close();
		});
		addComponent(b);
	}

	@Override
	public void tick()
	{

	}

	@Override
	public void render()
	{
		renderTitle("Triangle");
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
