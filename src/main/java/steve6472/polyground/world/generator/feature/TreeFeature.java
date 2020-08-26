package steve6472.polyground.world.generator.feature;

import org.json.JSONObject;
import steve6472.polyground.block.Block;
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
public class TreeFeature extends Feature
{
	private IBlockMatch blockUnderMatch;
	private BlockState log, leaves, blockUnder;
	private int heightMin, heightMax;

	public TreeFeature()
	{
	}
/*
	public TreeFeature(BlockState blockUnderMatch, BlockState log, BlockState leaves, BlockState blockUnder)
	{
		this.blockUnderMatch = blockUnderMatch;
		this.log = log;
		this.leaves = leaves;
		this.blockUnder = blockUnder;
	}*/

	@Override
	public void load(JSONObject json)
	{
//		blockUnderMatch = Blocks.loadStateFromJSON(json.getJSONObject("match_under"));
		blockUnderMatch = Match.match(json.getJSONObject("match_under"));
		log = Blocks.loadStateFromJSON(json.getJSONObject("log"));
		leaves = Blocks.loadStateFromJSON(json.getJSONObject("leaves"));
		blockUnder = Blocks.loadStateFromJSON(json.getJSONObject("set_under"));
		heightMin = json.getInt("height_min");
		heightMax = json.getInt("height_max");
	}

	@Override
	public void generate(World world, int x, int y, int z)
	{
		int h = world.getRandom().nextInt(heightMax - heightMin) + heightMin;

		for (int i = 0; i < h; i++)
		{
			world.setState(log, x, y + i + 1, z);
		}
		world.setState(blockUnder, x, y, z);
		for (int i = -2; i < 3; i++)
		{
			for (int j = -2; j < 3; j++)
			{
				for (int k = 0; k < 2; k++)
				{
					if (Math.abs(i) % 3 != 2 || Math.abs(j) % 3 != 2)
					{
						if (world.getBlock(x + i, y + h - 2 + k, z + j) == Block.air)
							world.setState(leaves, x + i, y + h - 2 + k, z + j);
					}
				}
			}
		}
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				for (int k = 0; k < 2; k++)
				{
					if (Math.abs(i) % 2 != 1 || Math.abs(j) % 2 != 1)
					{
						if (world.getBlock(x + i, y + h + k, z + j) == Block.air)
							world.setState(leaves, x + i, y + h + k, z + j);
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
		if (!blockUnderMatch.matches(world.getState(x, y, z)))
			return false;

		for (int i = -2; i < 3; i++)
		{
			for (int j = -2; j < 3; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					if (Math.abs(i) % 3 != 2 || Math.abs(j) % 3 != 2)
					{
						if (world.getBlock(x + i, y + 5 - 2 + k, z + j) != Block.air)
							return false;
					}
				}
			}
		}
		for (int i = -1; i < 2; i++)
		{
			for (int j = -1; j < 2; j++)
			{
				for (int k = 0; k < 3; k++)
				{
					if (Math.abs(i) % 2 != 1 || Math.abs(j) % 2 != 1)
					{
						if (world.getBlock(x + i, y + 5 + k, z + j) != Block.air)
							return false;
					}
				}
			}
		}

		return true;
	}

	@Override
	public EnumPlacement getPlacement()
	{
		return EnumPlacement.IN_HEIGHT_MAP;
	}
}
