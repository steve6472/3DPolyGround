package steve6472.polyground.world.generator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.07.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public enum EnumFeatureStage
{
	NONE, LAND_ALTER, CAVE_ALTER, TOP_MODIFICATION, TREE, VEGETATION, ORE, AIR, FINISHED;

	private static final EnumFeatureStage[] VALUES = {NONE, LAND_ALTER, CAVE_ALTER, TOP_MODIFICATION, TREE, VEGETATION, ORE, AIR, FINISHED};

	public static EnumFeatureStage[] getValues()
	{
		return VALUES;
	}
}
