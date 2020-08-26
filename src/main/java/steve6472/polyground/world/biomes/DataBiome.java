package steve6472.polyground.world.biomes;

import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.generator.EnumFeatureStage;
import steve6472.polyground.world.generator.Feature;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 04.08.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class DataBiome extends Biome
{
	private final String name;
	private BlockState topTile, underTile, caveTile;
	private final int biomeHeight, underLayerHeight, iterationCount;
	private final Vector3f foliageColor;
	private final float persistance, scale, low, high;
	private float[] parameters;
	private float altitude, temperature, weirdness, humidity;
	private boolean natural;

	public DataBiome(JSONObject json, Features features)
	{
		super();
		name = json.getString("name");
		JSONArray foliageArray = json.getJSONArray("foliage_color");
		foliageColor = new Vector3f(foliageArray.getFloat(0), foliageArray.getFloat(1), foliageArray.getFloat(2));
		natural = json.getBoolean("natural");

		JSONObject surface = json.getJSONObject("surface");
		topTile = Blocks.getDefaultState(surface.getString("top_block"));
		underTile = Blocks.getDefaultState(surface.getString("under_block"));
		caveTile = Blocks.getDefaultState(surface.getString("cave_block"));
		underLayerHeight = surface.getInt("under_layer_leight");
		biomeHeight = surface.getInt("biome_height");

		JSONObject climateParameters = json.getJSONObject("climate_parameters");
		altitude = climateParameters.getFloat("altitude");
		temperature = climateParameters.getFloat("temperature");
		weirdness = climateParameters.getFloat("weirdness");
		humidity = climateParameters.getFloat("humidity");

		JSONObject heightMapParameters = json.getJSONObject("height_map_parameters");
		iterationCount = heightMapParameters.getInt("iterations");
		persistance = heightMapParameters.getFloat("persistance");
		scale = heightMapParameters.getFloat("scale");
		low = heightMapParameters.getFloat("low");
		high = heightMapParameters.getFloat("high");

		if (json.has("features"))
		{
			JSONArray fs = json.getJSONArray("features");
			for (int i = 0; i < fs.length(); i++)
			{
				JSONObject feature = fs.getJSONObject(i);
				EnumFeatureStage stage = feature.getEnum(EnumFeatureStage.class, "stage");
				double chance = feature.getDouble("chance");
				String name = feature.getString("name");
				addFeature(stage, chance, features.getFeature(name));
			}
		}

		parameters = new float[] {altitude, temperature, weirdness, humidity};
	}

	public DataBiome(String name, BlockState topTile, BlockState underTile, BlockState caveTile, int underLayerHeight, int biomeHeight, int iterationCount, float persistance, float scale, float low, float high, Vector3f foliageColor)
	{
		super();
		this.name = name;
		this.topTile = topTile;
		this.underTile = underTile;
		this.caveTile = caveTile;
		this.biomeHeight = biomeHeight;
		this.underLayerHeight = underLayerHeight;
		this.iterationCount = iterationCount;
		this.foliageColor = foliageColor;
		this.persistance = persistance;
		this.scale = scale;
		this.low = low;
		this.high = high;
	}

	public DataBiome caveTile(BlockState caveTile)
	{
		this.caveTile = caveTile;
		return this;
	}

	public DataBiome underTile(BlockState underTile)
	{
		this.underTile = underTile;
		return this;
	}

	public DataBiome topTile(BlockState topTile)
	{
		this.topTile = topTile;
		return this;
	}

	public DataBiome altitude(float altitude)
	{
		this.altitude = altitude;
		return this;
	}

	public DataBiome temperature(float temperature)
	{
		this.temperature = temperature;
		return this;
	}

	public DataBiome weirdness(float weirdness)
	{
		this.weirdness = weirdness;
		return this;
	}

	public DataBiome humidity(float humidity)
	{
		this.humidity = humidity;
		return this;
	}

	public DataBiome create()
	{
		parameters = new float[] {altitude, temperature, weirdness, humidity};
		return this;
	}

	public DataBiome feature(EnumFeatureStage stage, double chance, Feature feature)
	{
		super.addFeature(stage, chance, feature);
		return this;
	}

	@Override
	public void addFeatures()
	{

	}

	@Override
	public float[] getParameters()
	{
		return parameters;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public BlockState getTopBlock()
	{
		return topTile;
	}

	@Override
	public BlockState getUnderBlock()
	{
		return underTile;
	}

	@Override
	public BlockState getCaveBlock()
	{
		return caveTile;
	}

	/**
	 * Indicates how high the biome will stretch above surface
	 *
	 * @return biome height
	 */
	@Override
	public int getBiomeHeight()
	{
		return biomeHeight;
	}

	@Override
	public int getUnderLayerHeight()
	{
		return underLayerHeight;
	}

	@Override
	public Vector3f getColor()
	{
		return foliageColor;
	}

	@Override
	public boolean generatesNaturally()
	{
		return natural;
	}

	@Override
	public int getIterationCount()
	{
		return iterationCount;
	}

	@Override
	public float getPersistance()
	{
		return persistance;
	}

	@Override
	public float getScale()
	{
		return scale;
	}

	@Override
	public float getLow()
	{
		return low;
	}

	@Override
	public float getHigh()
	{
		return high;
	}
}
