package steve6472.polyground.world.generator.feature;

import steve6472.polyground.block.Block;
import steve6472.polyground.registry.BlockRegistry;
import steve6472.polyground.world.World;
import steve6472.polyground.world.generator.EnumPlacement;
import steve6472.polyground.world.generator.IFeature;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.05.2020
 * Project: CaveGame
 *
 ***********************/
public class LakeFeature implements IFeature
{
	private final Block b, c, d, e;

	public LakeFeature()
	{
		b = BlockRegistry.getBlockByName("grass");
		c = BlockRegistry.getBlockByName("sand");
		d = BlockRegistry.getBlockByName("dirt");
		e = BlockRegistry.getBlockByName("cobblestone");
	}

	@Override
	public void generate(World world, int x, int y, int z)
	{
		int w = world.getRandom().nextInt(4) + 3;
		int h = world.getRandom().nextInt(2) + 2;
		int d = w;

		for (int i = -w - 1; i < w + 1; i++)
		{
			for (int j = -h - 1; j <= 0; j++)
			{
				for (int k = -d - 1; k < d + 1; k++)
				{
					double X = Math.pow(((i + 0.5) / (float) (w + 2)), 2);
					double Y = Math.pow(((j + 0.5) / (float) (h + 2)), 2);
					double Z = Math.pow(((k + 0.5) / (float) (d + 2)), 2);

					if (X + Y + Z < 1)
					{
						Block a = world.getBlock(i + x, j + y, k + z);
						if (a == b || a == this.d || a == e)
							world.setBlock(c, i + x, j + y, k + z);
					}
				}
			}
		}

		for (int i = -w; i < w; i++)
		{
			for (int j = -h; j < h; j++)
			{
				for (int k = -d; k < d; k++)
				{
					double X = Math.pow(((i + 0.5) / (float) w), 2);
					double Y = Math.pow(((j + 0.5) / (float) h), 2);
					double Z = Math.pow(((k + 0.5) / (float) d), 2);

					if (X + Y + Z < 1)
					{
						world.setBlock(Block.air, i + x, j + y, k + z);
					}
				}
			}
		}
	}

	@Override
	public int size()
	{
		return 1;
	}

	@Override
	public boolean canGenerate(World world, int x, int y, int z)
	{
		return y > 8 && y < 30 && world.getBlock(x, y, z) == b;
	}

	@Override
	public EnumPlacement getPlacement()
	{
		return EnumPlacement.IN_HEIGHT_MAP;
	}
}
