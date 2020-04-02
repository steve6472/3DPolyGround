package steve6472.polyground.item.registry;

import steve6472.polyground.item.Item;

import java.io.File;

@FunctionalInterface
public interface ISpecialItemFactory<T extends Item>
{
	T create(File f, int id);
}