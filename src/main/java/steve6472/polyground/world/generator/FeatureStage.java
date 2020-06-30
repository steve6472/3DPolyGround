package steve6472.polyground.world.generator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.05.2020
 * Project: CaveGame
 *
 ***********************/
public enum FeatureStage
{
	NONE, LAND_ALTER, TREE, VEGETATION, ORE, FINISHED;

	private static final FeatureStage[] VALUES = {NONE, LAND_ALTER, TREE, VEGETATION, ORE, FINISHED};

	public static FeatureStage[] getValues()
	{
		return VALUES;
	}
}
