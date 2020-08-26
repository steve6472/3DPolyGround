package steve6472.polyground.world.biomes;

import org.joml.Vector3f;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.generator.EnumFeatureStage;
import steve6472.polyground.world.generator.FeatureEntry;
import steve6472.polyground.world.generator.Feature;

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
	private final int id;
	private final Map<EnumFeatureStage, List<FeatureEntry>> features;

	public Biome()
	{
		id = Biomes.ID++;
		features = new HashMap<>();
		for (EnumFeatureStage stage : EnumFeatureStage.getValues())
			features.put(stage, new ArrayList<>());

		addFeatures();
	}

	public abstract void addFeatures();

	public abstract float[] getParameters();

	public abstract String getName();

	public abstract BlockState getTopBlock();

	public abstract BlockState getUnderBlock();

	public abstract BlockState getCaveBlock();

	/**
	 * Indicates how high the biome will stretch above surface
	 * @return biome height
	 */
	public abstract int getBiomeHeight();

	public abstract int getUnderLayerHeight();

	public abstract int getIterationCount();

	public abstract float getPersistance();

	public abstract float getScale();

	public abstract float getLow();

	public abstract float getHigh();

	public abstract Vector3f getColor();

	public abstract boolean generatesNaturally();

	public Map<EnumFeatureStage, List<FeatureEntry>> getFeatures()
	{
		return features;
	}

	public List<FeatureEntry> getFeaturesForStage(EnumFeatureStage stage)
	{
		return features.get(stage);
	}

	protected void addFeature(EnumFeatureStage stage, double chance, Feature feature)
	{
		addFeature(stage, new FeatureEntry(feature, chance));
	}

	protected void addFeature(EnumFeatureStage stage, FeatureEntry feature)
	{
		if (feature == null)
			throw new NullPointerException();
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

	public int getId()
	{
		return id;
	}
}
