package com.steve6472.polyground.world.biomes.registry;

import com.steve6472.polyground.world.biomes.Biome;

@FunctionalInterface
public interface IBiomeFactory<T extends Biome>
{
	T create();
}