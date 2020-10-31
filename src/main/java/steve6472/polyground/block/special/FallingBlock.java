package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
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
	public void onBlockAdded(BlockState state, World world, BlockState oldState, int x, int y, int z)
	{
		if (world.getBlock(x, y - 1, z) == Block.AIR && y > 0)
		{
			world.setBlock(Block.AIR, x, y, z);
			world.getEntityManager().addEntity(new steve6472.polyground.entity.FallingBlock(state, x, y, z));
		}
	}

	@Override
	public void neighbourChange(BlockState state, World world, EnumFace updateFrom, int x, int y, int z)
	{
		if (world.getBlock(x, y - 1, z) == Block.AIR && y > 0)
		{
			world.setBlock(Block.AIR, x, y, z);
			world.getEntityManager().addEntity(new steve6472.polyground.entity.FallingBlock(state, x, y, z));
		}
	}
}
