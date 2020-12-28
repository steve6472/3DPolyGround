package steve6472.polyground.world.generator.feature;

import org.json.JSONObject;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.blockdata.RootBlockData;
import steve6472.polyground.block.special.BranchBlock;
import steve6472.polyground.block.special.SaplingBlock;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.tree.Tree;
import steve6472.polyground.world.World;
import steve6472.polyground.world.generator.EnumPlacement;
import steve6472.polyground.world.generator.Feature;
import steve6472.polyground.world.generator.feature.components.match.IBlockMatch;
import steve6472.polyground.world.generator.feature.components.match.Match;
import steve6472.polyground.world.generator.feature.components.provider.IBlockProvider;
import steve6472.polyground.world.generator.feature.components.provider.Provider;
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
	private IBlockProvider saplingBlock;

	public SaplingFeature()
	{
	}

	@Override
	public void load(JSONObject json)
	{
		blockUnderMatch = Match.match(json.getJSONObject("match_under"));
		saplingBlock = Provider.provide(json.getJSONObject("sapling"));
	}

	@Override
	public void generate(World world, int x, int y, int z)
	{
//		long start = System.nanoTime();
		if (blockUnderMatch.matches(world.getState(x, y, z)))
		{
			world.setState(saplingBlock.getState(world, x, y, z), x, y + 1, z);

			boolean giantFix = false;
			Tree tree = null;
			int tickCount = RandomUtil.randomInt(150, 200);
			for (int i = 0; i < tickCount; i++)
			{
				BlockState s = world.getState(x, y + 1, z);
				if (s == saplingBlock.getState(world, x, y, z))
				{
					s.getBlock().randomTick(s, world, x, y + 1, z);
				} else
				{
					if (tree == null)
					{
						tree = new Tree();
						tree.analyze(world, x, y + 1, z);
					}
					tree.grow(world);
				}

				if (!giantFix)
				{
					Block root = world.getBlock(x, y, z);
					if (root.name.equals("root"))
					{
						final RootBlockData data = (RootBlockData) world.getData(x, y, z);
						if (data.isGiant)
						{
							tickCount += 1000;
						}
					}
					giantFix = true;
				}
			}
		}
//		System.out.println("Took: " + (System.nanoTime() - start) / 1_000_000d + "ms");
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
