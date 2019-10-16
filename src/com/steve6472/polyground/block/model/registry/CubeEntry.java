package com.steve6472.polyground.block.model.registry;

import org.joml.AABBf;

public class CubeEntry<T extends Cube>
{
	private ICubeFactory<T> factory;

	CubeEntry(ICubeFactory<T> factory)
	{
		this.factory = factory;
	}

	public T createNew(AABBf aabb)
	{
		return factory.create(aabb);
	}
}