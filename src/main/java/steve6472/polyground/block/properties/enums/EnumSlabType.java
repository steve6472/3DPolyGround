package steve6472.polyground.block.properties.enums;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 03.07.2020
 * Project: CaveGame
 *
 ***********************/
public enum EnumSlabType
{
	TOP, BOTTOM, DOUBLE;

	private static final EnumSlabType[] VALUES = {TOP, BOTTOM, DOUBLE};

	public static EnumSlabType[] getValues()
	{
		return VALUES;
	}
}
