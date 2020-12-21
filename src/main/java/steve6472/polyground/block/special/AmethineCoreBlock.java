package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.ISpecialRender;
import steve6472.polyground.block.blockdata.AmethineCoreData;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.IBlockData;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.gfx.stack.Stack;
import steve6472.polyground.world.World;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 22.10.2020
 * Project: CaveGame
 *
 ***********************/
public class AmethineCoreBlock extends Block implements IBlockData, ISpecialRender
{
	public AmethineCoreBlock(JSONObject json)
	{
		super(json);
		isFull = false;
	}

	@Override
	public void render(Stack stack, World world, BlockState state, int x, int y, int z)
	{
		AmethineCoreData data = (AmethineCoreData) world.getData(x, y, z);

		data.render(stack);
	}

	@Override
	public BlockData createNewBlockEntity(BlockState state)
	{
		return new AmethineCoreData();
	}
}
