package com.steve6472.polyground.block.registry;

import com.steve6472.polyground.block.Block;

import java.io.File;

public class SpecialBlockEntry<T extends Block>
{
	private ISpecialBlockFactory<T> factory;

	SpecialBlockEntry(ISpecialBlockFactory<T> factory)
	{
		this.factory = factory;
	}

	public T createNew(File f, int id)
	{
		return factory.create(f, id);
	}
}