package steve6472.polyground.gfx.model;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 24.10.2020
 * Project: CaveGame
 *
 ***********************/
public record NumericValue(float value) implements IKeyValue
{
	@Override
	public float getValue(double time)
	{
		return value;
	}
}
