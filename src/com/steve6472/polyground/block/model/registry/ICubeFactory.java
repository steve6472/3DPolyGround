package com.steve6472.polyground.block.model.registry;

import org.joml.AABBf;

@FunctionalInterface
public interface ICubeFactory<T extends Cube>
{
	T create(AABBf aabb);
}