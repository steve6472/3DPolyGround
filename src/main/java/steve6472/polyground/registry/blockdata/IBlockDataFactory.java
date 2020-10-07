package steve6472.polyground.registry.blockdata;

import steve6472.polyground.block.blockdata.BlockData;

@FunctionalInterface
public interface IBlockDataFactory<T extends BlockData>
{
	T create();
}