package steve6472.polyground.world.generator.feature;

import org.joml.Vector2f;
import steve6472.polyground.block.Block;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.05.2020
 * Project: CaveGame
 *
 ***********************/
public class VegetationPatchFeature implements IFeature
{
	private final Block blockUnder;
	private final Block blockToPlace;
	private final double chance;
	private final int radius;
	private final boolean decayFromCenter;

	public VegetationPatchFeature(Block blockUnder, Block blockToPlace, double chance, int radius, boolean decayFromCenter)
	{
		this.blockUnder = blockUnder;
		this.blockToPlace = blockToPlace;
		this.chance = chance;
		this.radius = radius;
		this.decayFromCenter = decayFromCenter;
	}

	@Override
	public void generate(World world, int x, int y, int z)
	{
		for (int i = -radius; i <= radius; i++)
		{
			for (int j = -radius; j <= radius; j++)
			{
				if (world.getRandom().nextFloat() < chance)
				{
					if (world.getBlock(x + i, y, z + j) == blockUnder)
					{
						if (decayFromCenter)
						{
							if (world.getRandom().nextFloat() < 1d / Vector2f.distanceSquared(x, y, x + i, y + j))
								world.setBlock(blockToPlace, x + i, y + 1, z + j);
						} else
						{
							world.setBlock(blockToPlace, x + i, y + 1, z + j);
						}
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
		return world.getBlock(x, y, z) == blockUnder;
	}

	@Override
	public EnumFeaturePlacement getPlacement()
	{
		return EnumFeaturePlacement.IN_HEIGHT_MAP;
	}
}
