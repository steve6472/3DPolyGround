package steve6472.polyground.world.generator;

import org.joml.SimplexNoise;
import steve6472.polyground.block.Block;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.world.biomes.registry.BiomeRegistry;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.main.TriConsumer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.11.2019
 * Project: SJP
 *
 ***********************/
public class CaveGenerator implements IGenerator
{
	private SubChunk subChunk;

	private long worldSeed = 4;

	@Override
	public void generate(SubChunk subChunk)
	{
		this.subChunk = subChunk;

		Block stone = BlockRegistry.getBlockByName("stone");
		Block cobblestone = BlockRegistry.getBlockByName("cobblestone");
		Block bedrock = BlockRegistry.getBlockByName("bedrock");

//		iterate((x, y, z) -> subChunk.setBlock(x, y, z, stone));
//		long start = System.nanoTime();
		iterate((x, y, z) -> {

			float scale = 3f;

			float s0 = noise(x + subChunk.getX() * 16, y + subChunk.getLayer() * 16, z + subChunk.getZ() * 16, scale * 0.01f);
			float s1 = noise(x + subChunk.getX() * 16, y + subChunk.getLayer() * 16, z + subChunk.getZ() * 16, scale * 0.03f);
			float s2 = noise(x + subChunk.getX() * 16, y + subChunk.getLayer() * 16, z + subChunk.getZ() * 16, scale * 0.03f);

			float mix = s0 * (s1 + 1) * 2f + s2;

			if (subChunk.getLayer() == 0)
			{
				mix *= y / 16f;
				mix -= (-y + 16) / 16f;
			}

			subChunk.getParent().heightMap[x][z] = subChunk.getWorld().getHeight() * 16;
			
			if (subChunk.getLayer() == 0 && y == 0)
				subChunk.getIds()[x][y][z] = bedrock.getId();
			else if (mix < -0.4f)
				subChunk.getIds()[x][y][z] = cobblestone.getId();
//				subChunk.setBlock(x, y, z, cobblestone);
			else if (mix < 0.3f)
//				subChunk.setBlock(x, y, z, stone);
				subChunk.getIds()[x][y][z] = stone.getId();

			subChunk.setBiomeId(x, y, z, BiomeRegistry.crystalCave.getId());
			subChunk.addBiome(BiomeRegistry.crystalCave.getInstance());

//			points[i][j][k] = (s0 * (s1 + 1) * 2f + s2 < 0.3f);
		});
//		System.out.println((System.nanoTime() - start) / 1000000f);

		//		new SphereCaveFeature(8, 8, 8, 6).generate(subChunk, stone.getId());
		//		new SphereCaveFeature(8, 15, 8, 4).generate(subChunk, 0);
		//		new SphereCaveFeature(10, 13, 9, 4).generate(subChunk, 0);

		//		new EllipsoidCaveFeature(8, 8, 8, 7, 4, 7).generate(subChunk, 0);

//		new SphereCaveFeature(8, 24, 8, 20).generate(subChunk, 0);
	}

	private static float noise(float x, float y, float z, float scale)
	{
		return SimplexNoise.noise(x * scale, y * scale, z * scale);
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

	interface ICaveFeature
	{
		void generate(SubChunk subChunk, int block);


	}

	class SphereCaveFeature
	{
		private final int x;
		private final int y;
		private final int z;
		private final int radius;

		public SphereCaveFeature(int x, int y, int z, int radius)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.radius = radius;
		}

		public void generate(SubChunk sc, int block)
		{
			int ox = sc.getX() * 16;
			int oy = sc.getLayer() * 16;
			int oz = sc.getZ() * 16;

			for (int i = 0; i < 16; i++)
			{
				for (int j = 0; j < 16; j++)
				{
					for (int k = 0; k < 16; k++)
					{
						double a = (i - x) + ox;
						double b = (j - y) + oy;
						double c = (k - z) + oz;
						double distance = a * a + b * b + c * c;

						if (distance < radius * radius)
						{
							sc.setBlock(i, j, k, block);
						}
					}
				}
			}
		}
	}

	class EllipsoidCaveFeature
	{
		private final int x;
		private final int y;
		private final int z;
		private final int xRadius;
		private final int yRadius;
		private final int zRadius;

		public EllipsoidCaveFeature(int x, int y, int z, int xRadius, int yRadius, int zRadius)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.xRadius = xRadius;
			this.yRadius = yRadius;
			this.zRadius = zRadius;
		}

		public void generate(SubChunk sc, int block)
		{
			for (int i = -xRadius; i < xRadius; i++)
			{
				for (int j = -yRadius; j < yRadius; j++)
				{
					for (int k = -zRadius; k < zRadius; k++)
					{
						double X = Math.pow(((i + 0.5) / (float) xRadius), 2);
						double Y = Math.pow(((j + 0.5) / (float) yRadius), 2);
						double Z = Math.pow(((k + 0.5) / (float) zRadius), 2);

						if (X + Y + Z < 1)
						{
							sc.setBlock(i + x, j + y, k + z, block);
						}
					}
				}
			}
		}
	}
}
