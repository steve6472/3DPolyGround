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

		int grass = BlockRegistry.getBlockIdByName("grass");
		int dirt = BlockRegistry.getBlockIdByName("dirt");
		int stone = BlockRegistry.getBlockIdByName("stone");
		int cobblestone = BlockRegistry.getBlockIdByName("cobblestone");

		for (int i = 0; i < sc.getIds().length; i++)
		{
			for (int j = 0; j < sc.getIds()[i].length; j++)
			{
				for (int k = 0; k < sc.getIds()[i][j].length; k++)
				{
					float f = SimplexNoise.noise(i / res + sc.getX() * 16f / res, j / res + sc.getLayer() * 16f / res, k / res + sc.getZ() * 16f / res);
					if (f <= 0.0f)
						sc.getIds()[i][j][k] = Block.air.getId();
					else if (f > 0.0f && f < 0.2f)
						sc.getIds()[i][j][k] = grass;
					else if (f >= 0.2f && f < 0.5f)
						sc.getIds()[i][j][k] = dirt;
					else if (f >= 0.5f && f < 0.75f)
						sc.getIds()[i][j][k] = stone;
					else if (f >= 0.5f)
						sc.getIds()[i][j][k] = cobblestone;
				}
			}
		}

		for (int i = 0; i < sc.getIds().length; i++)
		{
			for (int j = 0; j < sc.getIds()[i].length; j++)
			{
				for (int k = 0; k < sc.getIds()[i][j].length; k++)
				{
					if (j > 0)
						if (sc.getIds()[i][j][k] != Block.air.getId() && sc.getIds()[i][j - 1][k] == grass)
							sc.getIds()[i][j - 1][k] = dirt;
				}
			}
		}
	}
}
