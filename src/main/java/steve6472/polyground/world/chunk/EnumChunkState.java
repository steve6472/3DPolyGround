package steve6472.polyground.world.chunk;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.05.2020
 * Project: CaveGame
 *
 ***********************/
public enum EnumChunkState
{
	NOT_GENERATED, SHAPE, FEATURES, FULL;

	private static final EnumChunkState[] VALUES = new EnumChunkState[] {NOT_GENERATED, SHAPE, FEATURES, FULL};

	public static EnumChunkState[] getValues()
	{
		return VALUES;
	}
}
