package com.steve6472.polyground.world.generator;

import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.world.SubChunk;
import org.joml.SimplexNoise;

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

	@Override
	public void generate(SubChunk subChunk)
	{
		this.subChunk = subChunk;

		int grass = BlockRegistry.getBlockIdByName("grass");
		int dirt = BlockRegistry.getBlockIdByName("gravel");
		int stone = BlockRegistry.getBlockIdByName("stone");
		int cobblestone = BlockRegistry.getBlockIdByName("cobblestone");

		generateBiomes();
		generateHeightMap();
	}

	private void generateHeightMap()
	{
		int stone = BlockRegistry.getBlockIdByName("stone");

		iterate((i, j, k, biome) ->
		{
			float height =
			switch (biome)
			{
				case 0 -> sumOcatave(16, i + subChunk.getX() * 16, k + subChunk.getZ() * 16, 0.4f, 0.007f, 0, 31);
				case 1 -> sumOcatave(4, i + subChunk.getX() * 16, k + subChunk.getZ() * 16, 0.4f, 0.007f, 0, 63);
				case 2 -> sumOcatave(8, i + subChunk.getX() * 16, k + subChunk.getZ() * 16, 0.4f, 0.007f, 0, 31);
				default -> 1;
			};

			if (height > j + subChunk.getLayer() * 16)
			{
				subChunk.getIds()[i][j][k] = stone;
			} else
			{
				subChunk.getIds()[i][j][k] = Block.air.getId();
			}
		});
	}

	private void generateBiomes()
	{
		iterate((i, j, k) ->
		{
			float biome = sumOcatave(4, i + subChunk.getX() * 16, k + subChunk.getZ() * 16, 0.4f, 0.025f, 0, 3);
			subChunk.getIds()[i][j][k] = (int) Math.ceil(biome);

//			float height = sumOcatave(8, i + subChunk.getX() * 16, k + subChunk.getZ() * 16, 0.4f, 0.007f, 0, 31);
//			if (height > j + subChunk.getLayer() * 16)
//			{
//				subChunk.getIds()[i][j][k] =
//					switch ((int) Math.ceil(biome))
//						{
//							case 1 -> stone;
//							case 2 -> dirt;
//							case 3 -> cobblestone;
//							default -> BlockRegistry.getBlockByName("debug").getId();
//						};
//			} else
//			{
//				subChunk.getIds()[i][j][k] = Block.air.getId();
//			}
		});
	}

	private void iterate(TriConsumer consumer)
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

	private void iterate(QuadConsumer consumer)
	{
		iterate((i, j, k) -> {
			consumer.apply(i, j, k, subChunk.getIds()[i][j][k]);
		});
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

        for(int i = 0; i < num_iterations; i++)
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

	@FunctionalInterface
	private interface TriConsumer
	{
		void apply(int x, int y, int z);
	}

	@FunctionalInterface
	private interface QuadConsumer
	{
		void apply(int x, int y, int z, int id);
	}
}
