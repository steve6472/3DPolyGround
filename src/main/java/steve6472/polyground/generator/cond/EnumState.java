package steve6472.polyground.generator.cond;

enum EnumState
{
	INCLUDE("==", 0xdd30d933), EXCLUDE("!=", 0xddd94c30), IGNORE(null, 0xdd889294);

	String sign;
	int color;

	EnumState(String sign, int color)
	{
		this.sign = sign;
		this.color = color;
	}

	public static EnumState cycle(EnumState a)
	{
		return switch (a)
			{
				case INCLUDE -> EXCLUDE;
				case EXCLUDE -> IGNORE;
				case IGNORE -> INCLUDE;
			};
	}

	public static EnumState reverseCycle(EnumState a)
	{
		return switch (a)
			{
				case INCLUDE -> IGNORE;
				case IGNORE -> EXCLUDE;
				case EXCLUDE -> INCLUDE;
			};
	}
}