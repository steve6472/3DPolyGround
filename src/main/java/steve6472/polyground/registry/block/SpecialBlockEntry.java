package steve6472.polyground.registry.block;

import steve6472.polyground.block.Block;

import java.io.File;

public class SpecialBlockEntry<T extends Block>
{
	private ISpecialBlockFactory<T> factory;

	SpecialBlockEntry(ISpecialBlockFactory<T> factory)
	{
		this.factory = factory;
	}

	public T createNew(File f)
	{
		return factory.create(f);
	}
}