package steve6472.polyground.block.properties.enums;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 05.07.2020
 * Project: CaveGame
 *
 ***********************/
public enum EnumAxis
{
	X, Y, Z;

	private static final EnumAxis[] VALUES = {X, Y, Z};

	public static EnumAxis[] getValues()
	{
		return VALUES;
	}
}
