package com.steve6472.polyground.item.registry;

import com.steve6472.polyground.item.Item;

import java.io.File;

public class SpecialItemEntry<T extends Item>
{
	private ISpecialItemFactory<T> factory;

	SpecialItemEntry(ISpecialItemFactory<T> factory)
	{
		this.factory = factory;
	}

	public T createNew(File f, int id)
	{
		return factory.create(f, id);
	}
}