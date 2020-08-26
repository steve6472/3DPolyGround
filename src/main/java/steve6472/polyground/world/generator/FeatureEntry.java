package steve6472.polyground.world.generator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.07.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class FeatureEntry
{
	private final Feature feature;
	private final double chance;

	public FeatureEntry(Feature feature, double chance)
	{
		this.feature = feature;
		this.chance = chance;
	}

	public Feature getFeature()
	{
		return feature;
	}

	public double getChance()
	{
		return chance;
	}

	@Override
	public String toString()
	{
		return "FeatureEntry{" + "feature=" + feature + ", chance=" + chance + '}';
	}
}
