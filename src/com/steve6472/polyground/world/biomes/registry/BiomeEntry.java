package com.steve6472.polyground.world.biomes.registry;

import com.steve6472.polyground.world.biomes.Biome;

public class BiomeEntry<T extends Biome>
{
	private IBiomeFactory<T> factory;

	BiomeEntry(IBiomeFactory<T> factory)
	{
		this.factory = factory;
	}

	public T createNew()
	{
		return factory.create();
	}
}