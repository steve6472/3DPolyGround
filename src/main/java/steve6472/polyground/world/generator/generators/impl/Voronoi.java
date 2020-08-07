package steve6472.polyground.world.generator.generators.impl;

import org.joml.Vector2d;
import org.joml.Vector3i;
import steve6472.polyground.SSimplexNoise;
import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.biomes.Biomes;
import steve6472.polyground.world.generator.generators.IBiomeGenerator;
import steve6472.sge.main.game.GridStorage;
import steve6472.sge.main.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 29.07.2020
 * Project: ThreadedGenerator
 *
 * Generates 2D Biome map
 * 'y' parameter is ignored
 *
 ***********************/
public class Voronoi implements IBiomeGenerator
{
	private final int spread;       // tiles
	private final int seedJitter;   // tiles
	private final int searchRadius; // grids

	private final long seed;

	private final SSimplexNoise[] noises;
	private final SSimplexNoise jitterNoise;

	private final GridStorage<Vector3i> seedCache;

	/**
	 *
	 * @param spread (tiles)
	 * @param seedJitter (tiles)
	 * @param searchRadius (grids)
	 */
	public Voronoi(long seed, int spread, int seedJitter, int searchRadius)
	{
		this.seed = seed;
		this.spread = spread;
		this.seedJitter = seedJitter;
		this.searchRadius = searchRadius;

		noises = new SSimplexNoise[] {
			new SSimplexNoise(seed >> 1),
			new SSimplexNoise(seed >> 2)
		};

		jitterNoise = new SSimplexNoise(seed >> 32);

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
		float[] targets = new float[noises.length];
		for (int i = 0; i < targets.length; i++)
			targets[i] = noises[i].noise(x * scale, z * scale);

		float[] diffs = new float[Biomes.count()];

		for (int i = 0; i < Biomes.count(); i++)
		{
			float totalDiff = 0;
			for (int j = 0; j < targets.length; j++)
				totalDiff += Math.abs(Biomes.getBiome(i + 1).getParameters()[j] - targets[j]);
			diffs[i] = totalDiff / (float) targets.length;
		}

		int index = 0;
		float diff = diffs[index];

		List<Integer> same = null;

		for (int i = 1; i < diffs.length; i++)
		{
			float v = diffs[i];

			if (v == diff)
			{
				if (same == null)
					same = new ArrayList<>();
				same.add(index);
			} else if (v < diff)
			{
				index = i;
				diff = v;
			}
		}

		if (same != null && !same.isEmpty())
		{
			return Biomes.getBiome(new Random(getBiomeSeed(x, z)).nextInt(same.size()) + 1);
		}

		return Biomes.getBiome(index + 1);
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
