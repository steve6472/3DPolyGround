package steve6472.polyground.registry.feature;

import org.json.JSONObject;
import steve6472.polyground.world.generator.Feature;

public class FeatureEntry
{
	private final IFeatureFactory factory;
	private final String name;

	FeatureEntry(String name, IFeatureFactory factory)
	{
		this.name = name;
		this.factory = factory;
	}

	public String name()
	{
		return name;
	}

	public Feature createNew(JSONObject json)
	{
		Feature feature = factory.create();
		feature.load(json);
		return feature;
	}
}