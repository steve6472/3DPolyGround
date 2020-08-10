package steve6472.polyground.world.generator.generators.impl.world;

import org.joml.Vector2d;
import org.joml.Vector3i;
import steve6472.polyground.SSimplexNoise;
import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.biomes.Biomes;
import steve6472.polyground.world.generator.generators.IBiomeGenerator;
import steve6472.sge.main.game.GridStorage;
import steve6472.sge.main.util.RandomUtil;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 29.07.2020
 * Project: ThreadedGenerator
 *
 * Generates 2D Biome map
 * 'y' parameter is ignored
 *
 ***********************/
public class VoronoiBiomeGen implements IBiomeGenerator
{
	private final int spread;       // tiles
	private final int seedJitter;   // tiles
	private final int searchRadius; // grids

	private final long seed;

	private final SSimplexNoise[] noises;
	private final SSimplexNoise jitterNoise;

	private final GridStorage<Vector3i> seedCache;

	private final List<Biome> biomeSet;

	/**
	 *
	 * @param spread (tiles)
	 * @param seedJitter (tiles)
	 * @param searchRadius (grids)
	 */
	public VoronoiBiomeGen(long seed, int spread, int seedJitter, int searchRadius, List<Biome> biomes)
	{
		this.seed = seed;
		this.spread = spread;
		this.seedJitter = seedJitter;
		this.searchRadius = searchRadius;

		noises = new SSimplexNoise[] {
//			new SSimplexNoise(seed >> 1),
//			new SSimplexNoise((seed * 8) >> 2),
//			new SSimplexNoise((seed * 16) >> 3),
//			new SSimplexNoise((seed * 32) >> 4)
			new SSimplexNoise(-8376544303500664650L),
			new SSimplexNoise(3096958501783446189L),
			new SSimplexNoise(5852975705126687327L),
			new SSimplexNoise(-319185951298012992L)
		};

		jitterNoise = new SSimplexNoise(seed >> 32);

		this.biomeSet = biomes;

		// TODO: remove undusable after some time
		seedCache = new GridStorage<>();
	}

	public int getBiomeId(int x, int y, int z)
	{
		int gx = x / spread;
		int gz = z / spread;

		float jitter = jitterNoise.sumOcatave(4, x, z, 0.35f, 0.015f, -20, 20);
		x += jitter;
		z += jitter;

		for (int i = 0; i < searchRadius * 2 + 1; i++)
		{
			for (int j = 0; j < searchRadius * 2 + 1; j++)
			{
				if (seedCache.get(i + gx - searchRadius, j + gz - searchRadius) == null)
					seedCache.put(i + gx - searchRadius, j + gz - searchRadius, getSeedForGrid(i + gx - searchRadius, j + gz - searchRadius));
			}
		}

		double lastDistance = Double.MAX_VALUE;
		int biome = 0;

		for (int i = -searchRadius; i <= searchRadius; i++)
		{
			for (int j = -searchRadius; j <= searchRadius; j++)
			{
				Vector3i v = seedCache.get(i + gx, j + gz);
				double dis = Vector2d.distance(x, z, v.x, v.y);
				if (dis < lastDistance)
				{
					biome = v.z;
					lastDistance = dis;
				}
			}
		}

		return biome;
	}

	public Biome getBiome(int x, int y, int z)
	{
		return Biomes.getBiome(getBiomeId(x, y, z));
	}

	private Vector3i getSeedForGrid(int gx, int gz)
	{
		long seed0 =
			(gx * gx * 0xc5859e) + (gx * 0x9ab4aa) +
				(gz * gz) * getSeed() + (gz * 0xdafda5) ^ 0x4ec291;
		long seed1 =
			(gx * gx * 0x668298) + (gx * 0xe391ad) +
				(gz * gz) * getSeed() + (gz * 0x2996f4) ^ 0x5ae13f;

		int x = gx * spread + spread / 2 + RandomUtil.randomInt(-seedJitter, seedJitter, seed0);
		int z = gz * spread + spread / 2 + RandomUtil.randomInt(-seedJitter, seedJitter, seed1);

		//RandomUtil.randomInt(0, maxBiomes, getBiomeSeed(gx, gz))
		return new Vector3i(x, z, findBiome(x, z).getId());
	}

	private Biome findBiome(int x, int z)
	{
		float scale = 0.0025f;

		float[] n = new float[this.noises.length];
		for (int i = 0; i < n.length; i++)
			n[i] = this.noises[i].noise(x * scale, z * scale);

		float[] fitness = new float[biomeSet.size()];

		for (int i = 0; i < biomeSet.size(); i++)
		{
			Biome b = biomeSet.get(i);
			float[] p = b.getParameters();
			// Don't ask, I aint changing it for a while, and yes, I know I don't need to match indices of noises with biome parameters
			fitness[i] = fitness(p[1], p[3], p[0], p[2], n[1], n[3], n[0], n[2]);
		}

		int index = 0;
		float min = fitness[0];

		for (int i = 1; i < fitness.length; i++)
		{
			if (fitness[i] < min)
			{
				index = i;
				min = fitness[i];
			}
		}

		return biomeSet.get(index);
	}

	private static float fitness(float biomeTemperature, float biomeHumidity, float biomeAltitude, float biomeWeirdness,
	                             float noiseTemperature, float noiseHumidity, float noiseAltitude, float noiseWeirdness)
	{
		return (biomeTemperature - noiseTemperature) * (biomeTemperature - noiseTemperature)
			+ (biomeHumidity - noiseHumidity) * (biomeHumidity - noiseHumidity)
			+ (biomeAltitude - noiseAltitude) * (biomeAltitude - noiseAltitude)
			+ (biomeWeirdness - noiseWeirdness) * (biomeWeirdness - noiseWeirdness);
	}

	private long getBiomeSeed(int gx, int gz)
	{
		return (long) (gx * gx * 0x6aeffe) + (gx * 0xca8aa2) +
			(gz * gz) * getSeed() + (gz * 0x7e8ad6) ^ 0x76b9eaf;
	}

	@Override
	public long getSeed()
	{
		return seed;
	}
}
