package steve6472.polyground.world.generator.feature;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.05.2020
 * Project: CaveGame
 *
 ***********************/
public class FeatureEntry
{
	public final IFeature feature;
	public final double chance;

	public FeatureEntry(IFeature feature, double chance)
	{
		this.feature = feature;
		this.chance = chance;
	}
}
