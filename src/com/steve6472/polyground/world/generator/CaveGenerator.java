package com.steve6472.polyground.world.generator;

import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.world.SubChunk;

import java.util.function.BiConsumer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.11.2019
 * Project: SJP
 *
 ***********************/
public class CaveGenerator implements IGenerator
{
	private SubChunk subChunk;

	@Override
	public void generate(SubChunk subChunk)
	{
		this.subChunk = subChunk;

		Block stone = BlockRegistry.getBlockByName("stone");

		iterate((x, y, z) -> subChunk.setBlock(x, y, z, 0));

		new SphereCaveFeature(8, 8, 8, 6).generate(subChunk, stone.getId());
		new SphereCaveFeature(8, 15, 8, 4).generate(subChunk, 0);
		new SphereCaveFeature(10, 13, 9, 4).generate(subChunk, 0);
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

	private void iterate(BiConsumer<Integer, Integer> consumer)
	{
		for (int i = 0; i < subChunk.getIds().length; i++)
		{
			for (int k = 0; k < subChunk.getIds()[i][0].length; k++)
			{
				consumer.accept(i, k);
			}
		}
	}

	@FunctionalInterface
	private interface TriConsumer
	{
		void apply(int x, int y, int z);
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

//			FloatingText ft = new FloatingText();
//			ft.setPosition(x + 0.5f, y + 0.5f, z + 0.5f);
//			CaveGame.getInstance().world.addEntity(ft);
		}

		public void generate(SubChunk sc, int block)
		{
			for (int i = -radius; i < radius; i++)
			{
				for (int j = -radius; j < radius; j++)
				{
					for (int k = -radius; k < radius; k++)
					{
						double distance = i * i + j * j + k * k;
						double distanceSqrd = Math.sqrt(distance);

						if (distanceSqrd < radius)
						{
							sc.setBlock(i + x, j + y, z + k, block);
						}
					}
				}
			}
		}
	}
}
