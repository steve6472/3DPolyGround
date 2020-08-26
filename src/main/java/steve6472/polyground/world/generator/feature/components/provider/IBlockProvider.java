package steve6472.polyground.world.generator.feature.components.provider;

import org.json.JSONArray;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.08.2020
 * Project: CaveGame
 *
 ***********************/
public interface IBlockProvider
{
	void load(JSONArray array);

	BlockState getState(World world, int x, int y, int z);
}
