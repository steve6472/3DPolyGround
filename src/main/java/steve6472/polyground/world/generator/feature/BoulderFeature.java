package steve6472.polyground.world.generator.feature;

import org.joml.Vector2d;
import org.json.JSONObject;
import steve6472.polyground.block.states.BlockState;
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
 * On date: 20.10.2020
 * Project: CaveGame
 *
 ***********************/
public class BoulderFeature extends Feature
{
	private IBlockProvider block, slab, deco;
	private IBlockMatch target;

	@Override
	public void load(JSONObject json)
	{
		block = Provider.provide(json.getJSONObject("block"));
		slab = Provider.provide(json.getJSONObject("slab"));
		deco = Provider.provide(json.getJSONObject("deco"));
		target = Match.match(json.getJSONObject("block_under"));
	}

	float smoothstep(float edge0, float edge1, float x)
	{
		// Scale, bias and saturate x to 0..1 range
		x = clamp((x - edge0) / (edge1 - edge0), 0.0f, 1.0f);
		// Evaluate polynomial
		return x * x * (3 - 2 * x);
	}

	float clamp(float x, float lowerlimit, float upperlimit)
	{
		if (x < lowerlimit)
			x = lowerlimit;
		if (x > upperlimit)
			x = upperlimit;
		return x;
	}

	@Override
	public void generate(World world, int x, int y, int z)
	{
		float[][] grid = new float[RandomUtil.randomInt(4, 6)][RandomUtil.randomInt(4, 6)];
//		float[][] grid = new float[7][7];

		int cx = grid.length / 2;
		int cz = grid[0].length / 2;

		for (int r = 0; r < 3; r++)
		{
			for (int i = 0; i < grid.length; i++)
			{
				for (int j = 0; j < grid[i].length; j++)
				{
					double dist = Vector2d.distance(cx, cz, i, j);
					dist *= dist;
					dist += 1;
					grid[i][j] += RandomUtil.randomFloat(0, smoothstep(0, 0.5f, (float) (1f / dist)));
				}
			}
		}

		for (int i = 0; i < grid.length; i++)
		{
			for (int j = 0; j < grid[i].length; j++)
			{
				float g = grid[i][j];

				int flor = (int) Math.floor(g);
				float decimal = g - flor;

				int px = x + j - cx;
				int pz = z + i - cz;

				for (int k = 0; k <= flor; k++)
				{
					int py = y + k;

					BlockState state = world.getState(px, py, pz);

					if (state.getBlock().isReplaceable(state))
					{
						// top
						if (k == flor)
						{
							if (decimal > 0.7)
							{
								world.setState(slab.getState(world, px, py, pz), px, py, pz);
							} else if (decimal > 0.3 && RandomUtil.decide(3))
							{
								world.setState(deco.getState(world, px, py, pz), px, py, pz);
							}
						} else
						{
							world.setState(block.getState(world, px, py, pz), px, py, pz);
						}
					}
				}
			}
		}
	}

	/**
	 * Specifies how many chunks in square radius have to be present for this feature to generate
	 * max is 3
	 * 0 is self
	 *
	 * @return size
	 */
	@Override
	public int size()
	{
		return 1;
	}

	@Override
	public boolean canGenerate(World world, int x, int y, int z)
	{
		return target.matches(world.getState(x, y - 1, z));
	}

	@Override
	public EnumPlacement getPlacement()
	{
		return EnumPlacement.ON_HEIGHT_MAP;
	}
}
