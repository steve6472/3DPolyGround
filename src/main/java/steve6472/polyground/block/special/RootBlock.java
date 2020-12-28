package steve6472.polyground.block.special;

import org.json.JSONObject;
import steve6472.polyground.block.Block;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.IBlockData;
import steve6472.polyground.block.blockdata.RootBlockData;
import steve6472.polyground.block.properties.EnumProperty;
import steve6472.polyground.block.properties.IProperty;
import steve6472.polyground.block.properties.enums.EnumTreeType;
import steve6472.polyground.block.states.BlockState;
import steve6472.polyground.block.states.States;

import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 18.10.2020
 * Project: CaveGame
 *
 ***********************/
public class RootBlock extends Block implements IBlockData
{
	public static final EnumProperty<EnumTreeType> TREE_TYPE = States.TREE_TYPE;

	public RootBlock(JSONObject json)
	{
		super(json);
		setDefaultState(getDefaultState().with(TREE_TYPE, EnumTreeType.OAK));
	}

	@Override
	public BlockData createNewBlockEntity(BlockState state)
	{
		return new RootBlockData(state.get(TREE_TYPE));
	}

	@Override
	public void fillStates(List<IProperty<?>> properties)
	{
		properties.add(TREE_TYPE);
	}
}
