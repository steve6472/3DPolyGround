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

//		float Y = (float) Math.sin(Math.toRadians((System.currentTimeMillis() % 3600) * 0.1f)) * 0.2f + 0.25f;
//		float rot = (float) Math.toRadians((System.currentTimeMillis() % 3600) / 10f);
//
//		float c = (float) (Math.cos(Math.toRadians((System.currentTimeMillis() % (3600 * 2)) * 0.05f)) / 4f + 0.70f);
//
//		stack.color(c, 0, c, 1);
//		stack.translate(0, Y, 0);
//		stack.translate(0.5f, 0.5f, 0.5f);
//		stack.rotate(rot, 0, 1, 0);
//		stack.rotate(rot, 0, 0, 1);
//		stack.rotate(rot, 1, 0, 0);
//		stack.translate(-0.5f, -0.5f, -0.5f);
//		StackUtil.rectShade(stack, 0.25f, 0.25f, 0.25f, 0.5f, 0.5f, 0.5f);
	}

	@Override
	public BlockData createNewBlockEntity(BlockState state)
	{
		return new AmethineCoreData();
	}
}
