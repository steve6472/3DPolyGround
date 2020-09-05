package steve6472.polyground.block.special;

import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 05.09.2020
 * Project: CaveGame
 *
 ***********************/
public interface ILightBlock
{
	void spawnLight(BlockState state, World world, int x, int y, int z);
}
