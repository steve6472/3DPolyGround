package steve6472.polyground.world.generator;

import steve6472.polyground.block.Block;
import steve6472.polyground.world.biomes.Biome;
import steve6472.polyground.world.biomes.BlueLand;
import steve6472.polyground.world.biomes.GreenLand;
import steve6472.polyground.world.biomes.RedLand;
import steve6472.polyground.world.biomes.registry.BiomeRegistry;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.main.TriConsumer;
import org.joml.SimplexNoise;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 14.09.2019
 * Project: SJP
 *
 ***********************/
public class WorldGenerator implements IGenerator
{
	private SubChunk subChunk;

	private float[] heightMap;

	List<Biome> biomes;

	public WorldGenerator()
	{
		biomes = new ArrayList<>();
		BiomeRegistry.getEntries().forEach(c -> biomes.add(c.createNew()));
	}

	@Override
	public void generate(SubChunk subChunk)
	{
		this.subChunk = subChunk;

		createHeightMap();
		createWorld();
	}

	private void createHeightMap()
	{
		heightMap = getHeights(subChunk.getX() * 16, subChunk.getZ() * 16, subChunk.getX() * 16 + 16, subChunk.getZ() * 16 + 16);
	}

	private void createWorld()
	{
		iterate((i, j, k) ->
		{
			int maxHeight = (int) Math.ceil(heightMap[(i) + (k * 16)]);

			Biome b = getBiome(i, k);
			int top = b.getTopBlock().getId();
			int fill = b.getCaveBlock().getId();
			int under = b.getUnderBlock().getId();

			int y = j + subChunk.getLayer() * 16;

			if (y == maxHeight)
			{
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
		});

		iterate((x, y, z) ->
		{
			Biome b = getBiome(x, z);
			int id = 0;
			if (b instanceof RedLand)
				id = 1;
			if (b instanceof GreenLand)
				id = 2;
			if (b instanceof BlueLand)
				id = 3;
			subChunk.setBiomeId(x, y, z, id);
		});
	}

	private void iterate(TriConsumer<Integer, Integer, Integer> consumer)
	{
		for (int i = 0; i < subChunk.getIds().length; i++)
		{
			for (int j = 0; j < subChunk.getIds()[i].length; j++)
			{
				for (int k = 0; k < subChunk.getIds()[i][j].length; k++)
				{
					consumer.apply(i, j, k);
				}
			}
		}
	}

	private void iterate(int y, BiConsumer<Integer, Integer> consumer)
	{
		for (int i = 0; i < subChunk.getIds().length; i++)
		{
			for (int j = 0; j < subChunk.getIds()[i][y].length; j++)
			{
				consumer.accept(i, j);
			}
		}
	}

	private float sumOcatave(int num_iterations, int x, int y, float persistence, float scale, float low, float high)
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

	public static float smoothInterpolation(float bottomLeft, float topLeft, float bottomRight, float topRight, float xMin, float xMax, float zMin, float zMax, float x, float z)
	{
		float width = xMax - xMin, height = zMax - zMin;
		float xValue = 1 - (x - xMin) / width;
		float zValue = 1 - (z - zMin) / height;

		float a = smoothstep(bottomLeft, bottomRight, xValue);
		float b = smoothstep(topLeft, topRight, xValue);
		return smoothstep(a, b, zValue);
	}

	public static float smoothstep(float edge0, float edge1, float x)
	{
		// Scale, bias and saturate x to 0..1 range
		x = x * x * (3 - 2 * x);
		// Evaluate polynomial
		return (edge0 * x) + (edge1 * (1 - x));
	}


	public float[] getHeights(int xMin, int zMin, int xMax, int zMax)
	{
		float[] arr = new float[(xMax - xMin) * (zMax - zMin)];

		float f0 = getH(0, 0);
		float f1 = getH(0, 16);
		float f2 = getH(16, 0);
		float f3 = getH(16, 16);

		for (int x = xMin; x < xMax; ++x)
		{
			for (int z = zMin; z < zMax; ++z)
			{
				float h = smoothInterpolation(f0, f1, f2, f3, xMin, xMax, zMin, zMax, x, z);

				arr[(x - xMin) + (z - zMin) * (zMax - zMin)] = h;
			}
		}

		return arr;
	}

	private Biome getBiome(int x, int z)
	{
		float f = sumOcatave(16, x + subChunk.getX() * 16, z + subChunk.getZ() * 16, 0.4f, 0.007f, 0, 3);

		if (f <= 1)
		{
			return biomes.get(0);
		} else if (f > 1 && f <= 2)
		{
			return biomes.get(1);
		} else
		{
			return biomes.get(2);
		}
	}

	public float getH(int x, int z)
	{
		Biome biome = getBiome(x, z);

		int itCo;
		float persistance, low, high, scale;

		itCo = biome.getIterationCount();
		persistance = biome.getPersistance();
		low = biome.getLow();
		high = biome.getHigh();
		scale = biome.getScale();

		return sumOcatave(itCo, x + subChunk.getX() * 16, z + subChunk.getZ() * 16, persistance, scale, low, high);
	}
}
