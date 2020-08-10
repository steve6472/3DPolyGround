package steve6472.polyground.block.properties.enums;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 10.08.2020
 * Project: CaveGame
 *
 ***********************/
public enum EnumHalfSnowy
{
	TOP, BOTTOM, BOTTOM_1, BOTTOM_2, BOTTOM_3, BOTTOM_4, BOTTOM_5, BOTTOM_6, BOTTOM_7, BOTTOM_8;

	private static final EnumHalfSnowy[] VALUES = {TOP, BOTTOM, BOTTOM_1, BOTTOM_2, BOTTOM_3, BOTTOM_4, BOTTOM_5, BOTTOM_6, BOTTOM_7, BOTTOM_8};

	public static EnumHalfSnowy[] getValues()
	{
		return VALUES;
	}

	public boolean isBottom()
	{
		return this != TOP;
	}

	public boolean isSnowy()
	{
		return this != TOP && this != BOTTOM;
	}
}
