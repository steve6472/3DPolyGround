package steve6472.polyground;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 21.08.2019
 * Project: SJP
 *
 ***********************/
public enum EnumFace
{
	/**
	 * Y+
	 */
	UP,

	/**
	 * Y-
	 */
	DOWN,

	/**
	 * X+
	 */
	NORTH,

	/**
	 * X-
	 */
	SOUTH,

	/**
	 * Z+
	 */
	EAST,

	/**
	 * Z-
	 */
	WEST,

	/**
	 * NaN
	 */
	NONE;

	public String getName()
	{
		return this.name().toLowerCase();
	}

	public String getFancyName()
	{
		return this.name().charAt(0) + this.name().toLowerCase().substring(1);
	}

	public static EnumFace[] getFaces()
	{
		return new EnumFace[]{UP, DOWN, NORTH, EAST, SOUTH, WEST};
	}

	public static EnumFace[] getFacesReversed()
	{
		return new EnumFace[]{WEST, SOUTH, EAST, NORTH, DOWN, UP};
	}

	public boolean isSide()
	{
		return this != UP && this != DOWN;
	}

	public EnumFace getOpposite()
	{
		if (this == UP)
			return DOWN;
		if (this == DOWN)
			return UP;
		if (this == NORTH)
			return SOUTH;
		if (this == SOUTH)
			return NORTH;
		if (this == EAST)
			return WEST;
		if (this == WEST)
			return EAST;

		return NONE;
	}

	public int getXOffset()
	{
		if (this == NORTH)
			return 1;
		if (this == SOUTH)
			return -1;
		return 0;
	}

	public int getYOffset()
	{
		if (this == UP)
			return 1;
		if (this == DOWN)
			return -1;
		return 0;
	}

	public int getZOffset()
	{
		if (this == EAST)
			return 1;
		if (this == WEST)
			return -1;
		return 0;
	}

	public static EnumFace get(byte b)
	{
		return switch (b)
			{
				case 0 -> UP;
				case 1 -> DOWN;
				case 2 -> NORTH;
				case 3 -> SOUTH;
				case 4 -> EAST;
				case 5 -> WEST;
				default -> NONE;
			};
	}
}
