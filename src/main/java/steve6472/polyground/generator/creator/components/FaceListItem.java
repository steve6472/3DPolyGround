package steve6472.polyground.generator.creator.components;

import steve6472.polyground.EnumFace;
import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.block.model.registry.Cube;
import steve6472.polyground.block.model.registry.face.FaceRegistry;
import steve6472.polyground.generator.creator.BlockCreatorGui;
import steve6472.sge.gfx.font.CustomChar;
import steve6472.sge.gui.Component;
import steve6472.sge.gui.components.ToggleButton;
import steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 12.10.2019
 * Project: SJP
 *
 ***********************/
public class FaceListItem extends Component
{
	private ToggleButton button, visible;

	private BlockCreatorGui creatorGui;
	private EnumFace face;

	public FaceListItem(BlockCreatorGui creatorGui, EnumFace face)
	{
		this.creatorGui = creatorGui;
		this.face = face;
	}

	@Override
	public void init(MainApp main)
	{
		button = new ToggleButton(face.name().substring(0, 1).toUpperCase() + face.getName().substring(1));
		button.setSize(200 - 25 - 5, 25);
		addComponent(button);

		visible = new ToggleButton(CustomChar.EYE_ICON);
		visible.setRelativeLocation(200 - 25, 0);
		visible.setSize(25, 25);
		visible.setToggled(true);
		visible.addChangeEvent(c ->
		{
			Cube cube = creatorGui.getSelectedCube();
			if (cube == null)
				return;

			CubeFace cubeFace = cube.getFace(face);
			if (cubeFace == null)
				return;

			cubeFace.getProperty(FaceRegistry.isVisible).setVisible(c.isToggled());
		});
		addComponent(visible);
	}

	@Override
	public void tick()
	{
		tickComponents();
	}

	@Override
	public void render()
	{
		renderComponents();
	}

	public ToggleButton getToggleButton()
	{
		return button;
	}

	public ToggleButton getVisButton()
	{
		return visible;
	}

	public boolean isSelected()
	{
		return button.isToggled();
	}

	public void setSelected(boolean flag)
	{
		button.setToggled(flag);
	}

	public boolean isFaceVisible()
	{
		return visible.isToggled();
	}

	public void setVisible(boolean flag)
	{
		visible.setToggled(flag);
	}

	public EnumFace getFace()
	{
		return face;
	}

	public void setEnabled(boolean flag)
	{
		button.setEnabled(flag);
		visible.setEnabled(flag);
	}
}
