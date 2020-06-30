package steve6472.polyground.world.generator.feature;

import org.joml.Vector2f;
import steve6472.polyground.block.Block;
import steve6472.polyground.world.chunk.SubChunk;

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
	public void generate(SubChunk sc, int x, int y, int z)
	{
		for (int i = -radius; i <= radius; i++)
		{
			for (int j = -radius; j <= radius; j++)
			{
				if (sc.getWorld().getRandom().nextFloat() < chance)
				{
					if (sc.getBlockEfficiently(x + i, y, z + j) == blockUnder)
					{
						if (decayFromCenter)
						{
							if (sc.getWorld().getRandom().nextFloat() < 1d / Vector2f.distanceSquared(x, y, x + i, y + j))
								sc.setBlockEfficiently(x + i, y + 1, z + j, blockToPlace);
						} else
						{
							sc.setBlockEfficiently(x + i, y + 1, z + j, blockToPlace);
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
	public boolean canGenerate(SubChunk sc, int x, int y, int z)
	{
		return sc.getBlockId(x, y, z) == blockUnder.getId();
	}

	@Override
	public EnumFeaturePlacement getPlacement()
	{
		return EnumFeaturePlacement.IN_HEIGHT_MAP;
	}
}
