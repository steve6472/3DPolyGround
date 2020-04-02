package steve6472.polyground.world.chunk;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 17.11.2019
 * Project: SJP
 *
 ***********************/
public enum ModelLayer
{
	NORMAL(0), LIGHT(1), EMISSION_NORMAL(2), OVERLAY_0(3), /*OVERLAY_1, OVERLAY_2,*/ EMISSION_OVERLAY(4), FOLIAGE(5);

	int index;

	ModelLayer(int index)
	{
		this.index = index;
	}
}
