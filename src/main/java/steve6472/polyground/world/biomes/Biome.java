package steve6472.polyground.world.biomes;

import org.joml.Vector3f;
import steve6472.polyground.block.Block;
import steve6472.polyground.world.generator.FeatureStage;
import steve6472.polyground.world.generator.feature.FeatureEntry;
import steve6472.polyground.world.generator.feature.IFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 07.10.2019
 * Project: SJP
 *
 ***********************/
public abstract class Biome
{
	private final Map<FeatureStage, List<FeatureEntry>> features;

	public Biome()
	{
		features = new HashMap<>();
		addFeatures();
	}

	public abstract void addFeatures();

	public abstract String getName();

	public abstract Block getTopBlock();

	public abstract Block getUnderBlock();

	public abstract Block getCaveBlock();

	public abstract int getUnderLayerHeight();

	public abstract int getIterationCount();

	public abstract float getPersistance();

	public abstract float getScale();

	public abstract float getLow();

	public abstract float getHigh();

	public abstract Vector3f getColor();

	public Map<FeatureStage, List<FeatureEntry>> getFeatures()
	{
		return features;
	}

	protected void addFeature(FeatureStage stage, IFeature feature, double chance)
	{
		addFeature(stage, new FeatureEntry(feature, chance));
	}

	protected void addFeature(FeatureStage stage, FeatureEntry feature)
	{
		List<FeatureEntry> entries = features.get(stage);
		if (entries == null)
		{
			entries = new ArrayList<>();
			entries.add(feature);
			features.put(stage, entries);
		} else
		{
			entries.add(feature);
		}
	}
}
