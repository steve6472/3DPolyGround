package steve6472.polyground.generator.creator.components;

import steve6472.modelmaker.components.NumberSliderTriplet;
import steve6472.polyground.block.model.elements.TriangleElement;
import steve6472.polyground.generator.creator.BlockCreatorGui;
import steve6472.sge.gfx.SpriteRender;
import steve6472.sge.gui.Component;
import steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.08.2020
 * Project: CaveGame
 *
 ***********************/
public class TriangleEditWindow extends Component
{
	private final BlockCreatorGui blockCreatorGui;
	private NumberSliderTriplet v0, v1, v2;

	public TriangleEditWindow(BlockCreatorGui blockCreatorGui)
	{
		this.blockCreatorGui = blockCreatorGui;
	}

	@Override
	public void init(MainApp mainApp)
	{
		v0 = new NumberSliderTriplet();
		v0.setRelativeLocation(7, 7);
		v0.setSize(220, 30);
		v0.setOffset(1);
		addComponent(v0);
		v0.getSliderX().addChangeEvent(() -> ((TriangleElement) blockCreatorGui.elements.getSelected()).v0.x = v0.getSliderX().getValue());
		v0.getSliderY().addChangeEvent(() -> ((TriangleElement) blockCreatorGui.elements.getSelected()).v0.y = v0.getSliderY().getValue());
		v0.getSliderZ().addChangeEvent(() -> ((TriangleElement) blockCreatorGui.elements.getSelected()).v0.z = v0.getSliderZ().getValue());

		v1 = new NumberSliderTriplet();
		v1.setRelativeLocation(7, 42);
		v1.setSize(220, 30);
		v1.setOffset(1);
		addComponent(v1);
		v1.getSliderX().addChangeEvent(() -> ((TriangleElement) blockCreatorGui.elements.getSelected()).v1.x = v1.getSliderX().getValue());
		v1.getSliderY().addChangeEvent(() -> ((TriangleElement) blockCreatorGui.elements.getSelected()).v1.y = v1.getSliderY().getValue());
		v1.getSliderZ().addChangeEvent(() -> ((TriangleElement) blockCreatorGui.elements.getSelected()).v1.z = v1.getSliderZ().getValue());

		v2 = new NumberSliderTriplet();
		v2.setRelativeLocation(7, 77);
		v2.setSize(220, 30);
		v2.setOffset(1);
		addComponent(v2);
		v2.getSliderX().addChangeEvent(() -> ((TriangleElement) blockCreatorGui.elements.getSelected()).v2.x = v2.getSliderX().getValue());
		v2.getSliderY().addChangeEvent(() -> ((TriangleElement) blockCreatorGui.elements.getSelected()).v2.y = v2.getSliderY().getValue());
		v2.getSliderZ().addChangeEvent(() -> ((TriangleElement) blockCreatorGui.elements.getSelected()).v2.z = v2.getSliderZ().getValue());
	}

	public void update(TriangleElement e)
	{
		v0.setValues(e.v0.x, e.v0.y, e.v0.z);
		v1.setValues(e.v1.x, e.v1.y, e.v1.z);
		v2.setValues(e.v2.x, e.v2.y, e.v2.z);
	}

	@Override
	public void tick()
	{

	}

	@Override
	public void render()
	{
		SpriteRender.renderSingleBorderComponent(this, 0.0F, 0.0F, 0.0F, 1.0F, 0.3019608F, 0.3019608F, 0.3019608F, 1.0F);
	}
}
