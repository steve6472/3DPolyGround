package steve6472.polyground.block.blockdata;

import steve6472.polyground.registry.blockdata.BlockDataRegistry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 06.10.2020
 * Project: CaveGame
 *
 ***********************/
public class ChiselBlockData extends AbstractPickableMicroBlockData
{
	public ChiselBlockData()
	{
		super();
	}

	@Override
	public String getId()
	{
		return BlockDataRegistry.chisel.id();
	}
}
