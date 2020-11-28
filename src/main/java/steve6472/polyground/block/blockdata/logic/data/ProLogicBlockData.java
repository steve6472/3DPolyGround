package steve6472.polyground.block.blockdata.logic.data;

import steve6472.polyground.registry.blockdata.BlockDataRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2020
 * Project: CaveGame
 *
 ***********************/
public class ProLogicBlockData extends LogicBlockData
{
	@Override
	protected int getSize()
	{
		return 48;
	}

	@Override
	public String getId()
	{
		return BlockDataRegistry.proLogic.id();
	}
}
