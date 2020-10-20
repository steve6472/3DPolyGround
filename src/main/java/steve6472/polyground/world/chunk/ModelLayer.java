package steve6472.polyground.world.chunk;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 17.11.2019
 * Project: SJP
 *
 ***********************/
public enum ModelLayer
{
	TRANSPARENT(0), NORMAL(1), LIGHT(2), EMISSION_NORMAL(3), OVERLAY_0(4), EMISSION_OVERLAY(5);

	int index;

	ModelLayer(int index)
	{
		this.index = index;
	}

	private static final ModelLayer[] VALUES = new ModelLayer[] {TRANSPARENT, NORMAL, LIGHT, EMISSION_NORMAL, OVERLAY_0, EMISSION_OVERLAY};
}
