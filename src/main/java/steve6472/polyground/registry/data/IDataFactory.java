package steve6472.polyground.registry.data;

import steve6472.polyground.block.blockdata.BlockData;

@FunctionalInterface
public interface IDataFactory<T extends BlockData>
{
	T create();
}