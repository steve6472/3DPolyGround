package steve6472.polyground.world.generator.feature.components.match;

import org.json.JSONArray;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.Blocks;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 21.08.2020
 * Project: CaveGame
 *
 ***********************/
public class StateMatch implements IBlockMatch
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
	public boolean matches(BlockState state)
	{
		for (BlockState s : states)
			if (s == state)
				return true;
		return false;
	}
}
