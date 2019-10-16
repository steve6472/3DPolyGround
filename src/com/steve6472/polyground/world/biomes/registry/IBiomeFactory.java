package com.steve6472.polyground.world.biomes.registry;

import com.steve6472.polyground.block.model.registry.Cube;
import org.joml.AABBf;

@FunctionalInterface
public interface IBiomeFactory<T extends Cube>
{
	T create(AABBf aabb);
}