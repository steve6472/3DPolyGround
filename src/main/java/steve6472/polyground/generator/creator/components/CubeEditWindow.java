package steve6472.polyground.generator.creator.components;

import steve6472.modelmaker.components.NumberSliderTriplet;
import steve6472.polyground.block.model.elements.TriangleElement;
import steve6472.polyground.generator.creator.BlockCreatorGui;
import steve6472.sge.gui.Component;
import steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.08.2020
 * Project: CaveGame
 *
 ***********************/
public class CubeEditWindow extends Component
{
	private final BlockCreatorGui blockCreatorGui;
	private NumberSliderTriplet pos;

	public CubeEditWindow(BlockCreatorGui blockCreatorGui)
	{
		this.blockCreatorGui = blockCreatorGui;
	}

	@Override
	public void init(MainApp mainApp)
	{
		pos = new NumberSliderTriplet();
		pos.setRelativeLocation(7, 7);
		pos.setSize(220, 30);
		pos.setOffset(1);
		addComponent(pos);
		pos.getSliderX().addChangeEvent(() -> ((TriangleElement) blockCreatorGui.elements.getSelected()).v0.x = pos.getSliderX().getValue());
		pos.getSliderY().addChangeEvent(() -> ((TriangleElement) blockCreatorGui.elements.getSelected()).v0.y = pos.getSliderY().getValue());
		pos.getSliderZ().addChangeEvent(() -> ((TriangleElement) blockCreatorGui.elements.getSelected()).v0.z = pos.getSliderZ().getValue());
	}

	@Override
	public void tick()
	{

	}

	@Override
	public void render()
	{

	}
}
