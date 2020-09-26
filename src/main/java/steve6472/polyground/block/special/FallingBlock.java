package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.09.2020
 * Project: CaveGame
 *
 ***********************/
public class FallingBlock extends Block
{
	public FallingBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public void tick(BlockState state, World world, int x, int y, int z)
	{
		if (world.getBlock(x, y - 1, z) == Block.air && y > 0)
		{
			world.setBlock(Block.air, x, y, z);
			world.getEntityManager().addEntity(new steve6472.polyground.entity.FallingBlock(state, x, y, z));
		}
	}

	@Override
	public boolean isTickable()
	{
		return true;
	}
}
