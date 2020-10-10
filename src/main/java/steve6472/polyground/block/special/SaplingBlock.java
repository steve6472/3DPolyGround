package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.10.2020
 * Project: CaveGame
 *
 ***********************/
public class SaplingBlock extends CustomBlock
{
	public SaplingBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		if (modelLayer != ModelLayer.NORMAL)
			return 0;

		int tris = 0;

		buildHelper.setSubChunk(world.getSubChunkFromBlockCoords(x, y, z));

		tris += Bakery.autoTexturedCube(7, 0, 7, 2, 4, 2, "block/wood/log/oak_log", 32);
		tris += Bakery.autoTexturedCube(4, 4, 4, 8, 8, 8, "block/leaves/oak_leaves", 0, 63);

		return tris;
	}

	@Override
	public void randomTick(BlockState state, World world, int x, int y, int z)
	{
//		if (world.getRandom().nextInt(10) != 8)
//			return;

		world.setState(Blocks.getDefaultState("branch").with(BranchBlock.LEAVES, false).with(BranchBlock.RADIUS, 0).get(), x, y, z);
		world.setState(Blocks.getDefaultState("oak_leaves"), x, y + 1, z);
	}

	@Override
	public boolean randomTickable()
	{
		return true;
	}
}
