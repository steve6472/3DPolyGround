package steve6472.polyground.generator.biome;

import org.joml.Vector3f;
import org.json.JSONArray;
import org.json.JSONObject;
import steve6472.polyground.generator.BlockBuilder;
import steve6472.polyground.generator.DataGenerator;
import steve6472.polyground.world.generator.EnumFeatureStage;
import steve6472.sge.main.util.Triple;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 16.08.2020
 * Project: CaveGame
 *
 ***********************/
public class BiomeBuilder
{
	private final Vector3f foliageColor;
	private final List<Triple<EnumFeatureStage, Double, String>> features;
	private String name;
	private String topBlock, underBlock, caveBlock;
	private int biomeHeight;
	private int underLayerHeight;
	private int iterationCount;
	private float persistance;
	private float scale;
	private float low;
	private float high;
	private float altitude;
	private float temperature;
	private float weirdness;
	private float humidity;
	private boolean generatesNaturally;

	private BiomeBuilder()
	{
		this.foliageColor = new Vector3f();
		features = new ArrayList<>();
		generatesNaturally = true;
	}

	/* */

	public BiomeBuilder surface(String topBlock, String underBlock, String caveBlock, int underLayerHeight, int biomeHeight)
	{
		this.topBlock = topBlock;
		this.underBlock = underBlock;
		this.caveBlock = caveBlock;
		this.underLayerHeight = underLayerHeight;
		this.biomeHeight = biomeHeight;
		return this;
	}

	public BiomeBuilder heightMap(int iterationCount, float persistance, float scale, float low, float high)
	{
		this.iterationCount = iterationCount;
		this.persistance = persistance;
		this.scale = scale;
		this.low = low;
		this.high = high;
		return this;
	}

	public BiomeBuilder climate(float altitude, float temperature, float weirdness, float humidity)
	{
		this.altitude = altitude;
		this.temperature = temperature;
		this.weirdness = weirdness;
		this.humidity = humidity;
		return this;
	}

	/* */

	public BiomeBuilder natural(boolean generatesNaturally)
	{
		this.generatesNaturally = generatesNaturally;
		return this;
	}

	public BiomeBuilder high(float high)
	{
		this.high = high;
		return this;
	}

	public BiomeBuilder low(float low)
	{
		this.low = low;
		return this;
	}

	public BiomeBuilder scale(float scale)
	{
		this.scale = scale;
		return this;
	}

	public BiomeBuilder persistance(float persistance)
	{
		this.persistance = persistance;
		return this;
	}

	public BiomeBuilder foliageColor(float r, float g, float b)
	{
		this.foliageColor.set(r, g, b);
		return this;
	}

	public BiomeBuilder iterationCount(int iterationCount)
	{
		this.iterationCount = iterationCount;
		return this;
	}

	public BiomeBuilder underLayerHeight(int underLayerHeight)
	{
		this.underLayerHeight = underLayerHeight;
		return this;
	}

	public BiomeBuilder biomeHeight(int biomeHeight)
	{
		this.biomeHeight = biomeHeight;
		return this;
	}

	public BiomeBuilder name(String name)
	{
		this.name = name;
		return this;
	}

	public BiomeBuilder topBlock(String topBlock)
	{
		this.topBlock = topBlock;
		return this;
	}

	public BiomeBuilder underBlock(String underBlock)
	{
		this.underBlock = underBlock;
		return this;
	}

	public BiomeBuilder caveBlock(String caveBlock)
	{
		this.caveBlock = caveBlock;
		return this;
	}

	public BiomeBuilder altitude(float altitude)
	{
		this.altitude = altitude;
		return this;
	}

	public BiomeBuilder temperature(float temperature)
	{
		this.temperature = temperature;
		return this;
	}

	public BiomeBuilder weirdness(float weirdness)
	{
		this.weirdness = weirdness;
		return this;
	}

	public BiomeBuilder humidity(float humidity)
	{
		this.humidity = humidity;
		return this;
	}

	/* */

	public BiomeBuilder feature(EnumFeatureStage stage, double chance, String feature)
	{
		this.features.add(new Triple<>(stage, chance, feature));
		return this;
	}

	/* */

	public static BiomeBuilder create()
	{
		return new BiomeBuilder();
	}

	public JSONObject build()
	{
		JSONObject main = new JSONObject();
		main.put("name", name);
		main.put("foliage_color", new float[] {foliageColor.x, foliageColor.y, foliageColor.z});
		main.put("natural", generatesNaturally);

		JSONObject surface = new JSONObject();
		surface.put("top_block", topBlock);
		surface.put("under_block", underBlock);
		surface.put("cave_block", caveBlock);
		surface.put("under_layer_leight", underLayerHeight);
		surface.put("biome_height", biomeHeight);

		JSONObject heightMapParameters = new JSONObject();
		heightMapParameters.put("iterations", iterationCount);
		heightMapParameters.put("persistance", persistance);
		heightMapParameters.put("scale", scale);
		heightMapParameters.put("low", low);
		heightMapParameters.put("high", high);

		JSONObject climateParameters = new JSONObject();
		climateParameters.put("altitude", altitude);
		climateParameters.put("temperature", temperature);
		climateParameters.put("weirdness", weirdness);
		climateParameters.put("humidity", humidity);

		if (!features.isEmpty())
		{
			JSONArray features = new JSONArray();
			for (Triple<EnumFeatureStage, Double, String> t : this.features)
			{
				JSONObject feature = new JSONObject();
				feature.put("stage", t.a());
				feature.put("chance", t.b());
				feature.put("name", t.c());
				features.put(feature);
			}
			main.put("features", features);
		}

		main.put("climate_parameters", climateParameters);
		main.put("height_map_parameters", heightMapParameters);
		main.put("surface", surface);

		return main;
	}

	public void generate()
	{
		System.out.println("Generating biome " + name);
		File biome = new File(DataGenerator.BIOMES, name + ".json");
		try
		{
			if (biome.createNewFile())
			{
				System.out.println("Created biome " + biome.getPath());
			}
		} catch (IOException e)
		{
			System.err.println("Error while creating biome " + name);
			e.printStackTrace();
		}

		BlockBuilder.save(biome, build());
	}
}
