package steve6472.polyground.world.generator.feature;

import org.json.JSONObject;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.special.BranchBlock;
import steve6472.polyground.block.special.SaplingBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.World;
import steve6472.polyground.world.generator.EnumPlacement;
import steve6472.polyground.world.generator.Feature;
import steve6472.polyground.world.generator.feature.components.match.IBlockMatch;
import steve6472.polyground.world.generator.feature.components.match.Match;
import steve6472.sge.main.util.RandomUtil;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.05.2020
 * Project: CaveGame
 *
 ***********************/
public class SaplingFeature extends Feature
{
	private IBlockMatch blockUnderMatch;

	public SaplingFeature()
	{
	}

	@Override
	public void load(JSONObject json)
	{
		blockUnderMatch = Match.match(json.getJSONObject("match_under"));
	}

	@Override
	public void generate(World world, int x, int y, int z)
	{
		if (blockUnderMatch.matches(world.getState(x, y, z)))
		{
			world.setBlock(Blocks.getBlockByName("sapling"), x, y + 1, z);

			int tickCount = RandomUtil.randomInt(100, 150);
			for (int i = 0; i < tickCount; i++)
			{
				BlockState s = world.getState(x, y + 1, z);
				s.getBlock().randomTick(s, world, x, y + 1, z);
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
				for (int k = -2; k < 3; k++)
				{
					Block block = world.getBlock(x + i, y + k, z + j);
					if (block instanceof SaplingBlock || block instanceof BranchBlock)
						return false;
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
