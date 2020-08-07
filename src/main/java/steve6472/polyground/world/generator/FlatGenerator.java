package steve6472.polyground.world.generator;

import steve6472.polyground.block.Block;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.world.chunk.SubChunk;

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
		subChunk.stage = EnumChunkStage.FINISHED;
		if (subChunk.getLayer() != 0)
			return;

		Block stone = BlockRegistry.getBlockByName("stone");
//		int cobblestone = BlockRegistry.getBlockIdByName("cobblestone");

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
//				if (i == 15 || j == 15)
//					subChunk.getIds()[i][0][j] = cobblestone;
//				else
					subChunk.setBlock(stone, i, 0, j);
			}
		}

		subChunk.rebuild();

		/*
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
		}*/
	}
}
