package steve6472.polyground.world.generator.feature;

import org.joml.Vector3f;
import org.json.JSONObject;
import steve6472.polyground.block.Block;
import steve6472.polyground.world.World;
import steve6472.polyground.world.generator.EnumPlacement;
import steve6472.polyground.world.generator.Feature;
import steve6472.polyground.world.generator.feature.components.match.IBlockMatch;
import steve6472.polyground.world.generator.feature.components.match.Match;
import steve6472.polyground.world.generator.feature.components.provider.IBlockProvider;
import steve6472.polyground.world.generator.feature.components.provider.Provider;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.08.2020
 * Project: ThreadedGenerator
 *
 ***********************/
public class TileReplacePatchFeature extends Feature
{
	private IBlockMatch target;
	private IBlockProvider replace;
//	private BlockState target, replace;
	private double chance;
	private int radius;
	private boolean decayFromCenter;
	private boolean onlyTop;

	public TileReplacePatchFeature()
	{
	}

	/*
	public TileReplacePatchFeature(BlockState target, BlockState replace, double chance, int radius, boolean decayFromCenter, boolean onlyTop)
	{
		this.target = target;
		this.replace = replace;
		this.chance = chance;
		this.radius = Util.clamp(0, 7, radius);
		this.decayFromCenter = decayFromCenter;
		this.onlyTop = onlyTop;
	}*/

	@Override
	public void load(JSONObject json)
	{
		target = Match.match(json.getJSONObject("target"));
		replace = Provider.provide(json.getJSONObject("block"));
//		target = Blocks.loadStateFromJSON(json.getJSONObject("target"));
//		replace = Blocks.loadStateFromJSON(json.getJSONObject("block"));
		chance = json.getDouble("chance");
		radius = json.getInt("radius");
		decayFromCenter = json.getBoolean("decay_from_center");
		onlyTop = json.getBoolean("only_top");
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
						if (onlyTop)
							if (world.getBlock(x + i, y + j + 1, z + k) != Block.air)
								continue;

						if (decayFromCenter)
						{
							if (world.getRandom().nextFloat() < 1d / Vector3f.distanceSquared(x, y, z, x + i, y + j, z + k))
								if (target.matches(world.getState(x + i, y + j, z + k)))
									world.setState(replace.getState(world, x + i, y + j, z + k), x + i, y + j, z + k);
						} else
						{
							if (target.matches(world.getState(x + i, y + j, z + k)))
								world.setState(replace.getState(world, x + i, y + j, z + k), x + i, y + j, z + k);
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
		return target.matches(world.getState(x, y, z));
	}

	@Override
	public EnumPlacement getPlacement()
	{
		return EnumPlacement.IN_HEIGHT_MAP;
	}
}
