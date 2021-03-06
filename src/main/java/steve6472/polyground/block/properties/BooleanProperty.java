package steve6472.polyground.block.properties;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 02.07.2020
 * Project: StateTest
 *
 ***********************/
public class BooleanProperty extends Property<Boolean>
{
	private BooleanProperty(String name)
	{
		super(name);
	}

	public static BooleanProperty create(String name)
	{
		return new BooleanProperty(name);
	}

	@Override
	public Boolean[] getPossibleValues()
	{
		return new Boolean[] {true, false};
	}
}
