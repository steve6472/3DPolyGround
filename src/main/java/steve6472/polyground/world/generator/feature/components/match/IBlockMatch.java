package steve6472.polyground.world.generator.feature.components.match;

import org.json.JSONArray;
import steve6472.polyground.block.states.BlockState;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 21.08.2020
 * Project: CaveGame
 *
 ***********************/
public interface IBlockMatch
{
	void load(JSONArray array);

	boolean matches(BlockState state);
}
