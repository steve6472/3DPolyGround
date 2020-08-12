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

	private static final EnumFace[] VALUES = new EnumFace[] {UP, DOWN, NORTH, EAST, SOUTH, WEST, NONE};
	private static final EnumFace[] FACES = new EnumFace[] {UP, DOWN, NORTH, EAST, SOUTH, WEST};
	private static final EnumFace[] FACES_REVERSED = new EnumFace[] {WEST, SOUTH, EAST, NORTH, DOWN, UP};

	public String getName()
	{
		return this.name().toLowerCase();
	}

	public String getFancyName()
	{
		return this.name().charAt(0) + this.name().toLowerCase().substring(1);
	}

	public static EnumFace[] getValues()
	{
		return VALUES;
	}

	public static EnumFace[] getFaces()
	{
		return FACES;
	}

	public static EnumFace[] getFacesReversed()
	{
		return FACES_REVERSED;
	}

	public boolean isSide()
	{
		return this != UP && this != DOWN;
	}

	public EnumFace getOpposite()
	{
		return switch (this)
			{
				case UP -> DOWN;
				case DOWN -> UP;
				case NORTH -> SOUTH;
				case SOUTH -> NORTH;
				case EAST -> WEST;
				case WEST -> EAST;
				default -> NONE;
			};

	}

	public int getXOffset()
	{
		return switch (this)
			{
				case NORTH -> 1;
				case SOUTH -> -1;
				default -> 0;
			};
	}

	public int getYOffset()
	{
		return switch (this)
			{
				case UP -> 1;
				case DOWN -> -1;
				default -> 0;
			};
	}

	public int getZOffset()
	{
		return switch (this)
			{
				case EAST -> 1;
				case WEST -> -1;
				default -> 0;
			};
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
