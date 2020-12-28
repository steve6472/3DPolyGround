package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.EnumFace;
import steve6472.polyground.block.Tags;
import steve6472.polyground.block.model.elements.Bakery;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.enums.EnumTreeType;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;
import steve6472.polyground.entity.player.Player;
import steve6472.polyground.registry.Blocks;
import steve6472.polyground.world.ModelBuilder;
import steve6472.polyground.world.World;
import steve6472.polyground.world.chunk.ModelLayer;
import steve6472.sge.main.util.RandomUtil;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 09.10.2020
 * Project: CaveGame
 *
 ***********************/
public class SaplingBlock extends CustomBlock
{
	public static EnumProperty<EnumTreeType> TREE_TYPE = States.TREE_TYPE;

	public SaplingBlock(JSONObject json)
	{
		super(json);
		setDefaultState(getDefaultState().with(TREE_TYPE, EnumTreeType.OAK));
	}

	@Override
	public BlockState getStateForPlacement(World world, Player player, EnumFace placedOn, int x, int y, int z)
	{
		return getDefaultState().with(TREE_TYPE, EnumTreeType.getValues()[RandomUtil.randomInt(0, EnumTreeType.getValues().length - 1)]).get();
	}

	@Override
	public int createModel(int x, int y, int z, World world, BlockState state, ModelBuilder buildHelper, ModelLayer modelLayer)
	{
		if (modelLayer != ModelLayer.NORMAL)
			return 0;

		int tris = 0;

		buildHelper.setSubChunk(world.getSubChunkFromBlockCoords(x, y, z));

		final EnumTreeType type = state.get(TREE_TYPE);

		tris += Bakery.autoTexturedCube(7, 0, 7, 2, 4, 2, type.getLogTexture(), 32);
		tris += Bakery.autoTexturedCube(4, 4, 4, 8, 8, 8, type.getLeavesTexture(), 0, 63);


		return tris;
	}

	@Override
	public boolean isValidPosition(BlockState state, World world, int x, int y, int z)
	{
		BlockState st = world.getState(x, y - 1, z);

		if (st.hasTag(Tags.FLOWER_TOP))
			return super.isValidPosition(state, world, x, y, z);

		return false;
	}

	@Override
	public void randomTick(BlockState state, World world, int x, int y, int z)
	{
//		if (world.getRandom().nextInt(10) != 8)
//			return;

		final EnumTreeType type = state.get(TREE_TYPE);

		world.setState(Blocks.getDefaultState("branch").with(BranchBlock.LEAVES, false).with(BranchBlock.RADIUS, 0).with(BranchBlock.TREE_TYPE, type), x, y, z);
		world.setState(Blocks.getDefaultState(type.getLeaves()), x, y + 1, z);
		world.setState(Blocks.getDefaultState("root").with(RootBlock.TREE_TYPE, type), x, y - 1, z);
	}

	@Override
	public boolean randomTickable()
	{
		return true;
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(TREE_TYPE);
	}
}
