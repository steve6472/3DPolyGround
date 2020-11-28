package steve6472.polyground.block.special.logic;

import org.json.JSONObject;
import steve6472.polyground.block.blockdata.BlockData;
import steve6472.polyground.block.blockdata.logic.data.AdvancedLogicBlockData;
import steve6472.polyground.block.states.BlockState;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 26.09.2020
 * Project: CaveGame
 *
 ***********************/
public class AdvancedLogicBlock extends LogicBlock
{
	public AdvancedLogicBlock(JSONObject json)
	{
		super(json);
	}

	@Override
	public BlockData createNewBlockEntity(BlockState state)
	{
		return new AdvancedLogicBlockData();
	}

	@Override
	protected int getSize()
	{
		return 32;
	}
}
