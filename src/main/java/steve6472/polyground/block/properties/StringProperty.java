package steve6472.polyground.block.properties;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 02.07.2020
 * Project: StateTest
 *
 ***********************/
@Deprecated
public class StringProperty extends Property<String>
{
	private final String[] possibleValues;

	private StringProperty(String name, String[] possibleValues)
	{
		super(name);
		this.possibleValues = possibleValues;
	}

	public static StringProperty create(String name, String[] possibleValues)
	{
		return new StringProperty(name, possibleValues);
	}

	@Override
	public String[] getPossibleValues()
	{
		return possibleValues;
	}
}
