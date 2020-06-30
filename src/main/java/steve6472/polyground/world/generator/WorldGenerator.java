package steve6472.polyground.world.generator;

import org.joml.SimplexNoise;
import org.joml.Vector2f;
import org.joml.Vector3f;
import steve6472.polyground.block.Block;
import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.biomes.registry.BiomeEntry;
import steve6472.polyground.world.biomes.registry.BiomeRegistry;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.main.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 14.09.2019
 * Project: SJP
 *
 ***********************/
public class WorldGenerator implements IGenerator
{
	private SubChunk subChunk;
	private int lastX, lastZ;

	private float[] heightMap;

	List<Biome> biomes;
	final static int CHECK_SIZE = 8;
	final static int SIZE = 13;
	final static int SPACING = 6;
	final static int JITTER = SPACING * 8 - 1 - 16;

	Vector3f[][] points;

	public WorldGenerator()
	{
		biomes = new ArrayList<>();
		for (BiomeEntry<? extends Biome> c : BiomeRegistry.getEntries())
		{
			if (c == BiomeRegistry.voidBiome)
				continue;

			biomes.add(c.getInstance());
		}

		points = new Vector3f[SIZE][SIZE];
	}

	@Override
	public void generate(SubChunk subChunk)
	{
		this.subChunk = subChunk;

		if (lastX != subChunk.getX() || lastZ != subChunk.getZ())
			createHeightMap();
		createSubChunk();

		lastX = subChunk.getX();
		lastZ = subChunk.getZ();
	}

	private void createHeightMap()
	{
		for (int i = -SIZE / 2; i <= SIZE / 2; i++)
		{
			for (int j = -SIZE / 2; j <= SIZE / 2; j++)
			{
				Vector3f v = getSeed(i + subChunk.getX(), j + subChunk.getZ());
				points[i + SIZE / 2][j + SIZE / 2] = v;
			}
		}

		int a = 10;
//		heightMap = getHeights(5, 2);
		heightMap = getHeights(a, a / 2);
	}

	private void createSubChunk()
	{
		for (int i = 0; i < subChunk.getIds().length; i++)
		{
			for (int j = 0; j < subChunk.getIds()[i].length; j++)
			{
				for (int k = 0; k < subChunk.getIds()[i][j].length; k++)
				{
					int maxHeight = (int) Math.ceil(heightMap[(i) + (k * 16)]);

					Biome b = getBiome(i + subChunk.getX() * 16, k + subChunk.getZ() * 16);
					subChunk.addBiome(b);

					int top = b.getTopBlock().getId();
					int fill = b.getCaveBlock().getId();
					int under = b.getUnderBlock().getId();

					int y = j + subChunk.getLayer() * 16;

					if (y == maxHeight)
					{
						subChunk.getParent().heightMap[i][k] = y;
						subChunk.getIds()[i][j][k] = top;
					} else if (y < maxHeight - 1 - b.getUnderLayerHeight())
					{
						subChunk.getIds()[i][j][k] = fill;
					} else if (y < maxHeight)
					{
						subChunk.getIds()[i][j][k] = under;
					} else
					{
						subChunk.getIds()[i][j][k] = Block.air.getId();
					}

					subChunk.setBiomeId(i, j, k, BiomeRegistry.getBiomeEntry(b.getName()).getId());
				}
			}
		}
	}

	private float sumOcatave(int num_iterations, float x, float y, float persistence, float scale, float low, float high)
	{
		float maxAmp = 0;
		float amp = 1;
		float freq = scale;
		float noise = 0;

		for (int i = 0; i < num_iterations; i++)
		{
			noise += SimplexNoise.noise(x * freq, y * freq) * amp;
			maxAmp += amp;
			amp *= persistence;
			freq *= 2f;
		}
		noise /= maxAmp;

		noise = noise * (high - low) / 2f + (high + low) / 2f;

		return noise;
	}

	public float[] getHeights(int avg, int expand)
	{
		float[][] arr = new float[16 + expand * 2][16 + expand * 2];

		for (int x = -expand; x < 16 + expand; ++x)
		{
			for (int z = -expand; z < 16 + expand; ++z)
			{
				arr[x + expand][z + expand] = getH(x + subChunk.getX() * 16, z + subChunk.getZ() * 16);
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

	private Biome getBiome(float x, float z)
	{
		float jitter = sumOcatave(8, x, z, 0.4f, 0.02f, -20, 20);
		x += jitter;
		z += jitter;

		double lastDist = Float.MAX_VALUE;
		int biome = 0;

		for (int i = -SIZE / 2; i <= SIZE / 2; i++)
		{
			for (int j = -SIZE / 2; j <= SIZE / 2; j++)
			{
				Vector3f v = points[i + SIZE / 2][j + SIZE / 2];
				if (v == null)
					continue;
				double dist = Vector2f.distance(x, z, v.x, v.y);
				if (dist < CHECK_SIZE * 16 && dist < lastDist)
				{
					lastDist = dist;
					biome = (int) v.z;
				}
			}
		}

//		float f = sumOcatave(16, x, z, 0.4f, 0.003f, 0, biomes.size());
//		return biomes.get((int) Math.floor(f));
		return biomes.get(biome);
	}

	private Vector3f getSeed(int scx, int scz)
	{
		if (!(scx % SPACING == 0 && scz % SPACING == 0))
			return null;

		long seed0 =
			(int) (scx * scx * 0xc5859e) +
				(int) (scx * 0x9ab4aa) +
				(int) (scz * scz) * 0x041881L +
				(int) (scz * 0xdafda5) ^ 0x4ec291;
		long seed1 =
			(int) (scx * scx * 0x668298) +
				(int) (scx * 0xe391ad) +
				(int) (scz * scz) * 0x0daf9bL +
				(int) (scz * 0x2996f4) ^ 0x5ae13f;
		long seed2 =
			(int) (scx * scx * 0x6aeffe) +
				(int) (scx * 0xca8aa2) +
				(int) (scz * scz) * 0xf8f4bdL +
				(int) (scz * 0x7e8ad6) ^ 0x76b9eaf;

		return new Vector3f(
			RandomUtil.randomInt(-JITTER, JITTER, seed0) + (scx << 4),
			RandomUtil.randomInt(-JITTER, JITTER, seed1) + (scz << 4),
			RandomUtil.randomInt(0, biomes.size() - 1, seed2));
	}

	public float getH(float x, float z)
	{
		Biome biome = getBiome(x, z);

		int itCo;
		float persistance, low, high, scale;

		itCo = biome.getIterationCount();
		persistance = biome.getPersistance();
		low = biome.getLow();
		high = biome.getHigh();
		scale = biome.getScale();

		return sumOcatave(itCo, x, z, persistance, scale, low, high);
	}
}
