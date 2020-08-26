package steve6472.polyground.world.generator.feature.components.match;

import org.json.JSONArray;
import steve6472.polyground.block.states.BlockState;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 21.08.2020
 * Project: CaveGame
 *
 ***********************/
public class TagMatch implements IBlockMatch
{
	String[] tags;

	@Override
	public void load(JSONArray array)
	{
		tags = new String[array.length()];

		for (int i = 0; i < array.length(); i++)
		{
			tags[i] = array.getString(i);
		}
	}

	@Override
	public boolean matches(BlockState state)
	{
		for (String t : tags)
			if (state.hasTag(t))
				return true;
		return false;
	}
}

