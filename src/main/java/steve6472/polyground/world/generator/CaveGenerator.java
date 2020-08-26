package steve6472.polyground.world.generator;

import org.joml.SimplexNoise;
import steve6472.polyground.block.Block;
import steve6472.polyground.world.chunk.SubChunk;
import steve6472.sge.main.TriConsumer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.11.2019
 * Project: SJP
 *
 ***********************/
public class CaveGenerator
{
	private static float noise(float x, float y, float z, float scale)
	{
		return SimplexNoise.noise(x * scale, y * scale, z * scale);
	}

	private void iterate(TriConsumer<Integer, Integer, Integer> consumer)
	{
		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					consumer.apply(i, j, k);
				}
			}
		}
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

		public void generate(SubChunk sc, Block block)
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
							sc.setBlock(block, i, j, k);
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

		public void generate(SubChunk sc, Block block)
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
							sc.setBlock(block, i + x, j + y, k + z);
						}
					}
				}
			}
		}
	}
}
