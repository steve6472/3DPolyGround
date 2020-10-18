package steve6472.polyground.registry.feature;

import org.json.JSONObject;
import steve6472.polyground.world.generator.Feature;
import steve6472.polyground.world.generator.feature.*;
import steve6472.polyground.world.generator.feature.cave.OreVein;

import java.util.HashMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 19.08.2020
 * Project: CaveGame
 *
 ***********************/
public class FeatureRegistry
{
	private static final HashMap<String, FeatureEntry> REGISTRY = new HashMap<>();

	public static final FeatureEntry STACKABLE_PILLAR = register("stackable_pillar", StackablePillarFeature::new);
	public static final FeatureEntry PILLAR = register("pillar", PillarFeature::new);
	public static final FeatureEntry VEGETATION_PATCH = register("vegetation_patch", VegetationPatchFeature::new);
	public static final FeatureEntry TREE = register("tree", TreeFeature::new);
	public static final FeatureEntry SAPLING = register("sapling", SaplingFeature::new);
	public static final FeatureEntry TOP_SNOW = register("top_snow", TopSnowFeature::new);
	public static final FeatureEntry ORE_VEIN = register("ore_vein", OreVein::new);
	public static final FeatureEntry TILE_REPLACE_PATCH = register("tile_replace_patch", TileReplacePatchFeature::new);

	public static FeatureEntry register(String featureName, IFeatureFactory factory)
	{
		FeatureEntry entry = new FeatureEntry(featureName, factory);
		REGISTRY.put(featureName, entry);
		return entry;
	}

	public static Feature createFeature(String name, JSONObject json)
	{
		return REGISTRY.get(name).createNew(json);
	}
}
