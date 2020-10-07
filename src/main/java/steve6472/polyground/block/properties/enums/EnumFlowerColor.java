package steve6472.polyground.block.properties.enums;

import steve6472.sge.main.util.ColorUtil;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2020
 * Project: CaveGame
 *
 ***********************/
public enum EnumFlowerColor
{
	RED(0xff0000),
	GREEN(0x00ff00),
	BLUE(0x0000ff),
	CYAN(0x00ffff),
	MAGENTA(0xff00ff),
	YELLOW(0xffff00),
	BLACK(0),
	WHITE(0xffffff)
	;

	public final int r, g, b;

	EnumFlowerColor(int colorHex)
	{
		r = ColorUtil.getRed(colorHex);
		g = ColorUtil.getGreen(colorHex);
		b = ColorUtil.getBlue(colorHex);
	}

	private static EnumFlowerColor[] VALUES = {RED, GREEN, BLUE, CYAN, MAGENTA, YELLOW, BLACK, WHITE};

	public static EnumFlowerColor[] getValues()
	{
		return VALUES;
	}
}
