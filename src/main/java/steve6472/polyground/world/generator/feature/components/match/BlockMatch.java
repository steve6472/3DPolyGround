package steve6472.polyground.world.generator.feature.components.match;

import org.json.JSONArray;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.Blocks;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 21.08.2020
 * Project: CaveGame
 *
 ***********************/
public class BlockMatch implements IBlockMatch
{
	Block[] blocks;

	@Override
	public void load(JSONArray array)
	{
		blocks = new Block[array.length()];
		for (int i = 0; i < array.length(); i++)
		{
			blocks[i] = Blocks.getBlockByName(array.getString(i));
		}
	}

	@Override
	public boolean matches(BlockState state)
	{
		for (Block b : blocks)
			if (state.getBlock() == b)
				return true;

		return false;
	}
}
