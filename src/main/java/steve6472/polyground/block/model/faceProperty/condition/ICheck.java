package steve6472.polyground.block.model.faceProperty.condition;

import steve6472.polyground.world.World;

interface ICheck
{
	boolean test(int x, int y, int z, World world);

	default void fixBlockId() {}

	default boolean hasCheckSign(String s)
	{
		return s.contains("||") || s.contains("&&") || s.contains("!=") || s.contains("==");
	}

	default void print(String s, Object... f)
	{
		if (Boolean.parseBoolean(System.getProperty("debug_conditions", "false")))
			System.out.println(String.format(s, f));
	}
}