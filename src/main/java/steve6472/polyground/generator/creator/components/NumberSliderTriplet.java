package steve6472.polyground.generator.creator.components;

import steve6472.sge.gui.Component;
import steve6472.sge.main.MainApp;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.08.2020
 * Project: CaveGame
 *
 ***********************/
public class NumberSliderTriplet extends Component
{
	private NumberSlider sliderX, sliderY, sliderZ;
	private int offset;

	@Override
	public void init(MainApp mainApp)
	{
		sliderX = new NumberSlider();
		sliderX.setCornerColor(1, 0, 0);
		addComponent(sliderX);

		sliderY = new NumberSlider();
		sliderY.setCornerColor(0, 1, 0);
		addComponent(sliderY);

		sliderZ = new NumberSlider();
		sliderZ.setCornerColor(0, 0, 1);
		addComponent(sliderZ);

		update();
	}

	public void update()
	{
		if (sliderX == null)
			return;

		sliderX.setSize(width / 3 - offset, height);
		sliderX.setRelativeLocation(0, 0);

		sliderY.setSize(width / 3 - offset, height);
		sliderY.setRelativeLocation(width / 3 + offset, 0);

		sliderZ.setSize(width / 3 - offset, height);
		sliderZ.setRelativeLocation(width / 3 * 2 + offset * 2, 0);
	}

	public void setOffset(int offset)
	{
		this.offset = offset;
		update();
	}

	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height);
		update();
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
