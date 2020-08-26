package steve6472.polyground.world.generator.feature.components.provider;

import org.json.JSONArray;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.08.2020
 * Project: CaveGame
 *
 ***********************/
public class SimpleStateProvider implements IBlockProvider
{
	BlockState[] states;

	@Override
	public void load(JSONArray array)
	{
		states = new BlockState[array.length()];

		for (int i = 0; i < array.length(); i++)
		{
			states[i] = Blocks.loadStateFromJSON(array.getJSONObject(i));
		}
	}

	@Override
	public BlockState getState(World world, int x, int y, int z)
	{
		return states[(int) (hash(world.getSeed(), x, y, z) & (states.length - 1))];
	}

	private long hash(long seed, int x, int y, int z)
	{
		long h = seed + x * 374761393L + y * 668265263L + z * 2147483647L;
		h = (h ^ (h >> 14)) * 1274126177L;
		return h ^ (h >> 17);
	}
}
