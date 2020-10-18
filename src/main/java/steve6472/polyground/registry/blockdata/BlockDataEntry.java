package steve6472.polyground.registry.blockdata;

import steve6472.polyground.block.blockdata.BlockData;

public record BlockDataEntry<T extends BlockData>(IBlockDataFactory<T> factory, String id)
{
	public T createNew()
	{
		return factory.create();
	}
}