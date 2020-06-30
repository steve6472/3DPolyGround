package steve6472.polyground.world.generator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 28.05.2020
 * Project: CaveGame
 *
 ***********************/
public enum FeatureStage
{
	NONE, LAND_ALTER, CAVE_ALTER(false), TREE, VEGETATION, ORE, FINISHED;

	private final boolean onGround;

	FeatureStage()
	{
		onGround = true;
	}

	FeatureStage(boolean onGround)
	{
		this.onGround = onGround;
	}

	public boolean isOnGround()
	{
		return onGround;
	}

	private static final FeatureStage[] VALUES = {NONE, CAVE_ALTER, LAND_ALTER, TREE, VEGETATION, ORE, FINISHED};

	public static FeatureStage[] getValues()
	{
		return VALUES;
	}
}
