package steve6472.polyground.block.properties.enums;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 05.10.2020
 * Project: CaveGame
 *
 ***********************/
public enum EnumStoneType
{
	SANDSTONE, LIMESTONE, QUARTZITE;

	private static final EnumStoneType[] VALUES = {SANDSTONE, LIMESTONE, QUARTZITE};

	public static EnumStoneType[] getValues()
	{
		return VALUES;
	}
}
