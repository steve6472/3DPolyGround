package steve6472.polyground.block.registry;

import steve6472.polyground.block.Block;

import java.io.File;

@FunctionalInterface
public interface ISpecialBlockFactory<T extends Block>
{
	T create(File f, int id);
}