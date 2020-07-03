package steve6472.polyground.world.generator;

import steve6472.polyground.block.Block;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.world.chunk.SubChunk;
import org.joml.SimplexNoise;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 23.08.2019
 * Project: SJP
 *
 ***********************/
public class SimplexGenerator implements IGenerator
{
	public void generate(SubChunk sc)
	{
		float res = 20f;

		Block grass = BlockRegistry.getBlockByName("grass");
		Block dirt = BlockRegistry.getBlockByName("dirt");
		Block stone = BlockRegistry.getBlockByName("stone");
		Block cobblestone = BlockRegistry.getBlockByName("cobblestone");

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					float f = SimplexNoise.noise(i / res + sc.getX() * 16f / res, j / res + sc.getLayer() * 16f / res, k / res + sc.getZ() * 16f / res);
					if (f <= 0.0f)
						sc.setBlock(Block.air, i, j, k);
					else if (f > 0.0f && f < 0.2f)
						sc.setBlock(grass, i, j, k);
					else if (f >= 0.2f && f < 0.5f)
						sc.setBlock(dirt, i, j, k);
					else if (f >= 0.5f && f < 0.75f)
						sc.setBlock(stone, i, j, k);
					else if (f >= 0.5f)
						sc.setBlock(cobblestone, i, j, k);
				}
			}
		}

		for (int i = 0; i < 16; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				for (int k = 0; k < 16; k++)
				{
					if (j > 0)
						if (sc.getBlock(i, j, k) != Block.air && sc.getBlock(i, j - 1, k) == grass)
							sc.setBlock(dirt, i, j - 1, k);
				}
			}
		}
	}
}
