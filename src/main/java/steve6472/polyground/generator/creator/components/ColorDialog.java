package steve6472.polyground.generator.creator.components;

import steve6472.polyground.block.model.CubeFace;
import steve6472.polyground.registry.face.FaceRegistry;
import steve6472.sge.gfx.font.CustomChar;
import steve6472.sge.gui.components.Button;
import steve6472.sge.gui.components.NamedCheckBox;
import steve6472.sge.gui.components.dialog.SliderColorSelectDialog;
import steve6472.sge.main.MainApp;

import java.util.function.BiConsumer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2019
 * Project: SJP
 *
 ***********************/
public class ColorDialog extends SliderColorSelectDialog
{
	private NamedCheckBox isEmissive;
	private CubeFace face;

	public ColorDialog(CubeFace face)
	{
		super(null);
		this.face = face;
	}

	public void setOkEvent(BiConsumer<Button, SliderColorSelectDialog> okEvent)
	{
		this.okEvent = okEvent;
	}

	@Override
	public void init(MainApp main)
	{
		super.init(main);
		setSize(276 + 60, 138 + 41);

		redSlider.setValue(face.getProperty(FaceRegistry.tint).getRed() * 255f);
		greenSlider.setValue(face.getProperty(FaceRegistry.tint).getGreen() * 255f);
		blueSlider.setValue(face.getProperty(FaceRegistry.tint).getBlue() * 255f);

		isEmissive = new NamedCheckBox();
		isEmissive.setRelativeLocation(10, 145);
		isEmissive.setText("Is Emissive");
		isEmissive.setSize(120, 25);
		isEmissive.setBoxSize(14, 14);
		isEmissive.setSelectedChar(CustomChar.CROSS);
		isEmissive.setBoxPadding(5, 5);
		isEmissive.setEnabled(false);
		addComponent(isEmissive);
	}

	public boolean isEmissive()
	{
		return isEmissive.isToggled();
	}
}
