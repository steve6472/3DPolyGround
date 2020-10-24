package steve6472.polyground.block.properties.enums;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 10.08.2020
 * Project: CaveGame
 *
 ***********************/
public enum EnumLR
{
	LEFT, RIGHT;

	private static final EnumLR[] VALUES = {LEFT, RIGHT};

	public static EnumLR[] getValues()
	{
		return VALUES;
	}
}
