package steve6472.polyground.block;

import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public interface ISpecialRender
{
	void render(Stack stack, World world, BlockState state, int x, int y, int z);
}
