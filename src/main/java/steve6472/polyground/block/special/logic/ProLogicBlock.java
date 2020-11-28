package steve6472.polyground.block.special.logic;

import org.json.JSONObject;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.logic.data.ProLogicBlockData;
import steve6472.polyground.block.states.BlockState;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.09.2020
 * Project: CaveGame
 *
 ***********************/
public class ProLogicBlock extends LogicBlock
{
	public ProLogicBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public BlockData createNewBlockEntity(BlockState state)
	{
		return new ProLogicBlockData();
	}

	@Override
	protected int getSize()
	{
		return 48;
	}
}
