package com.steve6472.polyground.world.generator;

import com.steve6472.polyground.block.Block;
import com.steve6472.polyground.block.registry.BlockRegistry;
import com.steve6472.polyground.world.chunk.SubChunk;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 14.09.2019
 * Project: SJP
 *
 ***********************/
public class FlatGenerator implements IGenerator
{
	@Override
	public void generate(SubChunk subChunk)
	{
		for (int i = 0; i < subChunk.getIds().length; i++)
		{
			for (int j = 0; j < subChunk.getIds()[i].length; j++)
			{
				for (int k = 0; k < subChunk.getIds()[i][j].length; k++)
				{
					subChunk.getIds()[i][j][k] = Block.air.getId();

//					float f = SimplexNoise.noise(i / 20f + getX() * 16f / 20f, k / 20f + getZ() * 16f / 20f);
//					f = (f + 1f) / 2f * 4f + 16f;
//					if (f > j + layer * 16)
//					{
//						ids[i][j][k] = BlockRegistry.getBlockIdByName("stone");
//					}

					if (j == 0 && subChunk.getLayer() == 0)
						subChunk.getIds()[i][j][k] = BlockRegistry.getBlockIdByName("stone");
				}
			}
		}
	}
}
