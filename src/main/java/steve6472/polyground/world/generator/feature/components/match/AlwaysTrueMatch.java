package steve6472.polyground.world.generator.feature.components.match;

import org.json.JSONArray;
import steve6472.polyground.block.states.BlockState;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 21.08.2020
 * Project: CaveGame
 *
 ***********************/
public class AlwaysTrueMatch implements IBlockMatch
{
	@Override
	public void load(JSONArray array)
	{
	}

	@Override
	public boolean matches(BlockState state)
	{
		return true;
	}
}
