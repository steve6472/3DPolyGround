package steve6472.polyground.world.biomes;

import org.joml.Vector3f;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.generator.EnumFeatureStage;
import steve6472.polyground.world.generator.IFeature;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 04.08.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class DataBiome extends Biome
{
	private final String name;
	private final BlockState topTile, underTile, caveTile;
	private final int biomeHeight, underLayerHeight, iterationCount;
	private final Vector3f foliageColor;
	private final float persistance, scale, low, high;
	private float[] parameters;
	private float altitude, temperature, weirdness, humidity;

	public DataBiome(String name, BlockState topTile, BlockState underTile, BlockState caveTile, int biomeHeight, int underLayerHeight, int iterationCount, Vector3f foliageColor, float persistance, float scale, float low, float high)
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

	public DataBiome feature(EnumFeatureStage stage, double chance, IFeature feature)
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
