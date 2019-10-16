package com.steve6472.polyground.world.biomes.registry;

import com.steve6472.polyground.block.model.registry.Cube;
import org.joml.AABBf;

public class BiomeEntry<T extends Cube>
{
	private IBiomeFactory<T> factory;

	BiomeEntry(IBiomeFactory<T> factory)
	{
		this.factory = factory;
	}

	public T createNew(AABBf aabb)
	{
		return factory.create(aabb);
	}
}