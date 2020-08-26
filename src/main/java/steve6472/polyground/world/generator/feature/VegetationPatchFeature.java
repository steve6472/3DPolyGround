package steve6472.polyground.world.generator.feature;

import org.joml.Vector2f;
import org.json.JSONObject;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.World;
import steve6472.polyground.world.generator.EnumPlacement;
import steve6472.polyground.world.generator.Feature;
import steve6472.polyground.world.generator.feature.components.match.IBlockMatch;
import steve6472.polyground.world.generator.feature.components.match.Match;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.05.2020
 * Project: CaveGame
 *
 ***********************/
public class VegetationPatchFeature extends Feature
{
//	private BlockState blockUnder;
	private IBlockMatch blockUnder;
	private BlockState blockToPlace;
	private double chance;
	private int radius;
	private boolean decayFromCenter;

	public VegetationPatchFeature()
	{
	}
/*
	public VegetationPatchFeature(BlockState blockUnder, BlockState blockToPlace, double chance, int radius, boolean decayFromCenter)
	{
		this.blockUnder = blockUnder;
		this.blockToPlace = blockToPlace;
		this.chance = chance;
		this.radius = radius;
		this.decayFromCenter = decayFromCenter;
	}*/

	@Override
	public void load(JSONObject json)
	{
		blockUnder = Match.match(json.getJSONObject("block_under"));
//		blockUnder = Blocks.loadStateFromJSON(json.getJSONObject("block_under"));
		blockToPlace = Blocks.loadStateFromJSON(json.getJSONObject("block"));
		radius = json.getInt("radius");
		chance = json.getDouble("chance");
		decayFromCenter = json.getBoolean("decay");
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
//					if (world.getState(x + i, y - 1, z + j) == blockUnder)
					if (blockUnder.matches(world.getState(x + i, y - 1, z + j)))
					{
						if (decayFromCenter)
						{
							if (world.getRandom().nextFloat() < 1d / Vector2f.distanceSquared(x, y, x + i, y + j))
								world.setState(blockToPlace, x + i, y, z + j, 5);
						} else
						{
							world.setState(blockToPlace, x + i, y, z + j, 5);
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
		return blockUnder.matches(world.getState(x, y - 1, z));
//		return world.getState(x, y - 1, z) == blockUnder;
	}

	@Override
	public EnumPlacement getPlacement()
	{
		return EnumPlacement.ON_HEIGHT_MAP;
	}
}
