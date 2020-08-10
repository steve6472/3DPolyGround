package steve6472.polyground.world.generator.feature;

import org.joml.Vector3f;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.World;
import steve6472.polyground.world.generator.EnumPlacement;
import steve6472.polyground.world.generator.IFeature;
import steve6472.sge.main.Util;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.08.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class TileReplacePatchFeature implements IFeature
{
	private final BlockState target, replace;
	private final double chance;
	private final int radius;
	private final boolean decayFromCenter;

	public TileReplacePatchFeature(BlockState target, BlockState replace, double chance, int radius, boolean decayFromCenter)
	{
		this.target = target;
		this.replace = replace;
		this.chance = chance;
		this.radius = Util.clamp(0, 7, radius);
		this.decayFromCenter = decayFromCenter;
	}

	@Override
	public void generate(World world, int x, int y, int z)
	{
		for (int i = -radius; i <= radius; i++)
		{
			for (int j = -radius; j <= radius; j++)
			{
				for (int k = -radius; k <= radius; k++)
				{
					if (world.getRandom().nextDouble() <= chance)
					{
						if (decayFromCenter)
						{
							if (world.getRandom().nextFloat() < 1d / Vector3f.distanceSquared(x, y, z, x + i, y + j, z + k))
								if (world.getState(x + i, y + j, z + k) == target)
									world.setState(replace, x + i, y + j, z + k);
						} else
						{
							if (world.getState(x + i, y + j, z + k) == target)
								world.setState(replace, x + i, y + j, z + k);
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
		return world.getState(x, y, z) == target;
	}

	@Override
	public EnumPlacement getPlacement()
	{
		return EnumPlacement.IN_HEIGHT_MAP;
	}
}
