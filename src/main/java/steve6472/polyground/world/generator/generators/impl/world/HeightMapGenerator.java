package steve6472.polyground.world.generator.generators.impl.world;

import steve6472.polyground.SSimplexNoise;
import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.generator.generators.IBiomeGenerator;
import steve6472.polyground.world.generator.generators.IHeightMapGenerator;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.07.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class HeightMapGenerator implements IHeightMapGenerator
{
	private final IBiomeGenerator biomeGenerator;
	private final int avg, expand;
	private final SSimplexNoise noise;

	public HeightMapGenerator(IBiomeGenerator biomeGenerator, int avg, int expand)
	{
		this.biomeGenerator = biomeGenerator;
		this.avg = avg;
		this.expand = expand;
		this.noise = new SSimplexNoise(biomeGenerator.getSeed());
	}

	@Override
	public float[] generate(int cx, int cz)
	{
		float[][] arr = new float[16 + expand * 2][16 + expand * 2];

		for (int x = -expand; x < 16 + expand; ++x)
		{
			for (int z = -expand; z < 16 + expand; ++z)
			{
				arr[x + expand][z + expand] = getHeight(x + cx * 16, z + cz * 16);
			}
		}

		float[] res = new float[16 * 16];

		for (int x = 0; x < 16; ++x)
		{
			for (int z = 0; z < 16; ++z)
			{
				float t = 0;
				for (int i = -avg / 2; i < avg / 2 + 1; i++)
				{
					for (int j = -avg / 2; j < avg / 2 + 1; j++)
					{
						t += arr[x + expand + i][z + expand +  j];
					}
				}
				res[x + z * 16] = t / (float) (avg * avg);
			}
		}

		return res;
	}

	@Override
	public IBiomeGenerator getBiomeGenerator()
	{
		return biomeGenerator;
	}

	private float getHeight(int x, int z)
	{
		Biome biome = biomeGenerator.getBiome(x, 0, z);

		int itCo = biome.getIterationCount();
		float persistance = biome.getPersistance();
		float low = biome.getLow();
		float high = biome.getHigh();
		float scale = biome.getScale();

		return noise.sumOcatave(itCo, x, z, persistance, scale, low, high);
	}
}
