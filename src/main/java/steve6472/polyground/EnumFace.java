package steve6472.polyground;

import steve6472.polyground.block.properties.enums.EnumAxis;

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
	UP(EnumAxis.Y),

	/**
	 * Y-
	 */
	DOWN(EnumAxis.Y),

	/**
	 * X+
	 */
	NORTH(EnumAxis.X),

	/**
	 * X-
	 */
	SOUTH(EnumAxis.X),

	/**
	 * Z+
	 */
	EAST(EnumAxis.Z),

	/**
	 * Z-
	 */
	WEST(EnumAxis.Z),

	/**
	 * NaN
	 */
	NONE(null);

	private final String name, fancyName;
	private final EnumAxis axis;

	EnumFace(EnumAxis axis)
	{
		this.name = name().toLowerCase();
		this.fancyName = name().charAt(0) + name.substring(1);
		this.axis = axis;
	}

	private static final EnumFace[] VALUES = new EnumFace[] {UP, DOWN, NORTH, EAST, SOUTH, WEST, NONE};
	private static final EnumFace[] FACES = new EnumFace[] {UP, DOWN, NORTH, EAST, SOUTH, WEST};
	private static final EnumFace[] FACES_REVERSED = new EnumFace[] {WEST, SOUTH, EAST, NORTH, DOWN, UP};

	public EnumAxis getAxis()
	{
		return axis;
	}

	public String getName()
	{
		return name;
	}

	public String getFancyName()
	{
		return fancyName;
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
				case 4 -> EAST;
				case 3 -> SOUTH;
				case 5 -> WEST;
				default -> NONE;
			};
	}

	public static EnumFace get(String face)
	{
		return switch (face.toLowerCase())
			{
				case "up" -> UP;
				case "down" -> DOWN;
				case "north" -> NORTH;
				case "east" -> EAST;
				case "south" -> SOUTH;
				case "west" -> WEST;
				default -> NONE;
			};
	}
}
